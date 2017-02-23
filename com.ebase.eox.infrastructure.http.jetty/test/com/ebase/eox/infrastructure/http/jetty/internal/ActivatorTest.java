package com.ebase.eox.infrastructure.http.jetty.internal;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.jetty.osgi.boot.OSGiServerConstants;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.ebase.eox.infrastructure.http.jetty.internal.Activator;

public class ActivatorTest {

  private static final String OBFUSCTED_STORE_PASSWORD =
      "OBF:1j1w1rbw1r4z1vn61wgg1wfi1vnw1r2z1raa1iyy";

  private static final String ETC_KEYSTORE = "etc/keystore";

  private static final String TEST_URL_PREFIX = "http://b";

  private static final String JETTYHOME = "/jettyhome";
  private static final String JETTYHOME_ETC = "/jettyhome/etc";


  private static final String JETTY_SERVER_NAME = "com.ebase.eox.jetty.server";

  private static final String JETTY_XML = "jetty.xml";
  private static final String JETTY_HTTP_XML = "jetty-http.xml";
  private static final String JETTY_ALPN_XML = "jetty-alpn.xml";
  private static final String JETTY_HTTP2_XML = "jetty-http2.xml";
  private static final String JETTY_HTTP2C_XML = "jetty-http2c.xml";
  private static final String JETTY_HTTPS_XML = "jetty-https.xml";
  private static final String JETTY_SSL_XML = "jetty-ssl.xml";
  private static final String JETTY_SSL_CONTEXT_XML = "jetty-ssl-context.xml";
  private static final String JETTY_LOGGING = "jetty-logging.xml";

  Activator activatorUnderTest;

  @Mock
  private BundleContext mockedContext;
  @Mock
  private Bundle mockedBundle;
  @Mock
  private ServiceRegistration<ContextHandler> mockedContextHandlerServiceRegistration;
  @Mock
  private ServiceRegistration<Server> mockedJettyServerServiceRegistration;

  @Before
  public void setup() throws MalformedURLException {
    MockitoAnnotations.initMocks(this);

    when(mockedContext.getBundle()).thenReturn(mockedBundle);

    when(mockedBundle.getEntry(startsWith(JETTYHOME)))
        .then(answer -> new URL(TEST_URL_PREFIX + answer.getArgument(0)));

    activatorUnderTest = new Activator();
  }

  @Test
  public void itShouldRegisterServiceWithParameters() throws Exception {
    final Dictionary<String, Object> serverProps = new Hashtable<>();
    serverProps.put(OSGiServerConstants.MANAGED_JETTY_SERVER_NAME, JETTY_SERVER_NAME);

    serverProps.put(OSGiServerConstants.JETTY_BASE, TEST_URL_PREFIX + JETTYHOME);
    serverProps.put("jetty.sslContext.keyStorePath", ETC_KEYSTORE);
    serverProps.put("jetty.sslContext.keyStorePassword", OBFUSCTED_STORE_PASSWORD);
    serverProps.put("jetty.sslContext.keyManagerPassword", OBFUSCTED_STORE_PASSWORD);
    serverProps.put("jetty.sslContext.trustStorePath", ETC_KEYSTORE);

    serverProps.put(OSGiServerConstants.MANAGED_JETTY_XML_CONFIG_URLS,
        TEST_URL_PREFIX + JETTYHOME_ETC + "/" + JETTY_XML + "," + TEST_URL_PREFIX + JETTYHOME_ETC
            + "/" + JETTY_HTTP_XML + "," + TEST_URL_PREFIX + JETTYHOME_ETC + "/" + JETTY_SSL_XML
            + "," + TEST_URL_PREFIX + JETTYHOME_ETC + "/" + JETTY_SSL_CONTEXT_XML + ","
            + TEST_URL_PREFIX + JETTYHOME_ETC + "/" + JETTY_ALPN_XML + "," + TEST_URL_PREFIX
            + JETTYHOME_ETC + "/" + JETTY_HTTP2_XML + "," + TEST_URL_PREFIX + JETTYHOME_ETC + "/"
            + JETTY_HTTP2C_XML + "," + TEST_URL_PREFIX + JETTYHOME_ETC + "/" + JETTY_HTTPS_XML + ","
            + TEST_URL_PREFIX + JETTYHOME_ETC + "/" + JETTY_LOGGING);

    activatorUnderTest.start(mockedContext);

    verify(mockedContext).registerService(eq(Server.class), any(Server.class), eq(serverProps));

    final Dictionary<String, Object> contextHandlerProps = new Hashtable<>();
    contextHandlerProps.put(OSGiServerConstants.MANAGED_JETTY_SERVER_NAME, JETTY_SERVER_NAME);
    verify(mockedContext).registerService(eq(ContextHandler.class), any(ContextHandler.class),
        eq(contextHandlerProps));
  }

  @Test
  public void itShouldUnregisterServicesAndSetFieldsToNull() throws Exception {
    activatorUnderTest.contextHandlerServiceRegistration = mockedContextHandlerServiceRegistration;
    activatorUnderTest.jettyServerServiceRegistration = mockedJettyServerServiceRegistration;

    activatorUnderTest.stop(mockedContext);

    verify(mockedContextHandlerServiceRegistration).unregister();
    assertThat(activatorUnderTest.contextHandlerServiceRegistration, nullValue());
    verify(mockedJettyServerServiceRegistration).unregister();
    assertThat(activatorUnderTest.jettyServerServiceRegistration, nullValue());
  }

  @Test
  public void itShouldNotUnregisterNotInitializedServices() throws Exception {
    activatorUnderTest.contextHandlerServiceRegistration = null;
    activatorUnderTest.jettyServerServiceRegistration = null;

    activatorUnderTest.stop(mockedContext);

    assertThat(activatorUnderTest.contextHandlerServiceRegistration, nullValue());
    assertThat(activatorUnderTest.jettyServerServiceRegistration, nullValue());
  }

}
