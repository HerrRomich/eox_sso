package com.ebase.eox.web.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

  private LogTracker logTracker;
  private WebResourceTracker webResourceTracker;

  @Override
  public void start(BundleContext context) throws Exception {
    logTracker = new LogTracker(context);
    logTracker.open();
    webResourceTracker = new WebResourceTracker(context, logTracker);
    webResourceTracker.open();
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    if (webResourceTracker != null) {
      webResourceTracker.close();
      webResourceTracker = null;
    }
    if (logTracker != null) {
      logTracker.close();
      logTracker = null;
    }
  }

}
