package com.ebase.eox.homepage;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.log.LogService;

@Component(immediate = true, service = Filter.class,
    property = {HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "=/*"})
public class EoxHomePageHttpFilter implements Filter {

  private LogService logServices;

  @Reference
  protected void setLogService(LogService logServices) {
    synchronized (this) {
      this.logServices = logServices;
    }
  }
  
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    logServices.log(LogService.LOG_INFO, "catched");
    if (request instanceof HttpServletRequest) {
      HttpServletRequest r = (HttpServletRequest) request;
      String pathInfo = r.getPathInfo();
      if (pathInfo == null || "/".equals(pathInfo)) {
        r.getRequestDispatcher("index.html").forward(request, response);
      } else {
        chain.doFilter(request, response);
      }
    } else {
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {}

}
