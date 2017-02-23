package com.ebase.eox.infrastructure.utils.log;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class LogServiceWrapper implements LogService {

  private volatile LogService logService;
  
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
    LogService logService = getLogService();
    if (logService != null) {
      logService.log(sr, level, message, exception);
    }
  }

  public LogService getLogService() {
    return logService;
  }

  public void setLogService(LogService logService) {
    this.logService = logService;
  }

}
