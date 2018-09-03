package edu.harvard.h2ms.service;

import java.util.List;
import java.util.Map;

import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.domain.core.Question;
import edu.harvard.h2ms.exception.InvalidAnswerTypeException;
import edu.harvard.h2ms.exception.InvalidTimeframeException;
import edu.harvard.h2ms.exception.ResourceNotFoundException;

/**
 * Event Service ...
 */
public interface EventService {
	
	/**
	 * Retrieves a list of events for a particular boolean question.  Used to retrieve
	 * all events for a compliance end point to do further processing on.
	 * 
	 * @param question  	Question to lookup events for
	 * @return				List<Event> of all events with an answer to this question
	 * @throws InvalidAnswerTypeException	When question is not boolean
	 */
	public List<Event> findEventsForCompliance(Question question) throws InvalidAnswerTypeException;
	
	/**
	 * Retrieves all events in H2MS systems and returns results
	 * grouped by a specified time frame (i.e. week, month, year, quarter)
	 * @param timeframe - week, month, year, quarter
	 * @return Event count by time frame
	 * @throws InvalidTimeframeException 
	 */
	Map<String, Long> findEventCountByTimeframe(String timeframe) throws InvalidTimeframeException;

    /**
	 * Retrieves compliance and returns results grouped by a specified time frame
	 * (i.e. week, month, year, quarter)
	 *
	 * @param timeframe
	 *            - week, month, year, quarter
	 * @param events
	 *            - List of events to process
	 * @return Compliance by time frame
	 * @throws InvalidTimeframeException
	 */
	Map<String, Double> findComplianceByTimeframe(String timeframe, Question question, List<Event> events) throws InvalidTimeframeException;
}
