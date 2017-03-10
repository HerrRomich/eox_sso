package com.ebase.eox.application.web.internal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ServiceScope;

import com.ebase.eox.application.web.AngularRoute;
import com.ebase.eox.application.web.WebApplications;
import com.ebase.eox.application.web.WebApplicationsService;
import com.ebase.eox.infrastructure.services.RestService;

@Component(service = RestService.class, scope = ServiceScope.SINGLETON)
public class WebApplicationServiceImpl implements WebApplicationsService {

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  WebApplications webApplications;

  @Override
  public List<AngularRoute> getRoutes() {
    Stream<AngularRoute> routeStream =
        webApplications.getWebApplications().stream().map(webApplication -> {
          AngularRoute route = new AngularRoute();
          route.path = webApplication.getApplicationName();
          route.loadChildren = webApplication.getChildrenModule();
          return route;
        });
    return routeStream.collect(Collectors.toList());
  }

}
