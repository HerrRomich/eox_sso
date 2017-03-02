package com.ebase.eox.infrastructure.web.internal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;

import com.ebase.eox.infrastructure.web.WebResource;

public class WebResourceTrackerTest {

  private static final int BUNDLE_STETES_TO_TRACK = Bundle.ACTIVE;
  private static final String WEB_RESOURCE_PROPERTIES_FILE_NAME_TEMPLATE =
      "web-resource%1$d.properties";
  private static final String WEB_RESOURCES_JSON = "web-resources.json";
  private static final String EMPTY_STRING = "";
  private static final String PATH_TO_WEB_RESOURCE_REGISTRATION_FILE =
      "/OSGI-INF/" + WEB_RESOURCES_JSON;

  @Mock
  private BundleContext mockedContext;
  @Mock
  private BundleTracker<List<ServiceRegistration<WebResource>>> mockedBundleTracker;
  @Mock
  private Bundle mockedBundle;
  @Mock
  private LogService mockedLogService;

  private WebResourceTracker webResourceTrackerUnderTest;

  @Mock
  private URLConnection mockedUrlConnection;
  @Mock
  private ServiceRegistration<WebResource> mockedWebResourceRegistration;

  private URLStreamHandler stubStreamHandler;
  private URL webResourceRegistrationFile;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    webResourceTrackerUnderTest = new WebResourceTracker(mockedContext, mockedLogService);

    when(mockedBundle.getBundleContext()).thenReturn(mockedContext);

    stubStreamHandler = new URLStreamHandler() {

      @Override
      protected URLConnection openConnection(URL u) throws IOException {
        return mockedUrlConnection;
      }
    };
    webResourceRegistrationFile = new URL("foo", "bar", 99, "/foobar", stubStreamHandler);
  }

  @Test
  public void shoudReturnNullOnRegisterResourcesWhenNoResourceList() {
    when(mockedBundle.getEntry(eq(PATH_TO_WEB_RESOURCE_REGISTRATION_FILE))).thenReturn(null);
    BundleEvent bundleEvent = new BundleEvent(BundleEvent.STARTED, mockedBundle);

    List<ServiceRegistration<WebResource>> actualResources =
        webResourceTrackerUnderTest.addingBundle(mockedBundle, bundleEvent);

    assertThat(actualResources, nullValue());
  }

  @Test
  public void shoudReturnNullOnRegisterResourcesWhenEmptyResourceList() throws IOException {
    byte[] emptyBytes = EMPTY_STRING.getBytes();
    ByteArrayInputStream emptyByteArrayStream = new ByteArrayInputStream(emptyBytes);
    when(mockedUrlConnection.getInputStream()).thenReturn(emptyByteArrayStream);
    when(mockedBundle.getEntry(eq(PATH_TO_WEB_RESOURCE_REGISTRATION_FILE)))
        .thenReturn(webResourceRegistrationFile);
    BundleEvent bundleEvent = new BundleEvent(BundleEvent.STARTED, mockedBundle);

    List<ServiceRegistration<WebResource>> actualResources =
        webResourceTrackerUnderTest.addingBundle(mockedBundle, bundleEvent);

    assertThat(actualResources, nullValue());
  }

  @Test
  public void shoudRegisterWebResourcesOnRegisterResources()
      throws IOException, WebResourceRegistrationException {
    when(mockedUrlConnection.getInputStream())
        .thenReturn(this.getClass().getResourceAsStream(WEB_RESOURCES_JSON));
    when(mockedBundle.getEntry(eq(PATH_TO_WEB_RESOURCE_REGISTRATION_FILE)))
        .thenReturn(webResourceRegistrationFile);
    BundleEvent bundleEvent = new BundleEvent(BundleEvent.STARTED, mockedBundle);

    List<ServiceRegistration<WebResource>> actualResources =
        webResourceTrackerUnderTest.addingBundle(mockedBundle, bundleEvent);

    List<Dictionary<String, String>> expectedProps = fillExpectedProperties();
    int actualResourcesCount = actualResources.size();
    assertThat(actualResourcesCount, equalTo(3));
    verify(mockedContext).registerService(eq(WebResource.class), any(WebResource.class),
        eq(expectedProps.get(0)));
    verify(mockedContext).registerService(eq(WebResource.class), any(WebResource.class),
        eq(expectedProps.get(1)));
    verify(mockedContext).registerService(eq(WebResource.class), any(WebResource.class),
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
  public void shouldReturnNullOnAddingBundleIfRegistrationFailed() throws IOException {
    when(mockedUrlConnection.getInputStream()).thenThrow(Exception.class);

    BundleEvent bundleEvent = new BundleEvent(BundleEvent.STARTED, mockedBundle);
    List<ServiceRegistration<WebResource>> actualRegistrationList =
        webResourceTrackerUnderTest.addingBundle(mockedBundle, bundleEvent);

    assertThat(actualRegistrationList, nullValue());
  }

  @Test
  public void shoudUnregisterWebResourcesOnRemovingBundle() {
    BundleEvent bundleEvent = new BundleEvent(BundleEvent.STARTED, mockedBundle);
    List<ServiceRegistration<WebResource>> serviceList =
        Collections.nCopies(3, mockedWebResourceRegistration);

    webResourceTrackerUnderTest.removedBundle(mockedBundle, bundleEvent, serviceList);

    verify(mockedWebResourceRegistration, times(3)).unregister();
  }

}
