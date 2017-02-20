package com.ebase.eox.rs;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;

public class RestRegistratorTest {

  private static final String TEST_CONTEXT = "test_context";

  private static final int TEST_REGISTRATIONS_COUNT = 5;

  private RestRegistrator restRegistratorUnderTest;

  @Mock
  private RestApplication mockedRestApplication;
  @Mock
  private ServiceRegistration<Servlet> mockedServiceRegistration;
  @Mock
  private ContextGenerator mockedContextGenerator;
  @Mock
  private BundleContext mockedBundleContext;
  @Mock
  private ComponentContext mockedComponentContext;
  @Mock
  private ServiceTracker<RestApplication, ServiceRegistration<Servlet>> mockedServiceTracker;
  @Mock
  private ServiceReference<RestApplication> mockedServiceReference;
  @Mock
  private Bundle mockedBundle;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    Mockito.when(mockedContextGenerator.generateContext(ArgumentMatchers.any()))
        .thenReturn(RestRegistratorTest.TEST_CONTEXT);

    Mockito.when(mockedComponentContext.getBundleContext()).thenReturn(mockedBundleContext);

    Mockito.when(mockedServiceReference.getBundle()).thenReturn(mockedBundle);

    Mockito.when(mockedBundle.getBundleContext()).thenReturn(mockedBundleContext);

    restRegistratorUnderTest = Mockito.spy(new RestRegistrator());
    restRegistratorUnderTest.contextGenerator = mockedContextGenerator;
  }

  @Test
  public void testActivate() {
    Mockito.doReturn(mockedServiceTracker).when(restRegistratorUnderTest)
        .createServiceTracker(ArgumentMatchers.eq(mockedBundleContext));

    restRegistratorUnderTest.activate(mockedComponentContext);

    Mockito.verify(mockedServiceTracker).open();
  }

  @Test
  public void testCreateServiceTracker() {
    final ServiceTracker<RestApplication, ServiceRegistration<Servlet>> actualServiceTracker =
        restRegistratorUnderTest.createServiceTracker(mockedBundleContext);

    Assert.assertNotNull(actualServiceTracker);
  }

  @Test
  public void testDeactivate() {
    restRegistratorUnderTest.serviceTracker = mockedServiceTracker;

    restRegistratorUnderTest.deactivate();

    Mockito.verify(mockedServiceTracker).close();
  }

  @Test
  public void testRemovedService() {
    restRegistratorUnderTest.removedService(mockedServiceReference, mockedServiceRegistration);

    Mockito.verify(mockedServiceRegistration).unregister();
  }

  @Test
  public void testModifiedService() {
    restRegistratorUnderTest.modifiedService(mockedServiceReference, mockedServiceRegistration);
  }

  @Test
  public void testAddingService() {
    final Servlet mockedServlet = Mockito.mock(Servlet.class);
    Mockito.when(restRegistratorUnderTest.createRestServlet(ArgumentMatchers.any()))
        .thenReturn(mockedServlet);
    final ArgumentCaptor<Dictionary<String, Object>> capturedProps =
        ArgumentCaptor.forClass((new Hashtable<String, Object>()).getClass());
    Mockito
        .when(mockedBundleContext.registerService(ArgumentMatchers.eq(Servlet.class),
            ArgumentMatchers.eq(mockedServlet), capturedProps.capture()))
        .thenReturn(mockedServiceRegistration);

    final ServiceRegistration<Servlet> actualServiceReference =
        restRegistratorUnderTest.addingService(mockedServiceReference);

    Mockito.verify(mockedBundleContext).registerService(ArgumentMatchers.eq(Servlet.class),
        ArgumentMatchers.eq(mockedServlet), capturedProps.capture());
    final Object actualContext =
        capturedProps.getValue().get(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN);
    Assert.assertEquals(RestRegistratorTest.TEST_CONTEXT, actualContext);
    Assert.assertEquals(actualServiceReference, mockedServiceRegistration);
  }
}
