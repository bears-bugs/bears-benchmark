package edu.harvard.h2ms.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.harvard.h2ms.domain.core.Role;
import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.UserRepository;

/**
 * https://github.com/hellokoding/registration-login-spring-hsql/blob/master/src/main/java/com/hellokoding/auth/service/UserDetailsServiceImpl.java
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (Role role : user.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
						
		}
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
		
	}
}
