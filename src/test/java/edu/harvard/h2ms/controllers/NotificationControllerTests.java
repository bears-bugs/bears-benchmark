package edu.harvard.h2ms.controllers;

import static edu.harvard.h2ms.common.TestHelpers.obtainAccessToken;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.harvard.h2ms.domain.core.Notification;
import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.NotificationRepository;
import edu.harvard.h2ms.repository.UserRepository;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * look for e.h.h.c.NotificationControllerTests reference:
 * https://spring.io/guides/tutorials/bookmarks/
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class NotificationControllerTests {

  private static final Log log = LogFactory.getLog(NotificationControllerTests.class);

  static final String EMAIL = "jqadams@h2ms.org";
  static final String FIRSTNAME = "John";
  static final String MIDDLENAME = "Quincy";
  static final String LASTNAME = "Adams";
  static final String TYPE = "Doctor";
  static final String PASSWORD = "password";
  static final String NEWPASSWORD = "newpassword";
  static final String CONTENT_TYPE = "application/json;charset=UTF-8";

  static final String NOTIFICATION_EMAIL2 = "jdsam@email.com";

  @Autowired private FilterChainProxy springSecurityFilterChain;

  @Autowired private WebApplicationContext context;

  private MockMvc mvc;
  private User observer;

  @Autowired UserRepository userRepository;

  @Autowired NotificationRepository notificationRepository;

  /**
   * Setup prior to running unit tests
   *
   * @throws Exception
   */
  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);
    this.mvc =
        MockMvcBuilders.webAppContextSetup(context).addFilter(springSecurityFilterChain).build();

    // Sample User Data
    observer = new User("John", "Quincy", "Adams", EMAIL, PASSWORD, "Other");
    userRepository.save(observer);

    User subject = new User("Jane", "Doe", "Sam", NOTIFICATION_EMAIL2, "password", "Doctor");
    userRepository.save(subject);
  }

  /** Tests the ability to create and subscribe to a new notification */
  @Test
  @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
  public void test_Success_NotificationController_postCompliance() throws Exception {

    String NOTIFICATION_EMAIL = EMAIL;
    String NOTIFICATION_PASSWORD = PASSWORD;
    final String accessToken = obtainAccessToken(mvc, NOTIFICATION_EMAIL, NOTIFICATION_PASSWORD);

    String NOTIFICATION_NAME = "myNotification1";
    String NOTIFICATION_TYPE = "compliance1";
    String NOTIFICATION_TITLE = "myNotification1";
    String NOTIFICATION_BODY = "notification body text";

    // compose POST request JSON payload
    ObjectMapper mapper1 = new ObjectMapper();
    ObjectNode objectNode1 = mapper1.createObjectNode();
    objectNode1.put("name", NOTIFICATION_NAME);
    objectNode1.put("reportType", NOTIFICATION_TYPE);
    objectNode1.put("notificationTitle", NOTIFICATION_TITLE);
    objectNode1.put("notificationBody", NOTIFICATION_BODY);

    MockHttpServletResponse result1 =
        mvc.perform(
                post(String.format("/notifications"))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectNode1.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is(NOTIFICATION_NAME)))
            .andExpect(jsonPath("$.reportType", is(NOTIFICATION_TYPE)))
            .andExpect(jsonPath("$.notificationTitle", is(NOTIFICATION_TITLE)))
            .andExpect(jsonPath("$.notificationBody", is(NOTIFICATION_BODY)))
            .andReturn()
            .getResponse();

    log.debug(result1.getContentAsString());

    // Subscribe 1st user

    // compose POST request JSON payload
    ObjectMapper mapper2 = new ObjectMapper();
    ObjectNode objectNode2 = mapper2.createObjectNode();
    objectNode2.put("notificationName", NOTIFICATION_NAME);
    objectNode2.put("email", NOTIFICATION_EMAIL);

    MockHttpServletResponse result2 =
        mvc.perform(
                post(String.format("/notifications/subscribe"))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectNode2.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.action", is("user subscribed to notification")))
            .andExpect(jsonPath("$.notificationName", is(NOTIFICATION_NAME)))
            .andExpect(jsonPath("$.user", is(NOTIFICATION_EMAIL)))
            .andReturn()
            .getResponse();

    log.debug(result2.getContentAsString());

    Notification notification1 = notificationRepository.findOneByName(NOTIFICATION_NAME);

    log.debug("Notification users: " + notification1.getUser());

    Set<User> notifiedUsers1 = notification1.getUser();

    // subscriber count should be 1 now.
    assertThat(notifiedUsers1.size(), is(1));

    // Subscribe 2nd user

    // compose POST request JSON payload
    ObjectMapper mapper3 = new ObjectMapper();
    ObjectNode objectNode3 = mapper3.createObjectNode();
    objectNode3.put("notificationName", NOTIFICATION_NAME);
    objectNode3.put("email", NOTIFICATION_EMAIL2);

    MockHttpServletResponse result3 =
        mvc.perform(
                post(String.format("/notifications/subscribe"))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectNode3.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.action", is("user subscribed to notification")))
            .andExpect(jsonPath("$.notificationName", is(NOTIFICATION_NAME)))
            .andExpect(jsonPath("$.user", is(NOTIFICATION_EMAIL2)))
            .andReturn()
            .getResponse();

    log.debug(result3.getContentAsString());

    Notification notification2 = notificationRepository.findOneByName(NOTIFICATION_NAME);

    log.debug("Notification users: " + notification2.getUser());

    Set<User> notifiedUsers2 = notification2.getUser();

    // subscriber count should've increased to 2 now.
    assertThat(notifiedUsers2.size(), is(2));

    // Unsubscribe 1st user
    // compose POST request JSON payload
    ObjectMapper mapper4 = new ObjectMapper();
    ObjectNode objectNode4 = mapper3.createObjectNode();
    objectNode4.put("notificationName", NOTIFICATION_NAME);
    objectNode4.put("email", NOTIFICATION_EMAIL2);

    MockHttpServletResponse result4 =
        mvc.perform(
                post(String.format("/notifications/unsubscribe"))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectNode4.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.action", is("user unsubscribed to notification")))
            .andExpect(jsonPath("$.notificationName", is(NOTIFICATION_NAME)))
            .andExpect(jsonPath("$.user", is(NOTIFICATION_EMAIL2)))
            .andReturn()
            .getResponse();

    log.debug(result3.getContentAsString());

    Notification notification3 = notificationRepository.findOneByName(NOTIFICATION_NAME);

    log.debug("Notification users: " + notification3.getUser());

    Set<User> notifiedUsers3 = notification3.getUser();

    // subscriber count should've decreased to 1 now.
    assertThat(notifiedUsers3.size(), is(1));
  }
}
