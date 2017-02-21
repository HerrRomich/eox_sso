package com.ebase.eox.portal.internal;

import java.net.URL;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.http.context.ServletContextHelper;

public class DelegatedServletContextHalper extends ServletContextHelper {

  protected ServletContextHelper servletContextHelperDelegate;

  public DelegatedServletContextHalper() {
    super();
  }

  @Activate
  protected void activate(ComponentContext context) {
    final Bundle bundle = context.getUsingBundle();
    activateForBundle(bundle);
  }

  @Override
  public String getMimeType(String name) {
    return servletContextHelperDelegate.getMimeType(name);
  }

  @Override
  public String getRealPath(String path) {
    return servletContextHelperDelegate.getRealPath(path);
  }

  @Override
  public Set<String> getResourcePaths(String path) {
    return servletContextHelperDelegate.getResourcePaths(path);
  }

  @Override
  public URL getResource(String name) {
    return servletContextHelperDelegate.getResource(name);
  }

  void activateForBundle(final Bundle bundle) {
    servletContextHelperDelegate = new ServletContextHelper(bundle) {};
  }

}
