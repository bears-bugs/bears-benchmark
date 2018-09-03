package edu.harvard.h2ms.validator;

import static java.util.Arrays.asList;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.harvard.h2ms.domain.core.Answer;
import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.domain.core.Question;

@Component("beforeCreateEventValidator")
public class EventValidator implements Validator {

	@Override
	public boolean supports(Class<?> klass) {
		return Event.class.equals(klass);
	}

	@Override
	public void validate(Object object, Errors errors) {
		Event event = (Event) object;
		
		System.out.println(event.toString());
		
		if(event.getSubject() == null) {
			errors.rejectValue("subject", "Event.RequiredSubject");
		}
		
		if(event.getObserver() == null) {
			errors.rejectValue("observer", "Event.RequiredObserver");
		}
		
		if(event.getTimestamp() == null) {
			errors.rejectValue("timestamp", "Event.RequiredTimestamp");
		}
		
		if(event.getEventTemplate() == null) {
			errors.rejectValue("eventTemplate", "Event.RequiredTemplate");
		} else {
			Set<Question> questions = event.getEventTemplate().getQuestions();
			Set<Question> required = questions.stream().filter(q->q.getRequired()).collect(Collectors.toSet());

			
			int idx = 0;
			for(Answer answer : event.getAnswers()) {
				// User gave an answer to a question not in this template:
				if(!questions.contains(answer.getQuestion())) {
					errors.rejectValue(String.format("answers[%d].value", idx), "Answer.NotInTemplate");
				}
					
				/* Validate answer types are correct */
				switch(answer.getAnswerType()) {
				case "options":
					if(!answer.getQuestion().getOptions().contains(answer.getValue())) {
						errors.rejectValue(String.format("answers[%d].value", idx), "Answer.NotInOptions");
					}
					break;
				case "boolean":
					if(!asList("true", "false").contains(answer.getValue())) {
						errors.rejectValue(String.format("answers[%d].value", idx), "Answer.NotBoolean");
					}
					break;
				}
				
				// We saw the answer, remove it from the required list
				required.remove(answer.getQuestion());
				
				idx++;
			}

			if(!required.isEmpty()) {
				errors.reject("Event.RequiredAnswer");
			}
		}
	}
}
