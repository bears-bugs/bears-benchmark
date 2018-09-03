package edu.harvard.h2ms.web.controller;

import edu.harvard.h2ms.domain.core.Notification;
import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.NotificationRepository;
import edu.harvard.h2ms.repository.UserRepository;
import edu.harvard.h2ms.service.NotificationServiceImpl;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/notifications")
public class NotificationController {

  final Logger log = LoggerFactory.getLogger(EventController.class);

  @Autowired private NotificationRepository notificationRepository;

  @Autowired private NotificationServiceImpl notificationService;

  @Autowired private UserRepository userRepository;

  /**
   * Subscribes user to notifications
   *
   * @param requestParams
   * @return
   */
  @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
  public ResponseEntity<?> subscribeUser(@RequestBody Map<String, String> requestParams) {

    String email = (String) requestParams.get("email");
    String notificationName = (String) requestParams.get("notificationName");

    log.debug("searching for user by email " + requestParams);

    User user = userRepository.findByEmail(email);
    if (user == null) {
      final String MSG = "user email not found";
      log.info(MSG);
      return new ResponseEntity<String>(MSG, HttpStatus.NOT_FOUND);
    }

    log.debug("user found " + user);

    Notification notification = notificationRepository.findOneByName(notificationName);
    if (notification == null) {
      final String MSG = "notification not found";
      log.info(MSG);
      return new ResponseEntity<String>(MSG, HttpStatus.NOT_FOUND);
    }

    notificationService.subscribeUserNotification(user, notification);

    // Prepare return message
    Map<String, String> entity = new HashMap<>();
    entity.put("action", "user subscribed to notification");
    entity.put("user", email);
    entity.put("notificationName", notificationName);

    return new ResponseEntity<Object>(entity, HttpStatus.OK);
  }

  /**
   * Subscribes user to notifications
   *
   * @param requestParams
   * @return
   */
  @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
  public ResponseEntity<?> unsubscribeUser(@RequestBody Map<String, String> requestParams) {

    String email = (String) requestParams.get("email");
    String notificationName = (String) requestParams.get("notificationName");

    log.debug("searching for user by email " + requestParams);

    User user = userRepository.findByEmail(email);
    if (user == null) {
      final String MSG = "user email not found";
      log.info(MSG);
      return new ResponseEntity<String>(MSG, HttpStatus.NOT_FOUND);
    }

    log.debug("user found " + user);

    Notification notification = notificationRepository.findOneByName(notificationName);
    if (notification == null) {
      final String MSG = "notification not found";
      log.info(MSG);
      return new ResponseEntity<String>(MSG, HttpStatus.NOT_FOUND);
    }

    notificationService.unsubscribeUserNotification(user, notification);

    // Prepare return message
    Map<String, String> entity = new HashMap<>();
    entity.put("action", "user unsubscribed to notification");
    entity.put("user", email);
    entity.put("notificationName", notificationName);

    return new ResponseEntity<Object>(entity, HttpStatus.OK);
  }
}
