package edu.harvard.h2ms.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.service.EmailService;
import edu.harvard.h2ms.service.UserService;

/**
 * Adapted from: https://www.codebyamir.com/blog/forgot-password-feature-with-java-and-spring-boot
 *
 */
@RestController
@RequestMapping(path="/api/passwords")
public class PasswordController {
	
	final Logger log = LoggerFactory.getLogger(PasswordController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	public JavaMailSender emailSender;
	
	/**
	 * Sets the reset parameter
	 * e.g.,  http://localhost:8080/api/passwords/reset/admin@h2ms.org
	 * @param email
	 * @return  ok signal : {"action": "user reset token set"}
	 */
	@RequestMapping(value = "/reset/{email:.+}", method = RequestMethod.GET)
	public ResponseEntity<Object> getPasswordResetToken(@PathVariable String email) {
		
		Map<String,String> entity = new HashMap<>();
		entity.put("action", "user reset token set");
		
		User user = null;
		try {
			user = userService.findUserByEmail(UriUtils.decode(email, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(user != null) {
			String token = UUID.randomUUID().toString();
			user.setResetToken(token);
			
			SimpleMailMessage message = new SimpleMailMessage();
			
			/** user email address **/
			message.setTo(user.getEmail());
			
			/** uncomment for quick test: **/
			//message.setTo("my.email.address@gmail.com");
			
			message.setSubject("h2msreset token");
			message.setText("password reset token: "+token);
			
			// actually send the message
			emailService.sendEmail(message);
			
			// Save user
			userService.save(user);
			
			
		} else 
			log.info("error finding user with email: " + email);
		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}
	
	/**
	 * Resets password for user with reset token
	 * e.g.: {"token":"6b536da2-b3f0-4dcf-91ae-00e6d5c9c666","password":"newpassword"}
	 * @param requestParams
	 * @return  ok signal : {"action": "user password reset"}
	 */
	@RequestMapping(value = "/reset/token", method = RequestMethod.POST)
	public ResponseEntity<Object> resetPasswordViaToken(@RequestBody Map<String, String> requestParams) {
		
		Map<String,String> entity = new HashMap<>();
		entity.put("action", "user password reset");
		
		String token = requestParams.get("token");
		String password = requestParams.get("password");
		User user = userService.findUserByResetToken(token);
		if(user != null) {
			user.setPassword(password);
			// Save user
			userService.save(user);
		} else 
			log.info("error finding user with token: " + token);
		
		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}
}
