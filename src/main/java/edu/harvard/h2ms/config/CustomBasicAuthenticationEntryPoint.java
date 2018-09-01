package edu.harvard.h2ms.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * http://websystique.com/spring-security/secure-spring-rest-api-using-basic-authentication/
 *
 */
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
	final AuthenticationException authException) throws IOException, ServletException {
		//Auth fail, send error response
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader("WWW-Authenticate",  "Basic realm=" + getRealmName() + "");
		
		PrintWriter writer = response.getWriter();
		writer.println("HTTP Status 401 : " + authException.getMessage());
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		setRealmName("My_TEST_REALM");
		super.afterPropertiesSet();
	}
	
}
