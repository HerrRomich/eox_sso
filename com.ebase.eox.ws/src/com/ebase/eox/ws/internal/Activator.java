package com.ebase.eox.ws.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.wsdl.extensions.ElementExtensible;
import javax.xml.ws.Endpoint;

import org.apache.cxf.jaxws.spi.ProviderImpl;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

public class Activator implements BundleActivator {

  private ServiceRegistration<Servlet> servletRegistration;
  private Endpoint endpoint;

  @Override
  public void start(BundleContext context) throws Exception {
    String providerName = ProviderImpl.JAXWS_PROVIDER;
    Class<ElementExtensible> cl = ElementExtensible.class;
    cl.getMethods();

    try {
      CXFNonSpringServlet cxfServlet = new CXFNonSpringServlet();
      Dictionary<String, Object> props = new Hashtable<>();
      props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, "/ws/*");
      props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED, true);
      servletRegistration = context.registerService(Servlet.class, cxfServlet, props);

      endpoint = Endpoint.publish("/test", new TestWebServiceImpl());
    } catch (Throwable ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    if (endpoint != null) {
      endpoint.stop();
    }
    if (servletRegistration != null) {
      servletRegistration.unregister();
      servletRegistration = null;
    }
  }

}
