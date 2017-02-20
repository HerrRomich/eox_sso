package com.ebase.eox.web;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

@Component(immediate = true, service = BundleTrackerBuilder.class)
public class BundleTrackerBuilder {
  public <T> BundleTracker<T> buildBundleTracker(BundleContext context, int stateMask,
      BundleTrackerCustomizer<T> customizer) {
    BundleTracker<T> result = new BundleTracker<>(context, stateMask, customizer);
    return result;
  }
}
