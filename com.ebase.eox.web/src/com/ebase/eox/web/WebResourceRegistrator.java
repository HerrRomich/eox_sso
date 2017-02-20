package com.ebase.eox.web;

import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.log.LogService;

import com.ebase.eox.web.internal.WebResourceDefinition;

@Component(service = WebResourceRegistrator.class)
public class WebResourceRegistrator {
  private static final String CONTEXT_SELECT_FORMAT = "(%1$s=%2$s)";

  private static final String PATH_TO_WEB_RESOURCE_LIST = "/OSGI-INF/web-resources.json";

  @Reference(cardinality = ReferenceCardinality.OPTIONAL)
  LogService logService;

  private ObjectMapper objectMapper = new ObjectMapper();

  public List<ServiceRegistration<WebResource>> registerResources(Bundle bundle)
      throws WebResourceRegistrationException {
    URL resourcesUrl = bundle.getEntry(PATH_TO_WEB_RESOURCE_LIST);
    if (resourcesUrl == null) {
      String message = String.format("Registration file: %1$s in bundle %2$s is not found",
          PATH_TO_WEB_RESOURCE_LIST, bundle.getSymbolicName());
      throw new WebResourceRegistrationException(message);
    }
    try {
      ArrayList<WebResourceDefinition> webResourceDefinitions = objectMapper.readValue(resourcesUrl,
          new TypeReference<ArrayList<WebResourceDefinition>>() {});

      Stream<WebResourceDefinition> definitionsStream = webResourceDefinitions.stream();
      return definitionsStream
          .map(resourceDefinition -> registerResource(bundle, resourceDefinition))
          .collect(Collectors.toList());
    } catch (Exception e) {
      String message =
          String.format("Cannot register web resources from registration file: %1$s in bundle %2$s",
              PATH_TO_WEB_RESOURCE_LIST, bundle.getSymbolicName());
      throw new WebResourceRegistrationException(message, e);
    }
  }

  private ServiceRegistration<WebResource> registerResource(Bundle bundle,
      WebResourceDefinition resourceDefinition) {
    BundleContext context = bundle.getBundleContext();
    Dictionary<String, ?> props = createServiceProperties(resourceDefinition);
    ServiceRegistration<WebResource> webResourceRegistration =
        context.registerService(WebResource.class, new WebResource() {}, props);
    logService.log(LogService.LOG_INFO, "Web resource registriert");
    return webResourceRegistration;

  }

  private Dictionary<String, ?> createServiceProperties(WebResourceDefinition resourceDefinition) {
    Dictionary<String, String> props = new Hashtable<>();
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX, resourceDefinition.path);
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PATTERN, resourceDefinition.pattern);
    String contextSelectPropertyValue = String.format(CONTEXT_SELECT_FORMAT, HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME,
        resourceDefinition.contextName);
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
        contextSelectPropertyValue);
    return props;
  }

  public void unregisterResources(List<ServiceRegistration<WebResource>> resources) {
    resources.forEach(resource -> resource.unregister());
  }

}
