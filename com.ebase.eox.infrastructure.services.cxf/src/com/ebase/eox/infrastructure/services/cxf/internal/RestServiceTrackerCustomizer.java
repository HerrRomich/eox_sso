package com.ebase.eox.infrastructure.services.cxf.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.ebase.eox.infrastructure.services.RestService;

public class RestServiceTrackerCustomizer
    implements ServiceTrackerCustomizer<RestService, ServiceRegistration<Servlet>> {

  private static final String BASIC_CONTEXT = "/";
  private static final String SERVICESCONTENT = "com.ebase.eox.servicescontent";
  private static final String CONTEXT_SELECT_FORMAT = "(%1$s=%2$s)";

  BundleContext context;
  ServiceBuilder serviceBuilder;

  public RestServiceTrackerCustomizer(BundleContext context, ServiceBuilder serviceBuilder) {
    super();
    this.context = context;
    this.serviceBuilder = serviceBuilder;
  }

  @Override
  public ServiceRegistration<Servlet> addingService(ServiceReference<RestService> reference) {
    RestService restServices = context.getService(reference);
    Servlet servlet = serviceBuilder.buildJaxRsServlet(restServices);
    Dictionary<String, ?> props = getRestServletProperties();
    return context.registerService(Servlet.class, servlet, props);
  }

  private Dictionary<String, ?> getRestServletProperties() {
    Dictionary<String, String> props = new Hashtable<>();
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, BASIC_CONTEXT);
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED, "true");
    String contextSelectPropertyValue = String.format(CONTEXT_SELECT_FORMAT,
        HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME, SERVICESCONTENT);
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT, contextSelectPropertyValue);
    return props;
  }

  @Override
  public void modifiedService(ServiceReference<RestService> reference,
      ServiceRegistration<Servlet> service) {
    removedService(reference, service);
    addingService(reference);
  }

  @Override
  public void removedService(ServiceReference<RestService> reference,
      ServiceRegistration<Servlet> service) {
    service.unregister();
  }

}
