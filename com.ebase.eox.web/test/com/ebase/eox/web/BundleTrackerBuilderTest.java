package com.ebase.eox.web;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class BundleTrackerBuilderTest {
  @Mock
  private BundleContext mockedContext;
  @Mock
  private BundleTrackerCustomizer<Object> mockedCustomizer;

  private BundleTrackerBuilder bundleTrackerBuilderUnderTest = new BundleTrackerBuilder();

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testBuildBundleTracker() {
    int stateMask = Bundle.ACTIVE;
    BundleTracker<Object> actualBundleTracker = bundleTrackerBuilderUnderTest
        .buildBundleTracker(mockedContext, stateMask, mockedCustomizer);

    assertThat(actualBundleTracker, notNullValue());
  }

}
