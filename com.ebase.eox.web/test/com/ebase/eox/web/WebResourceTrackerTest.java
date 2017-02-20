package com.ebase.eox.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;

public class WebResourceTrackerTest {

  private static final int BUNDLE_STETES_TO_TRACK = Bundle.ACTIVE;

  @Mock
  private ComponentContext mockedContext;
  @Mock
  private BundleTracker<List<ServiceRegistration<WebResource>>> mockedBundleTracker;
  @Mock(name = "trackerBuilder")
  private BundleTrackerBuilder mockedTrackerBuilder;
  @Mock
  private Bundle mockedBundle;
  @Mock
  private WebResourceRegistrator mockedWebResourceRegistrator;
  @Mock
  private LogService mockedLogService;

  @InjectMocks
  private WebResourceTracker webResourceBundleTrackerUnderTest = new WebResourceTracker();


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(mockedTrackerBuilder.buildBundleTracker(any(), eq(BUNDLE_STETES_TO_TRACK),
        eq(webResourceBundleTrackerUnderTest))).thenReturn(mockedBundleTracker);

  }

  @Test
  public void shouldCreateBundleTrackerOnStart() throws Exception {
    webResourceBundleTrackerUnderTest.activate(mockedContext);

    BundleTracker<List<ServiceRegistration<WebResource>>> actualBundleTracker =
        webResourceBundleTrackerUnderTest.bundleTracker;
    assertThat(actualBundleTracker, equalTo(mockedBundleTracker));
  }

  @Test
  public void shouldStartBundleTrackerOnStart() throws Exception {
    webResourceBundleTrackerUnderTest.activate(mockedContext);

    verify(mockedBundleTracker).open();
  }

  @Test
  public void shouldDoNothingOnStopIfNoBundleTracker() throws Exception {
    webResourceBundleTrackerUnderTest.bundleTracker = null;

    webResourceBundleTrackerUnderTest.deactivate();

    verify(mockedBundleTracker, never()).close();
  }

  @Test
  public void shouldCloseBundleTrackerOnStop() throws Exception {
    webResourceBundleTrackerUnderTest.bundleTracker = mockedBundleTracker;

    webResourceBundleTrackerUnderTest.deactivate();

    verify(mockedBundleTracker).close();
  }

  @Test
  public void shouldClearBundleTrackerOnStop() throws Exception {
    webResourceBundleTrackerUnderTest.bundleTracker = mockedBundleTracker;

    webResourceBundleTrackerUnderTest.deactivate();

    BundleTracker<List<ServiceRegistration<WebResource>>> actualBundleTracker =
        webResourceBundleTrackerUnderTest.bundleTracker;
    assertThat(actualBundleTracker, nullValue());
  }

  @Test
  public void shouldCallRegistratorRegisterResourceOnAddingBundle()
      throws WebResourceRegistrationException {
    BundleEvent bundleEvent = new BundleEvent(BundleEvent.STARTED, mockedBundle);
    webResourceBundleTrackerUnderTest.addingBundle(mockedBundle, bundleEvent);

    verify(mockedWebResourceRegistrator).registerResources(eq(mockedBundle));
  }

  @Test
  public void shouldReturnNullOnAddingBundleIfRegistrationFailed()
      throws WebResourceRegistrationException {
    when(mockedWebResourceRegistrator.registerResources(eq(mockedBundle)))
        .thenThrow(WebResourceRegistrationException.class);

    BundleEvent bundleEvent = new BundleEvent(BundleEvent.STARTED, mockedBundle);
    List<ServiceRegistration<WebResource>> actualRegistrationList =
        webResourceBundleTrackerUnderTest.addingBundle(mockedBundle, bundleEvent);

    assertThat(actualRegistrationList, nullValue());
  }

  @Test
  public void shouldDoNothingOnModifiedBundle() {
    BundleEvent bundleEvent = new BundleEvent(BundleEvent.STARTED, mockedBundle);
    List<ServiceRegistration<WebResource>> serviceList = new ArrayList<>();
    webResourceBundleTrackerUnderTest.modifiedBundle(mockedBundle, bundleEvent, serviceList);
  }

  @Test
  public void shouldCallRegistratorRegisterUnresourceOnRemovingBundle() {
    BundleEvent bundleEvent = new BundleEvent(BundleEvent.STARTED, mockedBundle);
    List<ServiceRegistration<WebResource>> serviceList = new ArrayList<>();

    webResourceBundleTrackerUnderTest.removedBundle(mockedBundle, bundleEvent, serviceList);

    verify(mockedWebResourceRegistrator).unregisterResources(eq(serviceList));
  }

}
