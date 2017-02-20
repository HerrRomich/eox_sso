package com.ebase.eox.rs;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;

public class RestServlet extends CXFNonSpringJaxrsServlet {

  public RestServlet(RestApplication application) {
    super(application);
  }

}
