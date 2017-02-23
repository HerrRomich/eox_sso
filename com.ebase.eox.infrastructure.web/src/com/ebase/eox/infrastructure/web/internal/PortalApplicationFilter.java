package com.ebase.eox.infrastructure.web.internal;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(scope = ServiceScope.PROTOTYPE, service = Filter.class,
    property = {
        HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "="
            + PortalApplicationFilter.APPLICATION_CONTEXT + "/*",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=("
            + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=com.ebase.eox.appcontent)"})
public class PortalApplicationFilter implements Filter {

  private static final String USER_ROLE_SESSION_ATTRIBUTE = "com.ebase.eox.userrole";
  public static final String APPLICATION_CONTEXT = "/eox";
  public static final String USER_ROLE = "eox";

  @Override
  public void destroy() {}

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    HttpSession session = request.getSession(false);
    if (session == null) {
      session = request.getSession();
    }
    String userRole = (String)session.getAttribute(USER_ROLE_SESSION_ATTRIBUTE);
        if (!USER_ROLE.equals(userRole)) {
          request.logout();
          session.setAttribute(USER_ROLE_SESSION_ATTRIBUTE, USER_ROLE);
        }
    
    
    String pathInfo = request.getPathInfo();
    if (pathInfo != null && pathInfo.startsWith(PortalApplicationFilter.APPLICATION_CONTEXT)) {
      doForwardToDestination(pathInfo, request, response);
    } else {
      chain.doFilter(request, response);
    }
  }

  private void doForwardToDestination(String pathInfo, HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String pathToForward = null;
    if (PortalApplicationFilter.APPLICATION_CONTEXT.equals(pathInfo)) {
      pathToForward = "/index.html";
    } else {
      pathToForward = pathInfo.substring(PortalApplicationFilter.APPLICATION_CONTEXT.length());
    }
    request.getRequestDispatcher(pathToForward).forward(request, response);
  }

  @Override
  public void init(FilterConfig config) throws ServletException {}

}
