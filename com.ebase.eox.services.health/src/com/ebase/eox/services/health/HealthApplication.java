package com.ebase.eox.services.health;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Path;

import org.osgi.service.component.annotations.Component;

import com.ebase.eox.rs.RestApplication;
import com.ebase.eox.services.health.internal.HealthService;

@Component(immediate = true, service = RestApplication.class)
@Path("/grtest")
public class HealthApplication extends RestApplication {

  @Override
  public Set<Class<?>> getClasses() {
    return new HashSet<Class<?>>(Arrays.asList(HealthService.class));
  }

}
