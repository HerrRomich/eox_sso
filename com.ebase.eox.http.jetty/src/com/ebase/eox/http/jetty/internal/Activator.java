package com.ebase.eox.http.jetty.internal;

import java.net.URL;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.equinox.http.servlet.HttpServiceServlet;
import org.eclipse.jetty.osgi.boot.OSGiServerConstants;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

  private static final String JETTY_HOME = "/jettyhome";

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

  private static final String[] JETTY_CONFIGS =
      {Activator.JETTY_XML, Activator.JETTY_HTTP_XML, Activator.JETTY_SSL_XML,
          Activator.JETTY_SSL_CONTEXT_XML, Activator.JETTY_ALPN_XML, Activator.JETTY_HTTP2_XML,
          Activator.JETTY_HTTP2C_XML, Activator.JETTY_HTTPS_XML, Activator.JETTY_LOGGING};

  private static final String CONFIG_DIR = Activator.JETTY_HOME + "/" + "etc";
  
  ServiceRegistration<ContextHandler> contextHandlerServiceRegistration;
  ServiceRegistration<Server> jettyServerServiceRegistration;

  @Override
  public void start(final BundleContext context) throws Exception {
    registerServerService(context);

    registerContextHandlerService(context);
  }

  private void registerServerService(final BundleContext context) {
    final Server server = new Server();
    final Dictionary<String, Object> serverProps = new Hashtable<>();
    serverProps.put(OSGiServerConstants.MANAGED_JETTY_SERVER_NAME, Activator.JETTY_SERVER_NAME);

    final Bundle currentBundle = context.getBundle();
    final URL homeUrl = currentBundle.getEntry(Activator.JETTY_HOME);
    serverProps.put(OSGiServerConstants.JETTY_BASE, homeUrl.toString());
    serverProps.put("jetty.sslContext.keyStorePath", "etc/keystore");
    serverProps.put("jetty.sslContext.keyStorePassword",
        "OBF:1j1w1rbw1r4z1vn61wgg1wfi1vnw1r2z1raa1iyy");
    serverProps.put("jetty.sslContext.keyManagerPassword",
        "OBF:1j1w1rbw1r4z1vn61wgg1wfi1vnw1r2z1raa1iyy");
    serverProps.put("jetty.sslContext.trustStorePath", "etc/keystore");

    final String xmlConfigUrls = getJettyXmlConfigUrls(context);
    serverProps.put(OSGiServerConstants.MANAGED_JETTY_XML_CONFIG_URLS, xmlConfigUrls);

    jettyServerServiceRegistration = context.registerService(Server.class, server, serverProps);
  }

  private void registerContextHandlerService(final BundleContext context) {
    final HttpServiceServlet servlet = new HttpServiceServlet();
    final ServletHolder holder = new ServletHolder(servlet);
    holder.setAsyncSupported(true);
    final ServletContextHandler httpContext = new ServletContextHandler();

    httpContext.addServlet(holder, "/*");
    final Dictionary<String, String> servletProps = new Hashtable<>();
    servletProps.put(OSGiServerConstants.MANAGED_JETTY_SERVER_NAME, Activator.JETTY_SERVER_NAME);

    contextHandlerServiceRegistration =
        context.registerService(ContextHandler.class, httpContext, servletProps);
  }

  private String getJettyXmlConfigUrls(final BundleContext context) {
    final Stream<String> jettyConfigsArrayStream = Arrays.asList(Activator.JETTY_CONFIGS).stream();
    Stream<String> xmlConfigUrlsStream =
        jettyConfigsArrayStream.map(configName -> getJettyXmlConfigUrl(context, configName));
    return xmlConfigUrlsStream.collect(Collectors.joining(","));
  }

  private String getJettyXmlConfigUrl(final BundleContext context, final String configName) {
    final String configPath = String.format("%1$s/%2$s", Activator.CONFIG_DIR, configName);
    final Bundle currentBundle = context.getBundle();
    final URL configUrl = currentBundle.getEntry(configPath);
    return configUrl.toString();
  }

  @Override
  public void stop(final BundleContext context) throws Exception {
    if (contextHandlerServiceRegistration != null) {
      contextHandlerServiceRegistration.unregister();
      contextHandlerServiceRegistration = null;
    }
    if (jettyServerServiceRegistration != null) {
      jettyServerServiceRegistration.unregister();
      jettyServerServiceRegistration = null;
    }
  }
}
