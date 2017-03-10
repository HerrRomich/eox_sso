package com.ebase.eox.infrastructure.services.cxf.internal;

import javax.servlet.Servlet;
import javax.ws.rs.core.Application;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import com.ebase.eox.infrastructure.services.RestService;

public class Activator implements BundleActivator {

  private ServiceTracker<RestService, ServiceRegistration<Servlet>> restServiceTracker;

  @Override
  public void start(BundleContext context) throws Exception {
    restServiceTracker = createAndInitializeRestServiceTracker(context);
    restServiceTracker.open(true);
  }

  private ServiceTracker<RestService, ServiceRegistration<Servlet>> createAndInitializeRestServiceTracker(
      BundleContext context) {
    ServiceBuilder serviceBuilder = new ServiceBuilder();
    RestServiceTrackerCustomizer restServiceTrackerCustomizer =
        new RestServiceTrackerCustomizer(context, serviceBuilder);
    return new ServiceTracker<>(context, RestService.class, restServiceTrackerCustomizer);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    if (restServiceTracker != null) {
      restServiceTracker.close();
      restServiceTracker = null;
    }
  }

}
