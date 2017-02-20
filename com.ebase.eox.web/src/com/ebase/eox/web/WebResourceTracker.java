package com.ebase.eox.web;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

@Component(immediate = true, service = WebResourceTracker.class)
public class WebResourceTracker
    implements BundleTrackerCustomizer<List<ServiceRegistration<WebResource>>> {

  private static final int BUNDLE_STETES_TO_TRACK = Bundle.ACTIVE;
  BundleTracker<List<ServiceRegistration<WebResource>>> bundleTracker;

  @Reference(cardinality = ReferenceCardinality.OPTIONAL)
  volatile LogService logService;

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  BundleTrackerBuilder bundleTrackerBuilder;

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  WebResourceRegistrator webResourceRegistrator;


  @Activate
  protected void activate(ComponentContext context) throws Exception {
    BundleContext bundleContext = context.getBundleContext();
    bundleTracker =
        bundleTrackerBuilder.buildBundleTracker(bundleContext, BUNDLE_STETES_TO_TRACK, this);
    bundleTracker.open();
  }

  @Deactivate
  protected void deactivate() throws Exception {
    if (bundleTracker == null)
      return;
    bundleTracker.close();
    bundleTracker = null;
  }

  @Override
  public List<ServiceRegistration<WebResource>> addingBundle(Bundle bundle, BundleEvent event) {
    try {
      return webResourceRegistrator.registerResources(bundle);
    } catch (WebResourceRegistrationException e) {
      log(LogService.LOG_WARNING, e.getMessage(), e);
      return null;
    }
  }

  private void log(int level, String message, Throwable exception) {
    if (logService != null)
      logService.log(level, message, exception);
  }

  @Override
  public void modifiedBundle(Bundle bundle, BundleEvent event,
      List<ServiceRegistration<WebResource>> resources) {
    ;
  }

  @Override
  public void removedBundle(Bundle bundle, BundleEvent event,
      List<ServiceRegistration<WebResource>> resources) {
    webResourceRegistrator.unregisterResources(resources);
  }

}

