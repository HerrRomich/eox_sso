package com.ebase.eox.test.rest.impl.internal;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import com.ebase.eox.test.rest.Health;
import com.ebase.eox.test.rest.HealthService;

/*@Component(immediate = true, name = "Health", property = { "service.exported.interfaces=*",
		"service.exported.configs=org.apache.cxf.rs",
		"org.apache.cxf.rs.address=/health" }, scope=ServiceScope.SINGLETON)*/
public class HealthServiceImpl implements HealthService {
	
	@Context
	ServletContext context;
	
	@Override
	public Health getHealth() {
		Health health = new Health();
		health.setStatus(context.getServerInfo());
		return health;
	}

}
