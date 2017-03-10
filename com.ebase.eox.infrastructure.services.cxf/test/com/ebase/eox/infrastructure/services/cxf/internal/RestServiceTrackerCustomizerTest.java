package com.ebase.eox.infrastructure.services.cxf.internal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.ebase.eox.infrastructure.services.RestService;
import com.ebase.eox.infrastructure.services.cxf.internal.RestServiceTrackerCustomizer;
import com.ebase.eox.infrastructure.services.cxf.internal.ServiceBuilder;

@RunWith(MockitoJUnitRunner.class)
public class RestServiceTrackerCustomizerTest {

  private static final String SERVICESCONTENT = "com.ebase.eox.servicescontent";
  private static final String CONTEXT_SELECT_FORMAT = "(%1$s=%2$s)";
  private static final String BASIC_CONTEXT = "/";

  @InjectMocks
  private RestServiceTrackerCustomizer restServiceTrackerCustomizerUnderTest;

  @Mock
  private ServiceReference<RestService> mockedServiceApplicationRegistrationWithPath;
  @Mock
  private BundleContext mockedContext;
  @Mock
  private ServiceBuilder mockedServiceBuilder;
  @Mock
  private RestService mockedRestService;
  @Mock
  private Servlet mockedRestServlet;
  @Mock
  private ServiceRegistration<Servlet> mockedServletRegistration;
  @Mock
  private ServiceReference<RestService> mockedServiceApplicationRegistrationWithoutPath;

  @Before
  public void setUp() throws Exception {
    restServiceTrackerCustomizerUnderTest =
        spy(new RestServiceTrackerCustomizer(mockedContext, mockedServiceBuilder));

    when(mockedContext.getService(eq(mockedServiceApplicationRegistrationWithPath)))
        .thenReturn(mockedRestService);
  }

  @Test
  public void shouldRegisterRestServletOnAddingRestService() {
    when(mockedServiceBuilder.buildJaxRsServlet(eq(mockedRestService)))
        .thenReturn(mockedRestServlet);
    Dictionary<String, ?> testProperties = generateTestPropertiesWithPath();
    when(
        mockedContext.registerService(eq(Servlet.class), eq(mockedRestServlet), eq(testProperties)))
            .thenReturn(mockedServletRegistration);

    ServiceRegistration<Servlet> actualServiceReference = restServiceTrackerCustomizerUnderTest
        .addingService(mockedServiceApplicationRegistrationWithPath);

    assertThat(actualServiceReference, equalTo(mockedServletRegistration));
  }

  private Dictionary<String, ?> generateTestPropertiesWithPath() {
    Dictionary<String, String> props = new Hashtable<>();
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, BASIC_CONTEXT);
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED, "true");
    String contextSelectPropertyValue = String.format(CONTEXT_SELECT_FORMAT,
        HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME, SERVICESCONTENT);
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT, contextSelectPropertyValue);
    return props;
  }

  @Test
  public void shouldUnregisterRestServletOnRemovedService() {
    restServiceTrackerCustomizerUnderTest
        .removedService(mockedServiceApplicationRegistrationWithPath, mockedServletRegistration);

    verify(mockedServletRegistration).unregister();
  }

  @Test
  public void shouldCallSequentiallyRemovedServiceAndAddingServiceOnModifiedServices() {
    restServiceTrackerCustomizerUnderTest
        .modifiedService(mockedServiceApplicationRegistrationWithPath, mockedServletRegistration);

    InOrder inOrder = inOrder(restServiceTrackerCustomizerUnderTest);

    inOrder.verify(restServiceTrackerCustomizerUnderTest).removedService(
        eq(mockedServiceApplicationRegistrationWithPath), eq(mockedServletRegistration));
    inOrder.verify(restServiceTrackerCustomizerUnderTest)
        .addingService(eq(mockedServiceApplicationRegistrationWithPath));
  }

}
