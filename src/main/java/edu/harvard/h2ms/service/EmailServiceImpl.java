package edu.harvard.h2ms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

@Component
@Service("emailService")
@PropertySources({
	@PropertySource(value = "classpath:mailserver.mock.properties", ignoreResourceNotFound = true),
	@PropertySource(value = "classpath:mailserver.properties", ignoreResourceNotFound = true)
})
public class EmailServiceImpl implements EmailService, ApplicationListener<ApplicationReadyEvent> {
	
	final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	// Async will not wait for email delivery
	@Async
	public void sendEmail(SimpleMailMessage email) {		
		log.info("sending email to: "+email);
		mailSender.send(email);
	}
	
	@Value("${emailServerType}")
	private String emailServerType;
	
	static private GreenMail greenMail = null;
	/**
	* http://blog.netgloo.com/2014/11/13/run-code-at-spring-boot-startup/
	* This event is executed as late as conceivably possible to indicate that 
	* the application is ready to service requests.
	*/
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		
		if(greenMail == null && emailServerType.equalsIgnoreCase("greenmail")) {		
			log.info("************************STARTING GREENMAIL****************");
			greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();			
		}
		return;
	}

}
