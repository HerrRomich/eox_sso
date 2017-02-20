package com.ebase.eox.ws;

import org.apache.cxf.Bus;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

public class Test {

  private void test() {
    final CXFNonSpringServlet servlet = new CXFNonSpringServlet();

    final Bus bus = servlet.getBus();
  }

}
