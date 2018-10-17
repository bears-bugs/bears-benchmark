package edu.harvard.h2ms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

	@Override
	public String findLoggedInUserName() {
		Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
		
		if (userDetails instanceof UserDetails) {
			return ((UserDetails)userDetails).getUsername();
		}
		return null;
	}

	@Override
	public void autologin(String email, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		
		authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		
		if(usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			logger.debug(String.format("autologin %s success!",email));
		}
		
	}
	
	
	

}
