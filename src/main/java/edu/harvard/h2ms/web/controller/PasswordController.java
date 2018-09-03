package edu.harvard.h2ms.web.controller;

import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.UserRepository;
import edu.harvard.h2ms.service.EmailService;
import edu.harvard.h2ms.service.UserService;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

/**
 * Adapted from: https://www.codebyamir.com/blog/forgot-password-feature-with-java-and-spring-boot
 */
@RestController
@RequestMapping(path = {"/api/passwords", "/registration"})
@PropertySources({
  @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
  @PropertySource(
    value = "classpath:application.properties.override",
    ignoreResourceNotFound = true
  )
})
public class PasswordController {

  final Logger log = LoggerFactory.getLogger(PasswordController.class);

  @Value("${application.security.properties.admin.usertype}")
  private String adminUserType;

  @Autowired private UserService userService;

  @Autowired private EmailService emailService;

  @Autowired public JavaMailSender emailSender;

  @Autowired private UserRepository userRepository;

  /**
   * Sets the reset parameter e.g., http://localhost:8080/api/passwords/reset/admin@h2ms.org
   *
   * @param email The user's email
   * @param resetPasswordCallback The url that the user will be redirected to so that they can enter
   *     a new password. The token is appended to the callback as a path variable.
   * @param request Allows access to the server's name. e.x. h2ms.org
   * @return ok signal : {"action": "user reset token set"}
   */
  @RequestMapping(value = "/reset/{email:.+}", method = RequestMethod.GET)
  public ResponseEntity<Object> getPasswordResetToken(
      @PathVariable String email,
      @RequestParam("resetPasswordCallback") String resetPasswordCallback,
      HttpServletRequest request) {
    Map<String, String> entity = new HashMap<>();
    entity.put("action", "user reset token set");

    User user = null;
    try {
      user = userService.findUserByEmail(UriUtils.decode(email, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    if (user != null) {
      String token = UUID.randomUUID().toString();
      user.setResetToken(token);

      SimpleMailMessage message = new SimpleMailMessage();

      // user email address
      message.setTo(user.getEmail());

      // uncomment for quick test:
      // message.setTo("my.email.address@gmail.com");

      message.setSubject("Reset Password - " + request.getServerName());

      String messageText =
          "Please click the following link to reset your password: "
              + resetPasswordCallback
              + "/"
              + token;
      message.setText(messageText);

      // actually send the message
      emailService.sendEmail(message);

      // Save user
      userService.save(user);

    } else log.info("error finding user with email: " + email);
    return new ResponseEntity<Object>(entity, HttpStatus.OK);
  }

  /**
   * Resets password for user with reset token e.g.:
   * {"token":"6b536da2-b3f0-4dcf-91ae-00e6d5c9c666","password":"newpassword"}
   *
   * @return ok signal : {"action": "user password reset"}
   */
  @RequestMapping(value = "/reset/token", method = RequestMethod.POST)
  public ResponseEntity<Object> resetPasswordViaToken(
      @RequestBody Map<String, String> requestParams) {

    Map<String, String> entity = new HashMap<>();
    entity.put("action", "user password reset");

    String token = requestParams.get("token");
    String password = requestParams.get("password");
    User user = userService.findUserByResetToken(token);
    if (user != null) {
      user.setPassword(password);
      user.setVerified(true);
      // Save user
      userService.save(user);
    } else log.info("error finding user with token: " + token);

    return new ResponseEntity<Object>(entity, HttpStatus.OK);
  }

  /**
   * Restful API for User registration by email
   *
   * <p>*****************************************
   *
   * <p>FRONT END DEV: DO NOT ASK FOR PASSWORD - it'll be clobbered
   *
   * <p>*****************************************
   */
  @RequestMapping(value = "/newuser/email", method = RequestMethod.POST)
  public ResponseEntity<?> registerUserByEmail(@RequestBody User user) {

    // User created will need verification
    user.setVerified(false);

    String token = UUID.randomUUID().toString();
    user.setResetToken(token);

    // TODO: user password can't be set
    user.setPassword(token);
    if (userRepository.findByEmail(user.getEmail()) != null) {
      final String MSG = "user email already taken";
      log.info(MSG);
      return new ResponseEntity<String>(MSG, HttpStatus.CONFLICT);
    }

    if (user.getType() == adminUserType) {
      final String MSG = "admin user cannot be created using standard email registration";
      log.info(MSG);
      return new ResponseEntity<String>(MSG, HttpStatus.FORBIDDEN);
    }

    // TODO: is there a password policy?
    userRepository.save(user);

    SimpleMailMessage message = new SimpleMailMessage();

    /** user email address * */
    message.setTo(user.getEmail());

    /** uncomment for quick test: * */
    // message.setTo("my.email.address@gmail.com");

    message.setSubject("h2msreset token - new user registration");
    message.setText("please use the password reset token: " + user.getResetToken());

    // actually send the message
    emailService.sendEmail(message);

    // Save user
    userService.save(user);
    Map<String, String> entity = new HashMap<>();
    entity.put("action", "user password reset");

    return new ResponseEntity<Object>(entity, HttpStatus.OK);
  }
}
