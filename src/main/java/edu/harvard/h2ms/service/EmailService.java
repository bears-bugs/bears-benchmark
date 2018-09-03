package edu.harvard.h2ms.service;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	
	public void sendEmail(SimpleMailMessage email);

}
