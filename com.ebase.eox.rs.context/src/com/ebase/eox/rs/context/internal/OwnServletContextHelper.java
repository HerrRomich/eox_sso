package com.ebase.eox.rs.context.internal;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(service = ServletContextHelper.class,
    property = {HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=context_name",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH + "=/context"})
public class OwnServletContextHelper extends ServletContextHelper {

  private ServletContextHelper delegateContextHelper;

  @Activate
  protected void activate(ComponentContext context) {
    final Bundle usingBundle = context.getUsingBundle();
    delegateContextHelper = new ServletContextHelper(usingBundle) {};
  }

  public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    return delegateContextHelper.handleSecurity(request, response);
  }

  public URL getResource(String name) {
    return delegateContextHelper.getResource(name);
  }

  public String getMimeType(String name) {
    return delegateContextHelper.getMimeType(name);
  }

  public Set<String> getResourcePaths(String path) {
    return delegateContextHelper.getResourcePaths(path);
  }

  public String getRealPath(String path) {
    return delegateContextHelper.getRealPath(path);
  }

}
