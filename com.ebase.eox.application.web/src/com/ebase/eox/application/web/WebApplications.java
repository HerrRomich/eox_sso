package com.ebase.eox.application.web;

import java.util.List;

public interface WebApplications {

  List<WebApplication> getWebApplications();

  String getWebApplicationUserGroupByPathOrNull(String path);

  boolean isValidWebApplicationPath(String path);

}
