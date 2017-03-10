package com.ebase.eox.application.web;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.ebase.eox.infrastructure.services.RestService;

@Path("/applications")
public interface WebApplicationsService extends RestService {

  @GET
  @Path("routes")
  @Produces("application/json")
  List<AngularRoute> getRoutes();

}
