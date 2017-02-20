package com.ebase.eox.test.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("")
@Produces("application/json")
public interface HealthService {

	@GET
	Health getHealth();

}
