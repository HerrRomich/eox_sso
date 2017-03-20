package com.ebase.eox.infrastructure.web.internal;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.context.ServletContextHelper;

import com.ebase.eox.infrastructure.web.internal.DelegatedServletContextHalper;

@RunWith(MockitoJUnitRunner.class)
public class DelegatedServletContextHalperTest {

  private static final String TEST_MIME_TYPE_NAME = "test/mime_type_name";
  private static final String TEST_PATH = "test/path";
  private static final String TEST_RESOURCE_NAME = "test_resource_name";

  private DelegatedServletContextHalper delegatedServletContextHelperUnderTest;
  @Mock
  private ServletContextHelper mockedDelegate;
  @Mock
  private Bundle mockedBundle;
  @Mock
  private ComponentContext mockedComponentContext;
  @Mock
  private Bundle mockedUsingBundle;

  @Before
  public void setup() {
    delegatedServletContextHelperUnderTest = spy(new DelegatedServletContextHalper() {});
    delegatedServletContextHelperUnderTest.servletContextHelperDelegate = mockedDelegate;
  }

  @Test
  public void testActivate() {
    when(mockedComponentContext.getUsingBundle()).thenReturn(mockedUsingBundle);
    delegatedServletContextHelperUnderTest.activate(mockedComponentContext);

    verify(delegatedServletContextHelperUnderTest).activateForBundle(eq(mockedUsingBundle));
  }

  @Test
  public void itShouldCreateDelegateWithBundle() {
    delegatedServletContextHelperUnderTest.servletContextHelperDelegate = null;

    delegatedServletContextHelperUnderTest.activateForBundle(mockedBundle);

    assertThat(delegatedServletContextHelperUnderTest.servletContextHelperDelegate,
        not(nullValue()));
  }

  @Test
  public void itShouldCallDelegateOnGetMimeTipeDelegate() {
    delegatedServletContextHelperUnderTest.getMimeType(TEST_MIME_TYPE_NAME);

    verify(mockedDelegate).getMimeType(eq(TEST_MIME_TYPE_NAME));
  }

  @Test
  public void itShouldCallDelegateOnGetRealPath() {
    delegatedServletContextHelperUnderTest.getRealPath(TEST_PATH);

    verify(mockedDelegate).getRealPath(eq(TEST_PATH));
  }

  @Test
  public void itShouldCallDelegateOnGetResource() {
    delegatedServletContextHelperUnderTest.getResource(TEST_RESOURCE_NAME);

    verify(mockedDelegate).getResource(eq(TEST_RESOURCE_NAME));
  }

  @Test
  public void itShouldCallDelegateOnGetResourcePaths() {
    delegatedServletContextHelperUnderTest.getResourcePaths(TEST_PATH);

    verify(mockedDelegate).getResourcePaths(eq(TEST_PATH));
  }

}
