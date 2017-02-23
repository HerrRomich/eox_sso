package com.ebase.eox.infrastructure.utils.log;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class LogTracker extends ServiceTracker<LogService, LogService> implements LogService {

  public LogTracker(BundleContext bundleContext) {
    super(bundleContext, LogService.class, null);
  }

  @Override
  public void log(int level, String message) {
    log(null, level, message, null);
  }

  @Override
  public void log(int level, String message, Throwable exception) {
    log(null, level, message, exception);
  }

  @Override
  public void log(ServiceReference sr, int level, String message) {
    log(sr, level, message, null);
  }

  @Override
  public void log(ServiceReference sr, int level, String message, Throwable exception) {
    LogService logService = getService();
    if (logService != null) {
      logService.log(sr, level, message, exception);
    }
  }

}
