package com.ebase.eox.application.web;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.ebase.eox.infrastructure.services.RestService;

@Path("web-applications")
public interface WebApplicationsService extends RestService {
  
  @GET
  @Produces("application/json")
  List<AngularRoute> getRoutes();

}
