package edu.harvard.h2ms.controllers;

import edu.harvard.h2ms.H2MSRestAppInitializer;
import edu.harvard.h2ms.common.TestHelpers;
import edu.harvard.h2ms.domain.core.*;
import edu.harvard.h2ms.repository.EventRepository;
import edu.harvard.h2ms.repository.EventTemplateRepository;
import edu.harvard.h2ms.repository.QuestionRepository;
import edu.harvard.h2ms.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import static edu.harvard.h2ms.common.TestHelpers.obtainAccessToken;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class UserControllerTests {

    private static final Log log = LogFactory.getLog(UserControllerTests.class);

    static final String EMAIL = "jqadams@h2ms.org";
    static final String PASSWORD = "password";
    static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    EventTemplateRepository eventTemplateRepository;

    @Autowired
    EventRepository eventRepository;

    private Question booleanQuestion;
    private Question optionsQuestion;
    
    /**
     * Setup prior to running unit tests
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .build();

        // Sample User Data
        User observer = new User("John", "Quincy", "Adams", EMAIL, PASSWORD, "Other");
        userRepository.save(observer);
        User subject = new User("Jane", "Doe", "Sam", "sample@email.com", "password", "Doctor");
        userRepository.save(subject);

        // Creates and persists event
        Event event = new Event();
        Set<Answer> answers = new HashSet<>();
        Answer answer = new Answer();
        
        booleanQuestion = new Question();
        booleanQuestion.setPriority(1);
        booleanQuestion.setRequired(TRUE);
        booleanQuestion.setAnswerType("boolean");
        booleanQuestion.setQuestion("Washed?");
        booleanQuestion.setEventTemplate(eventTemplateRepository.findByName("Handwashing Event"));

        optionsQuestion = new Question();
        optionsQuestion.setPriority(2);
        optionsQuestion.setRequired(false);
        optionsQuestion.setAnswerType("options");
        optionsQuestion.setQuestion("Relative moment");
        optionsQuestion.setOptions(asList("entering", "exiting"));
        optionsQuestion.setEventTemplate(eventTemplateRepository.findByName("Handwashing Event"));
        
        answer.setQuestion(booleanQuestion);
        answer.setValue("true");
        answers.add(answer);
        event.setAnswers(answers);
        event.setLocation("Location_01");
        event.setSubject(subject);
        event.setObserver(observer);
        event.setEventTemplate(eventTemplateRepository.findByName("Handwashing Event"));
        event.setObserver(observer);
        event.setTimestamp(new Date(System.currentTimeMillis()));
        eventRepository.save(event);
    }

    /**
     * Tests the success of the /users/compliance/{question_id} endpoint. The endpoint
     * is used to retrieve the compliance value for a particular question
     */
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void test_Success_UserController_findUserCompliance() throws Exception {

        final String accessToken = obtainAccessToken(mvc, "jqadams@h2ms.org", "password");

        // Makes API calls and checks for success status
         MockHttpServletResponse result = mvc.perform(get(String.format("/users/compliance/%d", booleanQuestion.getId()))
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();


        Map<String, Double> mapResult = TestHelpers.getDoubleMap(result.getContentAsString());

        assertThat(mapResult.get("Doctor"), is(1.0));
        assertThat(mapResult.get("Other"), is(0.0));
    }
    
    /**
     * Tests the failure of the /users/compliance/{question_id} endpoint when a question
     * isn't found.
     */
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void test_NotFound_UserController_findUserCompliance() throws Exception {

        final String accessToken = obtainAccessToken(mvc, "jqadams@h2ms.org", "password");

        mvc.perform(get("/users/compliance/0")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    
    /**
     * Tests the failure of the /users/compliance/{question_id} endpoint when
     * compliance is generated for a non-boolean end point.
     */
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void test_BadRequest_UserController_findUserCompliance() throws Exception {

        final String accessToken = obtainAccessToken(mvc, "jqadams@h2ms.org", "password");

         mvc.perform(get(String.format("/users/compliance/%d", optionsQuestion.getId()))
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
