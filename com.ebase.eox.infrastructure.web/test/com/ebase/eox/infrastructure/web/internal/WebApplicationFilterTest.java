package com.ebase.eox.infrastructure.web.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.ebase.eox.infrastructure.web.internal.WebApplicationFilter;

@RunWith(MockitoJUnitRunner.class)
public class PortalApplicationFilterTest {

  private static final String KNOWN_RESOURCE = "/known_kontext/known_resource.resource";
  private static final String UNKNOWN_RESOURCE = "/unknown_kontext/known_resource";

  private static final String INDEX_HTML = "/index.html";

  @InjectMocks
  private WebApplicationFilter portalApplicationFilterUnderTest = new WebApplicationFilter();

  @Mock
  private HttpServletRequest mockedServletRequest;
  @Mock
  private HttpServletResponse mockedServletResponse;
  @Mock
  private FilterChain mockedFilterChain;
  @Mock
  private RequestDispatcher mockedRequestDispatcher;

  @Before
  public void setUp() throws Exception {
    when(mockedServletRequest.getRequestDispatcher(eq(INDEX_HTML)))
        .thenReturn(mockedRequestDispatcher);
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        HttpServletRequest request = (HttpServletRequest) invocation.getArgument(0);
        HttpServletResponse response = (HttpServletResponse) invocation.getArgument(1);
        if (KNOWN_RESOURCE.equals(request.getPathInfo())) {
          when(response.getStatus()).thenReturn(HttpServletResponse.SC_OK);
        } else {
          when(response.getStatus()).thenReturn(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
      }
    }).when(mockedFilterChain).doFilter(eq(mockedServletRequest), eq(mockedServletResponse));
  }

  @Test
  public void shouldRedirectToIndexHtmlWhenResourceNotFound() throws IOException, ServletException {
    when(mockedServletRequest.getPathInfo()).thenReturn(UNKNOWN_RESOURCE);

    portalApplicationFilterUnderTest.doFilter(mockedServletRequest, mockedServletResponse,
        mockedFilterChain);

    verify(mockedFilterChain).doFilter(eq(mockedServletRequest), eq(mockedServletResponse));
    verify(mockedServletRequest).getRequestDispatcher(eq(INDEX_HTML));
    verify(mockedRequestDispatcher).forward(eq(mockedServletRequest), eq(mockedServletResponse));
  }

  @Test
  public void shouldDoNothingWhenResourceFound() throws IOException, ServletException {
    when(mockedServletRequest.getPathInfo()).thenReturn(KNOWN_RESOURCE);

    portalApplicationFilterUnderTest.doFilter(mockedServletRequest, mockedServletResponse,
        mockedFilterChain);

    verify(mockedServletRequest, never()).getRequestDispatcher(any());
  }

}
