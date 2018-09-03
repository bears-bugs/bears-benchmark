package edu.harvard.h2ms.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.domain.core.Question;
import edu.harvard.h2ms.exception.InvalidAnswerTypeException;
import edu.harvard.h2ms.exception.InvalidTimeframeException;
import edu.harvard.h2ms.repository.QuestionRepository;
import edu.harvard.h2ms.service.EventService;
import edu.harvard.h2ms.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/users")
public class UserController {

	final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;

	@Autowired
	private QuestionRepository questionRepository;
	
	/**
	 * Rest end point for retrieving the compliance to a
	 * particular question.  Compliance is calculated as
	 * the percent of "true" values compared to the total population
	 * of answers.
	 * 
	 * Ex. /users/compliance/3
	 * 
	 * @return
	 */
	@RequestMapping(value = "/compliance/{questionId}", method = RequestMethod.GET)
	public ResponseEntity<?> findCompliance(@PathVariable Long questionId) {		
		List<Event> events;
		Question question = questionRepository.findOne(questionId);
		
		if(question == null) {
			return new ResponseEntity<String>("Question not found.", HttpStatus.NOT_FOUND);
		}
		
		try {	
			events = eventService.findEventsForCompliance(question);
			return new ResponseEntity<Map<String, Double>>(
					userService.findComplianceByUserType(question, events), HttpStatus.OK);
		} catch (InvalidAnswerTypeException answerType) {
			return new ResponseEntity<String>(answerType.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
