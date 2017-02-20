package com.ebase.eox.test.rest.impl.internal;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.PerRequestResourceProvider;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	private Server server;
	private Server server1;

	@Override
	public void start(BundleContext context) throws Exception {
		JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
		
		
		JAXRSServerFactoryBean rsServiceFactoryBean = new JAXRSServerFactoryBean();
		rsServiceFactoryBean.setResourceProvider(new PerRequestResourceProvider(HealthServiceImpl.class));
		rsServiceFactoryBean.setAddress("/health");
		rsServiceFactoryBean.setProvider(jsonProvider);
		server = rsServiceFactoryBean.create();

		rsServiceFactoryBean = new JAXRSServerFactoryBean();
		rsServiceFactoryBean.setResourceProvider(new PerRequestResourceProvider(HealthServiceImpl.class));
		rsServiceFactoryBean.setAddress("/health1");
		rsServiceFactoryBean.setProvider(jsonProvider);
		server1 = rsServiceFactoryBean.create();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		server.destroy();
		server1.destroy();
	}

}
