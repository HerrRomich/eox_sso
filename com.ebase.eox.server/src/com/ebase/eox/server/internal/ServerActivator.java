package com.ebase.eox.server.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class ServerActivator implements BundleActivator {

  static final String KEYSTORE_PATH = "/jettyhome/etc/keystore";
  static final String FELIX_KEYSTORE_PASSWORD_VALUE = "foreverNR1";
  static final String JETTY_SERVICE_PID = "org.apache.felix.http";
  static final String FELIX_JETTY_SESSION_COOKIE_SECURE =
      "org.apache.felix.https.jetty.session.cookie.secure";
  static final String FELIX_HTTP_ENABLE = "org.apache.felix.http.enable";
  static final String HTTP_PORT = "org.osgi.service.http.port";
  static final String FELIX_HTTPS_ENABLE = "org.apache.felix.https.enable";
  static final String HTTPS_PORT = "org.osgi.service.http.port.secure";
  static final String FELIX_KEYSTORE = "org.apache.felix.https.keystore";
  static final String FELIX_KEYSTORE_PASSWORD = "org.apache.felix.https.keystore.password";

  @Override
  public void start(BundleContext context) throws Exception {
    Dictionary<String, Object> props = getProperties(context);

    ServiceReference<ConfigurationAdmin> configurationAdminReference =
        context.getServiceReference(ConfigurationAdmin.class);

    if (configurationAdminReference != null) {
      ConfigurationAdmin confAdmin =
          (ConfigurationAdmin) context.getService(configurationAdminReference);

      Configuration configuration = confAdmin.getConfiguration(JETTY_SERVICE_PID, null);
      configuration.update(props);
    }

  }

  Dictionary<String, Object> getProperties(BundleContext context) {
    Dictionary<String, Object> props = new Hashtable<String, Object>();
    props.put(FELIX_JETTY_SESSION_COOKIE_SECURE, true);
    props.put(FELIX_HTTP_ENABLE, true);
    props.put(HTTP_PORT, 8080);
    props.put(FELIX_HTTPS_ENABLE, true);
    props.put(HTTPS_PORT, 8443);
    Bundle currentBundle = context.getBundle();
    String keyStorePath = currentBundle.getEntry(KEYSTORE_PATH).toString();
    props.put(FELIX_KEYSTORE, keyStorePath);
    props.put(FELIX_KEYSTORE_PASSWORD, FELIX_KEYSTORE_PASSWORD_VALUE);
    return props;
  }

  @Override
  public void stop(BundleContext context) throws Exception {}

}
