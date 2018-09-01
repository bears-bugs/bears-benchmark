package edu.harvard.h2ms.seeders;

import static java.util.Arrays.asList;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;

import edu.harvard.h2ms.domain.core.EventTemplate;
import edu.harvard.h2ms.domain.core.Question;
import edu.harvard.h2ms.repository.EventTemplateRepository;
import edu.harvard.h2ms.repository.QuestionRepository;

/**
 * HandwashEventTemplateSeeder.  This seeds the basic template for a hand washing event.
 *
 * @author stbenjam
 */
@Component
public class HandwashEventTemplateSeeder {
    private EventTemplateRepository eventTemplateRepository;
    private QuestionRepository questionRepository;

    @Autowired
    public HandwashEventTemplateSeeder(EventTemplateRepository eventTemplateRepository, QuestionRepository questionRepository)
    {
        this.eventTemplateRepository = eventTemplateRepository;
        this.questionRepository = questionRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
    	EventTemplate template = new EventTemplate("Handwashing Event");
    	eventTemplateRepository.save(template);

    	Set<Question> questions = ImmutableSet.<Question> of (
    			new Question(
    					"Relative moment",
    					"options",
    					asList("Room Entry", "Room Exit"),
    					true,
    					1,
    					template
    					),
    			
    			new Question(
    					"Opportunity to wash?",
    					"boolean",
    					null,
    					true,
    					2,
    					template
    					),

    			new Question(
    					"Washed?",
    					"boolean",
    					null,
    					true,
    					3,
    					template
    					),
    			
    			new Question(
    					"Handwashing Method",
    					"options",
    					asList("Soap and Water", "Alcohol"),
    					false,
    					4,
    					template
    					)
    			);

    	questionRepository.save(questions);
    }
}
