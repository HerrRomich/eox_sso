package com.ebase.eox.infrastructure.services.cxf.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Servlet;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import com.ebase.eox.infrastructure.services.RestService;

public class ServiceBuilder {

  public Servlet buildJaxRsServlet(RestService restService) {
    Object jaxRsProvider = new JacksonJaxbJsonProvider();
    Set<Object> singletones = new HashSet<>(Arrays.asList(restService, jaxRsProvider ));
    return new CXFNonSpringJaxrsServlet(singletones);
  }

}
