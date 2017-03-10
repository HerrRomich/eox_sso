package com.ebase.eox.infrastructure.web.internal;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(scope = ServiceScope.PROTOTYPE, service = Filter.class,
    property = {
        HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "="
            + PortalApplicationFilter.APPLICATION_CONTEXT + "*",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=("
            + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=com.ebase.eox.appcontent)"})
public class PortalApplicationFilter implements Filter {

  private static final String INDEX_HTML = "/index.html";
  private static final String USER_ROLE_SESSION_ATTRIBUTE = "com.ebase.eox.userrole";
  public static final String APPLICATION_CONTEXT = "/";
  public static final String USER_ROLE = "eox";

  @Activate
  protected void activate() {
    System.out.println("Test");
  }
  
  @Override
  public void destroy() {}

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    /*
     * Boolean isFirstCall = (Boolean)request.getAttribute(FIRST_CALL_REQUEST_ATTR); if (isFirstCall
     * != null && !isFirstCall) return; request.setAttribute(FIRST_CALL_REQUEST_ATTR, true);
     * 
     * 
     * HttpSession session = request.getSession(false); if (session == null) { session =
     * request.getSession(); } String userRole = (String)
     * session.getAttribute(USER_ROLE_SESSION_ATTRIBUTE); if (!USER_ROLE.equals(userRole)) {
     * request.logout(); session.setAttribute(USER_ROLE_SESSION_ATTRIBUTE, USER_ROLE); }
     */
    chain.doFilter(request, response);
    // Response is OK
    if (response.getStatus() < 400)
      return;

    RequestDispatcher requestDispatcherToIndexHtml = request.getRequestDispatcher(INDEX_HTML);
    requestDispatcherToIndexHtml.forward(request, response);
  }

  @Override
  public void init(FilterConfig config) throws ServletException {}

}
