package com.ebase.eox.homepage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.osgi.service.component.annotations.Component;

import com.ebase.eox.portal.EoxPortalConstants;

@Component(immediate = true, property = EoxPortalConstants.EOX_JAX_RS_SERVICE_PROPERTY + "="
    + EoxPortalConstants.EOX_JAX_RS_SERVICE, service = Object.class)
@Path("/test")
public class TestRestService {

  @GET
  @Produces("application/json")
  public TestData getTest() {
    return new TestData();
  }

}
