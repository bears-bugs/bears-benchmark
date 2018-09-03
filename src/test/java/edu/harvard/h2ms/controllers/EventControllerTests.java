package edu.harvard.h2ms.controllers;

import edu.harvard.h2ms.common.TestHelpers;
import edu.harvard.h2ms.domain.core.Answer;
import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.domain.core.Question;
import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.EventRepository;
import edu.harvard.h2ms.repository.EventTemplateRepository;
import edu.harvard.h2ms.repository.QuestionRepository;
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

import static java.util.Arrays.asList;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import static edu.harvard.h2ms.common.TestHelpers.obtainAccessToken;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class EventControllerTests {

    private static final Log log = LogFactory.getLog(EventControllerTests.class);

    static final String EMAIL = "jqadams@h2ms.org";
    static final String PASSWORD = "password";
    static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private Question booleanQuestion;
    private Question optionsQuestion;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    EventTemplateRepository eventTemplateRepository;

    @Autowired
    EventRepository eventRepository;

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
        booleanQuestion.setRequired(true);
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
        event.setTimestamp(new Date("28-MAR-2018"));
        
        eventRepository.save(event);
    }

    /**
     * Tests the success of the /count/week endpoint.
     * The endpoint is used for retrieving all events grouped by a
     * specified timeframe (i.e. week, month, year, quarter)
     */
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void test_Success_EventController_findEventCountByTimeframe() throws Exception {

        final String accessToken = obtainAccessToken(mvc, "jqadams@h2ms.org", "password");

        // Makes API calls and checks for success status
        MockHttpServletResponse result = mvc.perform(get("/events/count/week")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        
        assertThat(result.getContentAsString(), containsString("13th (2018)"));
    }

    /**
     * Tests the success of the /compliance/{question}/{timeframe} endpoint.
     * The endpoint is used for retrieving compliance rate grouped by a
     * specified timeframe (i.e. week, month, year, quarter)
     */
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void test_Success_EventController_findComplianceByTimeframe() throws Exception {

        final String accessToken = obtainAccessToken(mvc, "jqadams@h2ms.org", "password");

        // Makes API calls and checks for success status
        MockHttpServletResponse result = mvc.perform(get(String.format("/events/compliance/%d/week", booleanQuestion.getId()))
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.debug(result.getContentAsString());
        
        Map<String, Double> mapResult = TestHelpers.getDoubleMap(result.getContentAsString());
        
        assertThat(mapResult.get("13th (2018)"), is(1.0));
    }
    
    /**
     * Tests the failure of the /compliance/{question}/{timeframe} endpoint
     * when a question is not found.
     */
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void test_NotFound_EventController_findComplianceByTimeframe() throws Exception {

        final String accessToken = obtainAccessToken(mvc, "jqadams@h2ms.org", "password");

        // Makes API calls and checks for success status
        mvc.perform(get("/events/compliance/0/week")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the failure of the /compliance/{question}/{timeframe} endpoint
     * when a question is not of boolean type.
     */
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void test_InvalidQuestionType_EventController_findComplianceByTimeframe() throws Exception {

        final String accessToken = obtainAccessToken(mvc, "jqadams@h2ms.org", "password");

        mvc.perform(get(String.format("/events/compliance/%d/bad_timeframe", optionsQuestion.getId()))
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    /**
     * Tests the failure of the /compliance/{question}/{timeframe} endpoint
     * when a timeframe is invalid.
     */
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void test_InvalidTimeframe_EventController_findComplianceByTimeframe() throws Exception {

        final String accessToken = obtainAccessToken(mvc, "jqadams@h2ms.org", "password");

        mvc.perform(get(String.format("/events/compliance/%d/bad_timeframe", booleanQuestion.getId()))
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
