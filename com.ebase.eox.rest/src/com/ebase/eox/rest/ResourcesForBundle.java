package com.ebase.eox.rest;

import java.util.*;
import java.util.stream.Collectors;

import org.eclipse.osgi.internal.messages.Msg;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.*;
import org.osgi.service.log.LogService;

public class ResourcesForBundle {

  private static final String REST_CLASSES_ATTRIBUTE = "classes";
  public static final String RESOURCES_MANIFEST = "X-Rest-Resources";
  private Long bundleId;
  private Map<String, List<Class<?>>> resources;
  private LogService logService;

  private ResourcesForBundle(Bundle bundle) throws BundleException {
    bundleId = bundle.getBundleId();
    logService = getLogService(bundle);
    fillResources(bundle);
  }

  private LogService getLogService(Bundle bundle) {
    BundleContext bundleContext = bundle.getBundleContext();
    ServiceReference<LogService> logServiceReference = bundleContext.getServiceReference(LogService.class);
    return bundleContext.getService(logServiceReference);
  }

  private void fillResources(Bundle bundle) throws BundleException {
    logService.log(LogService.LOG_INFO, String.format("Start searching REST resource classeses in bundle %1$s", bundle));
    Dictionary<String, String> bundleHeaders = bundle.getHeaders();
    String manifestValue = bundleHeaders.get(RESOURCES_MANIFEST);
    ManifestElement[] manifestElements =
        ManifestElement.parseHeader(RESOURCES_MANIFEST, manifestValue);

    if (manifestElements == null)
      resources = Collections.emptyMap();
    else
      fillResourcesFromManifestElements(bundle, manifestValue, manifestElements);
  }

  private void fillResourcesFromManifestElements(Bundle bundle, String manifestValue, ManifestElement[] manifestElements)
      throws BundleException {
    try {
      resources =
          Arrays.stream(manifestElements).collect(Collectors.toMap(ManifestElement::getValue,
              element -> loadBundleClassesFromManifestElement(bundle, element)));
    } catch (NullPointerException e) {
      throw new BundleException(
          NLS.bind(Msg.MANIFEST_INVALID_HEADER_EXCEPTION, RESOURCES_MANIFEST, manifestValue),
          BundleException.MANIFEST_ERROR);
    }
  }

  private List<Class<?>> loadBundleClassesFromManifestElement(Bundle bundle,
      ManifestElement element) {
    String classNames = element.getAttribute(REST_CLASSES_ATTRIBUTE);
    return Arrays.stream(ManifestElement.getArrayFromList(classNames)).map(className -> {
      return tryToLoadBundleClassOrReturnNull(bundle, className);
    }).filter(classz -> classz != null).collect(Collectors.toList());
  }

  private Class<?> tryToLoadBundleClassOrReturnNull(Bundle bundle, String className) {
    try {
      return bundle.loadClass(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  public static ResourcesForBundle of(Bundle bundle) throws BundleException {
    return new ResourcesForBundle(bundle);
  }

  public Long getBundleId() {
    return bundleId;
  }

  public Set<String> getResourceContexts() {
    return resources.keySet();
  }

  public List<Class<?>> getResources(String context) {
    return resources.get(context);
  }

}
