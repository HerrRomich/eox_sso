package com.ebase.eox.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

public class WebResourceRegistratorTest {

  private static final String WEB_RESOURCE_PROPERTIES_FILE_NAME_TEMPLATE =
      "web-resource%1$d.properties";
  private static final String WEB_RESOURCES_JSON = "web-resources.json";
  private static final String EMPTY_STRING = "";
  private static final String PATH_TO_WEB_RESOURCE_REGISTRATION_FILE =
      "/OSGI-INF/" + WEB_RESOURCES_JSON;
  @InjectMocks
  private WebResourceRegistrator resourceRegistratorUnderTest = new WebResourceRegistrator();
  @Mock
  private Bundle mockedBundle;
  @Mock
  private URLConnection mockedUrlConnection;
  @Mock
  private BundleContext mockedBundleContext;
  @Mock
  private LogService mockedLogService;
  @Mock
  private ServiceRegistration<WebResource> mockedWebResourceRegistration;

  private URLStreamHandler stubStreamHandler;
  private URL webResourceRegistrationFile;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(mockedBundle.getBundleContext()).thenReturn(mockedBundleContext);

    stubStreamHandler = new URLStreamHandler() {

      @Override
      protected URLConnection openConnection(URL u) throws IOException {
        return mockedUrlConnection;
      }
    };
    webResourceRegistrationFile = new URL("foo", "bar", 99, "/foobar", stubStreamHandler);
  }

  @Test(expected = WebResourceRegistrationException.class)
  public void shoudThrowWebResourceRegistrationExceptionOnRegisterResourcesWhenNoResourceList()
      throws WebResourceRegistrationException {
    when(mockedBundle.getEntry(eq(PATH_TO_WEB_RESOURCE_REGISTRATION_FILE))).thenReturn(null);

    resourceRegistratorUnderTest.registerResources(mockedBundle);

  }

  @Test(expected = WebResourceRegistrationException.class)
  public void shoudThrowWebResourceRegistrationExceptionOnRegisterResourcesWhenEmptyResourceList()
      throws IOException, WebResourceRegistrationException {
    byte[] emptyBytes = EMPTY_STRING.getBytes();
    ByteArrayInputStream emptyByteArrayStream = new ByteArrayInputStream(emptyBytes);
    when(mockedUrlConnection.getInputStream()).thenReturn(emptyByteArrayStream);
    when(mockedBundle.getEntry(eq(PATH_TO_WEB_RESOURCE_REGISTRATION_FILE)))
        .thenReturn(webResourceRegistrationFile);

    resourceRegistratorUnderTest.registerResources(mockedBundle);
  }

  @Test
  public void shoudRegisterWebResourcesOnRegisterResources()
      throws IOException, WebResourceRegistrationException {
    when(mockedUrlConnection.getInputStream())
        .thenReturn(this.getClass().getResourceAsStream(WEB_RESOURCES_JSON));
    when(mockedBundle.getEntry(eq(PATH_TO_WEB_RESOURCE_REGISTRATION_FILE)))
        .thenReturn(webResourceRegistrationFile);

    List<ServiceRegistration<WebResource>> actualResources =
        resourceRegistratorUnderTest.registerResources(mockedBundle);

    List<Dictionary<String, String>> expectedProps = fillExpectedProperties();

    int actualResourcesCount = actualResources.size();
    assertThat(actualResourcesCount, equalTo(3));
    verify(mockedBundleContext).registerService(eq(WebResource.class), any(WebResource.class),
        eq(expectedProps.get(0)));
    verify(mockedBundleContext).registerService(eq(WebResource.class), any(WebResource.class),
        eq(expectedProps.get(1)));
    verify(mockedBundleContext).registerService(eq(WebResource.class), any(WebResource.class),
        eq(expectedProps.get(2)));
  }

  private List<Dictionary<String, String>> fillExpectedProperties() {
    List<Dictionary<String, String>> expectedProps =
        IntStream.rangeClosed(1, 3).mapToObj(position -> {
          return loadWebResourceProperty(position);
        }).collect(Collectors.toList());
    return expectedProps;
  }

  private Dictionary<String, String> loadWebResourceProperty(int position) {
    Properties props = new Properties();
    String fileName = String.format(WEB_RESOURCE_PROPERTIES_FILE_NAME_TEMPLATE, position);
    InputStream fileStream = this.getClass().getResourceAsStream(fileName);
    try {
      props.load(fileStream);
      Map<String, String> propsMap = props.stringPropertyNames().stream()
          .collect(Collectors.toMap(propName -> propName, propName -> props.getProperty(propName)));
      return new Hashtable<>(propsMap);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void shoudUnregisterWebResourcesOnUnregisterResources() throws IOException {
    List<ServiceRegistration<WebResource>> resources =
        Collections.nCopies(3, mockedWebResourceRegistration);

    resourceRegistratorUnderTest.unregisterResources(resources);

    verify(mockedWebResourceRegistration, times(3)).unregister();
  }

}
