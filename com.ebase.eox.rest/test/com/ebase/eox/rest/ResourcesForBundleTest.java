package com.ebase.eox.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Dictionary;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.*;
import org.osgi.service.log.LogService;

public class ResourcesForBundleTest {

  private static final String RESOURCE_2_NAME = "com.ebase.eox.test.TestResource2";
  private static final String RESOURCE_1_NAME = "com.ebase.eox.test.TestResource1";
  private static final String RESORCES_CONTEXT = "eox";
  private static final Long TEST_BUNDLE_ID = 12L;
  private static final String WRONG_MANIFEST = "grum=pulum";
  private static final String CORRECT_MANIFEST = RESORCES_CONTEXT + ";classes=\"" + RESOURCE_1_NAME + "," + RESOURCE_2_NAME + "\"";

  @Mock
  private Bundle mockedBundle;
  @Mock
  private BundleContext mockedBundleContext;
  @Mock
  private Dictionary<String, String> mockedHeaders;
  @Mock
  private ServiceReference<LogService> mockedLogServiceReference;
  @Mock
  private LogService mockedLogService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    
    when(mockedBundle.getBundleId()).thenReturn(TEST_BUNDLE_ID);
    when(mockedBundle.getBundleContext()).thenReturn(mockedBundleContext);
    when(mockedBundle.getHeaders()).thenReturn(mockedHeaders);
    
    when(mockedBundleContext.getServiceReference(LogService.class)).thenReturn(mockedLogServiceReference);
    when(mockedBundleContext.getService(mockedLogServiceReference)).thenReturn(mockedLogService);
  }

  @Test
  public void testWithNoManifest() throws BundleException {
    ResourcesForBundle resources = ResourcesForBundle.of(mockedBundle);
    
    assertEquals(TEST_BUNDLE_ID, resources.getBundleId());
    assertEquals(0, resources.getResourceContexts().size());
  }
  
  @Test(expected = BundleException.class)
  public void testWithWrongFormatedManifest() throws BundleException {
    when(mockedHeaders.get(ResourcesForBundle.RESOURCES_MANIFEST)).thenReturn(WRONG_MANIFEST);
    
    ResourcesForBundle.of(mockedBundle);
  }
  
  @Test
  public void testWithCorrectManifest() throws BundleException {
    when(mockedHeaders.get(ResourcesForBundle.RESOURCES_MANIFEST)).thenReturn(CORRECT_MANIFEST);

    ResourcesForBundle resources = ResourcesForBundle.of(mockedBundle);
    
    assertEquals(TEST_BUNDLE_ID, resources.getBundleId());
    Set<String> resourceContexts = resources.getResourceContexts();
    assertEquals(1, resourceContexts.size());
    assertThat(resourceContexts, org.hamcrest.core.IsCollectionContaining.hasItem(RESORCES_CONTEXT));
  }

}
