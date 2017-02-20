package com.ebase.eox.server.internal;

import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class ServerActivatorTest {

  private static final String TEST_URL_TEXT = "http://aaa";

  static final String KEYSTORE_PATH = "/jettyhome/etc/keystore";
  static final String JETTY_SERVICE_PID = "org.apache.felix.http";
  static final String FELIX_KEYSTORE = "org.apache.felix.https.keystore";

  private ServerActivator spyedServerActivatorUnderTest;

  @Mock
  private BundleContext mockedBundleContext;
  @Mock
  private ServiceReference<ConfigurationAdmin> mockedServiceReference;
  @Mock
  private Bundle mockedBundle;
  @Mock
  private ConfigurationAdmin mockedConfigAdmin;
  @Mock
  private Dictionary<String, Object> mockedProps;
  @Mock
  private Configuration mockedConfiguration;

  private URL stabbedUrl;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    stabbedUrl = new URL(ServerActivatorTest.TEST_URL_TEXT);

    Mockito.when(mockedBundleContext.getBundle()).thenReturn(mockedBundle);
    Mockito.when(mockedBundle.getEntry(ArgumentMatchers.eq(ServerActivatorTest.KEYSTORE_PATH)))
        .thenReturn(stabbedUrl);
    Mockito.when(mockedBundleContext.getService(ArgumentMatchers.eq(mockedServiceReference)))
        .thenReturn(mockedConfigAdmin);
    Mockito
        .when(mockedConfigAdmin.getConfiguration(
            ArgumentMatchers.eq(ServerActivatorTest.JETTY_SERVICE_PID), ArgumentMatchers.eq(null)))
        .thenReturn(mockedConfiguration);


    spyedServerActivatorUnderTest = Mockito.spy(new ServerActivator());
  }

  @Test
  public void testStartWhenConfigRef() throws Exception {
    Mockito
        .when(
            mockedBundleContext.getServiceReference(ArgumentMatchers.eq(ConfigurationAdmin.class)))
        .thenReturn(mockedServiceReference);
    Mockito.doReturn(mockedProps).when(spyedServerActivatorUnderTest)
        .getProperties(ArgumentMatchers.eq(mockedBundleContext));

    spyedServerActivatorUnderTest.start(mockedBundleContext);

    Mockito.verify(mockedConfigAdmin).getConfiguration(
        ArgumentMatchers.eq(ServerActivatorTest.JETTY_SERVICE_PID), ArgumentMatchers.eq(null));
    Mockito.verify(mockedConfiguration).update(ArgumentMatchers.eq(mockedProps));
  }

  @Test
  public void testGetProperties() throws IOException {
    final Dictionary<String, Object> props =
        spyedServerActivatorUnderTest.getProperties(mockedBundleContext);

    Assert.assertEquals(props.get(ServerActivatorTest.FELIX_KEYSTORE),
        ServerActivatorTest.TEST_URL_TEXT);

  }

  @Test
  public void testStop() throws Exception {
    spyedServerActivatorUnderTest.stop(mockedBundleContext);
  }

}
