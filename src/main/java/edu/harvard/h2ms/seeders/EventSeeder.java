package edu.harvard.h2ms.seeders;

import edu.harvard.h2ms.domain.core.*;
import edu.harvard.h2ms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class EventSeeder {

    private EventRepository eventRepository;
    private AnswerRepository answerRepository;
    private UserRepository userRepository;
    private EventTemplateRepository eventTemplateRepository;
    private QuestionRepository questionRepository;

    @Autowired
    public EventSeeder(EventRepository eventRepository, AnswerRepository answerRepository, UserRepository userRepository,
                       EventTemplateRepository eventTemplateRepository, QuestionRepository questionRepository) {
        this.eventRepository = eventRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.eventTemplateRepository = eventTemplateRepository;
        this.questionRepository = questionRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedEventTable();
    }

    private void seedEventTable() {

        // Event Template must be persisted prior to Event persistence
        EventTemplate eventTemplate = new EventTemplate();
        eventTemplate.setName("Event_Template_1");
        eventTemplateRepository.save(eventTemplate);

        // Question must be persisted prior to Answer persistence
        Question question = new Question();
        question.setAnswerType("testvalue_answer");
        question.setRequired(Boolean.TRUE);
        question.setOptions(Arrays.asList("Option1", "Option2"));
        question.setQuestion("What is ... ?");
        question.setPriority(1);
        question.setEventTemplate(eventTemplate);
        questionRepository.save(question);
        Set<Question> questions = new HashSet<>();

        // Answer must be persisted prior to Event persistence
        Answer answer = new Answer();
        answer.setAnswerType("testvalue_answer");
        answer.setValue("testvalue_value");
        answerRepository.save(answer);

        // User must be persisted prior to Event persistence
        User user = new User();
        user.setEmail("sample@h2ms.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("A");
        user.setNotificationFrequency("1");
        userRepository.save(user);

        // Creates and persists event
        Event event = new Event();
        Set<Answer> answers = new HashSet<>();
        event.setAnswers(answers);
        event.setLocation("testvalue_location");
        event.setSubject(user);
        event.setEventTemplate(eventTemplate);
        event.setObserver(user);
        event.setTimestamp(new Date(System.currentTimeMillis()));
        eventRepository.save(event);
    }

}
