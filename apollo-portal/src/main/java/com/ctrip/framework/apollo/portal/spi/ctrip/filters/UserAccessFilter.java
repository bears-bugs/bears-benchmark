package com.ctrip.framework.apollo.portal.spi.ctrip.filters;

import com.ctrip.framework.apollo.portal.constant.TracerEventType;
import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import com.ctrip.framework.apollo.tracer.Tracer;

import com.google.common.base.Strings;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

public class UserAccessFilter implements Filter {

  private static final String STATIC_RESOURCE_REGEX = ".*\\.(js|html|htm|png|css|woff2)$";

  private UserInfoHolder userInfoHolder;

  public UserAccessFilter(UserInfoHolder userInfoHolder) {
    this.userInfoHolder = userInfoHolder;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String requestUri = ((HttpServletRequest) request).getRequestURI();

    try {
      if (!isOpenAPIRequest(requestUri) && !isStaticResource(requestUri)) {
        UserInfo userInfo = userInfoHolder.getUser();
        if (userInfo != null) {
          Tracer.logEvent(TracerEventType.USER_ACCESS, userInfo.getUserId());
        }
      }
    } catch (Throwable e) {
      Tracer.logError("Record user access info error.", e);
    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {

  }

  private boolean isOpenAPIRequest(String uri) {
    return !Strings.isNullOrEmpty(uri) && uri.startsWith("/openapi");
  }

  private boolean isStaticResource(String uri) {
    return !Strings.isNullOrEmpty(uri) && uri.matches(STATIC_RESOURCE_REGEX);
  }

}
