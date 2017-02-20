package com.ebase.eox.rest.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		configCxfServletExplorer(context);
	}

	private void configCxfServletExplorer(BundleContext context) throws IOException {
		ServiceReference<ConfigurationAdmin> configAdminServiceReference = context
				.getServiceReference(ConfigurationAdmin.class);
		if (configAdminServiceReference == null)
			return;
		configCxfServletExplorerByCmServiceReference(context, configAdminServiceReference);
	}

	private void configCxfServletExplorerByCmServiceReference(BundleContext context,
			ServiceReference<ConfigurationAdmin> configAdminServiceReference) throws IOException {
		ConfigurationAdmin configAdmin = context.getService(configAdminServiceReference);
		if (configAdmin == null)
			return;
		try {
			Configuration servletExplorerConfiguration = configAdmin.getConfiguration("org.apache.cxf.osgi", null);

			configCxfServletExplorerByCmConfiguration(servletExplorerConfiguration); 
		} finally {
			context.ungetService(configAdminServiceReference);
		}
	}

	private void configCxfServletExplorerByCmConfiguration(Configuration servletExplorerConfiguration) throws IOException {
		Dictionary<String, String> props = new Hashtable<>();
		props.put("org.apache.cxf.servlet.context", "/rest");
		servletExplorerConfiguration.update(props);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
