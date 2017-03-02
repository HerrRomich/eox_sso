package com.ebase.eox.application.web.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.ebase.eox.application.web.WebApplication;
import com.ebase.eox.application.web.WebApplications;

@Component(service = WebApplications.class)
public class WebApplicationsImpl implements WebApplications {

  private List<WebApplication> webApplications = new ArrayList<>();

  @Reference(cardinality=ReferenceCardinality.MULTIPLE)
  protected void setWebApplication(WebApplication webApplication) {
    synchronized (webApplications) {
      webApplications.add(webApplication);
    }
  }
  
  protected void unsetWebApplication(WebApplication webApplication) {
    synchronized (webApplications) {
      webApplications.remove(webApplication);
    }
  }

  @Override
  public List<WebApplication> getWebApplications() {
    return Collections.unmodifiableList(webApplications);
  }

  @Override
  public String getWebApplicationUserGroupByPathOrNull(String path) {
    Stream<WebApplication> foundWebApplications = findWebApplication(path);
    return foundWebApplications.map(webApplication -> webApplication.getApplicationUserGroup())
        .findAny().orElse(null);

  }

  @Override
  public boolean isValidWebApplicationPath(String path) {
    Stream<WebApplication> foundWebApplications = findWebApplication(path);
    return foundWebApplications.findAny().isPresent();
  }

  private Stream<WebApplication> findWebApplication(String path) {
    return webApplications.stream()
        .filter(webApplication -> path.startsWith(webApplication.getApplicationName()));
  }

}
