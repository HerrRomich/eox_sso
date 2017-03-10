package com.ebase.eox.application.web.diva.internal;

import org.osgi.service.component.annotations.Component;

import com.ebase.eox.application.web.WebApplication;

@Component(service = WebApplication.class, immediate = true)
public class DivaWebApplication implements WebApplication {

  private static final String ADMIN_USER_GROUP = "admin";
  private static final String DIVA_APPLICATION_NAME = "diva";
  private static final String DIVA_MODULE_PATH = "/app/diva/diva.module#DivaModule";

  @Override
  public String getApplicationName() {
    return DIVA_APPLICATION_NAME;
  }

  @Override
  public String getApplicationUserGroup() {
    return ADMIN_USER_GROUP;
  }

  @Override
  public String getChildrenModule() {
    return DIVA_MODULE_PATH;
  }

}
