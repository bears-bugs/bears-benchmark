package edu.harvard.h2ms.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.UserRepository;
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

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class PasswordControllerTests {

  private static final Log log = LogFactory.getLog(PasswordControllerTests.class);

  static final String EMAIL = "jqadams@h2ms.org";
  static final String FIRSTNAME = "John";
  static final String MIDDLENAME = "Quincy";
  static final String LASTNAME = "Adams";
  static final String TYPE = "Doctor";
  static final String PASSWORD = "password";
  static final String NEWPASSWORD = "newpassword";
  static final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Autowired private FilterChainProxy springSecurityFilterChain;

  @Autowired private WebApplicationContext context;

  private MockMvc mvc;

  @Autowired UserRepository userRepository;

  /** Setup prior to running unit tests */
  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);
    this.mvc =
        MockMvcBuilders.webAppContextSetup(context).addFilter(springSecurityFilterChain).build();
  }

  /** Tests user registration */
  @Test
  @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
  public void test_register_passwordController_registerUserByEmail() throws Exception {

    ObjectMapper mapper1 = new ObjectMapper();

    User user = new User(FIRSTNAME, MIDDLENAME, LASTNAME, EMAIL, PASSWORD, TYPE);

    log.debug("Test: user info for registration " + mapper1.writeValueAsString(user));

    MockHttpServletResponse result =
        mvc.perform(
                post(String.format("/registration/newuser/email"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper1.writeValueAsString(user))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

    log.debug("Test: user info for registration");
    log.debug(result.getContentAsString());

    User registeredUser = userRepository.findByEmail(EMAIL);

    String resetToken = registeredUser.getResetToken();
    log.debug("isVerified" + registeredUser.isVerified());

    // should NOT be verified right after registration
    assertFalse(registeredUser.isVerified());

    assertEquals(registeredUser.getFirstName(), FIRSTNAME);

    ObjectMapper mapper2 = new ObjectMapper();
    ObjectNode objectNode2 = mapper2.createObjectNode();
    objectNode2.put("token", resetToken);
    objectNode2.put("password", NEWPASSWORD);

    // do a password reset
    MockHttpServletResponse result2 =
        mvc.perform(
                post(String.format("/registration/reset/token"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectNode2.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    log.debug("Test: user info for password reset");
    log.debug(result2.getContentAsString());
    User verifiedUser = userRepository.findByEmail(EMAIL);
    log.debug("isverified" + verifiedUser.isVerified());

    // should be verified after password reset
    assertTrue(verifiedUser.isVerified());

    // should still be the same
    assertEquals(verifiedUser.getFirstName(), FIRSTNAME);
  }
}
