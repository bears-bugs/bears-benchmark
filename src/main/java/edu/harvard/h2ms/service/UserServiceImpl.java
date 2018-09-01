package edu.harvard.h2ms.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.UserRepository;

/**
 * Contains the business logic and call methods in the repository layer
 * https://www.codebyamir.com/blog/create-rest-api-with-spring-boot
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		userRepository.findAll().forEach(users::add);
		return users;
	}
	
	public User getUser(Long id) {
		return userRepository.findOne(id);
	}
	
	public User addUser(User user) {
		userRepository.save(user);
		return user;
	}
	
	public void deleteUser(Long id) {
		userRepository.delete(id);
	}
	

	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//		user.setRoles(new HashSet<>(roleRepository.findAll()));
		userRepository.save(user);
	}

}
