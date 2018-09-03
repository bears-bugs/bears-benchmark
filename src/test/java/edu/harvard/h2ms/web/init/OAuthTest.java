package edu.harvard.h2ms.web.init;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

/**
 * Tests that OAuth authentication works. Adapted from the tutorial at
 * http://www.baeldung.com/oauth-api-testing-with-spring-mvc.
 *
 * @author stbenjam
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class OAuthTest {
    static final String EMAIL = "jqadams@h2ms.org";
    static final String PASSWORD = "password";
    static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Autowired private WebApplicationContext wac;

    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private UserRepository userRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(this.wac)
                        .addFilter(springSecurityFilterChain)
                        .build();

        User user = new User("John", "Quincy", "Adams", EMAIL, PASSWORD, "Other");
        userRepository.save(user);
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        ResultActions result =
                mockMvc.perform(
                                post("/oauth/token")
                                        .params(params)
                                        .with(httpBasic("h2ms", "secret"))
                                        .accept("application/json"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(CONTENT_TYPE));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void testNoTokenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/events")).andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void testWithTokenIsOK() throws Exception {
        final String accessToken = obtainAccessToken("jqadams@h2ms.org", "password");

        System.out.println(accessToken);

        mockMvc.perform(
                        get("/events")
                                .header("Authorization", "Bearer " + accessToken)
                                .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }
}
