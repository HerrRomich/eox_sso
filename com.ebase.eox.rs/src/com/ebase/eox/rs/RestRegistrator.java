package com.ebase.eox.rs;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

@Component(immediate = true, scope = ServiceScope.SINGLETON)
public class RestRegistrator
    implements ServiceTrackerCustomizer<RestApplication, ServiceRegistration<Servlet>> {

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  ContextGenerator contextGenerator;

  ServiceTracker<RestApplication, ServiceRegistration<Servlet>> serviceTracker;

  @Activate
  protected void activate(final ComponentContext componentContext) {
    final BundleContext bundleContext = componentContext.getBundleContext();
    serviceTracker = createServiceTracker(bundleContext);
    serviceTracker.open();
  }

  ServiceTracker<RestApplication, ServiceRegistration<Servlet>> createServiceTracker(
      final BundleContext bundleContext) {
    return new ServiceTracker<>(bundleContext, RestApplication.class, this);
  }

  Servlet createRestServlet(final RestApplication application) {
    return new RestServlet(application);
  }

  @Override
  public ServiceRegistration<Servlet> addingService(
      final ServiceReference<RestApplication> reference) {
    final BundleContext bundleContext = getBundleContext(reference);
    final RestApplication application = bundleContext.getService(reference);
    final Servlet service = createRestServlet(application);
    final Dictionary<String, Object> props = new Hashtable<>();

    final String restContext = contextGenerator.generateContext(application);
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, restContext);
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED, true);
    ServiceRegistration<Servlet> serviceRegistration;
    serviceRegistration = bundleContext.registerService(Servlet.class, service, props);
    return serviceRegistration;
  }

  @Override
  public void modifiedService(final ServiceReference<RestApplication> reference,
      final ServiceRegistration<Servlet> service) {
    ;
  }

  @Override
  public void removedService(final ServiceReference<RestApplication> reference,
      final ServiceRegistration<Servlet> service) {
    service.unregister();
  }

  private BundleContext getBundleContext(final ServiceReference<RestApplication> reference) {
    final Bundle bundle = reference.getBundle();
    final BundleContext bundleContext = bundle.getBundleContext();
    return bundleContext;
  }

  @Deactivate
  protected void deactivate() {
    serviceTracker.close();
  }

}
