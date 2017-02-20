package com.ebase.eox.services.health.internal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Path("/health")
public class HealthService {

  @GET
  @Path("check")
  @Produces("application/json")
  public String check(@Context HttpServletRequest request) {
    request.getSession().getId();
    return "OK1 _22 33_";
  }

}
