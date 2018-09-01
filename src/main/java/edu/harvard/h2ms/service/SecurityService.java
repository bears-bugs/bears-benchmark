package edu.harvard.h2ms.service;

public interface SecurityService {
	String findLoggedInUserName();
	
	void autologin(String email, String password);

}
