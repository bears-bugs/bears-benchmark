package edu.harvard.h2ms.web.controller;

import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.domain.core.Question;
import edu.harvard.h2ms.exception.InvalidAnswerTypeException;
import edu.harvard.h2ms.exception.InvalidTimeframeException;
import edu.harvard.h2ms.repository.QuestionRepository;
import edu.harvard.h2ms.service.EventService;
import edu.harvard.h2ms.service.ReportService;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/events")
public class EventController {

  final Logger log = LoggerFactory.getLogger(EventController.class);

  @Autowired private EventService eventService;

  @Autowired private QuestionRepository questionRepository;

  @Autowired private ReportService reportService;

  /**
   * Rest end point for retrieving all events in H2MS systems and returns results grouped by a
   * specified time frame (i.e. week, month, year, quarter)
   *
   * <p>Example: /events/count/week
   *
   * @param timeframe - week, month, year, quarter
   * @return ResponseEntity - 200 OK with JSON Map<String, Long> with results - 400 Bad Request on
   *     incorrect time frame
   */
  @RequestMapping(value = "/count/{timeframe}", method = RequestMethod.GET)
  public ResponseEntity<?> findEventCountByTimeframe(@PathVariable String timeframe) {
    log.info("Searching for all events grouping by " + timeframe);

    try {
      return new ResponseEntity<Map<String, Long>>(
          eventService.findEventCountByTimeframe(timeframe), HttpStatus.OK);
    } catch (InvalidTimeframeException e) {
      log.error(e.getMessage());
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Rest end point for retrieving all events in H2MS systems and returns results grouped by an
   * observer
   *
   * <p>Example: /events/count/observer
   *
   * @return ResponseEntity - 200 OK with JSON Map<String, Long> with results - 400 Bad Request
   */
  @RequestMapping(value = "/count/observer", method = RequestMethod.GET)
  public ResponseEntity<?> findEventCountByObserver() {
    log.info("Searching for all events grouping by observer");

    return new ResponseEntity<Map<String, Long>>(
        eventService.findEventCountByObserver(), HttpStatus.OK);
  }

  /**
   * Rest end point for getting compliance of a specific question grouped by the location.
   *
   * <p>Example: /events/compliance/19/location
   *
   * @param questionId - ID for Question
   * @return ResponseEntity - 200 OK with JSON Map<String, Double> with compliance results - 400 Bad
   *     Request on incorrect time frame - 404 Not Found if question not found
   */
  @RequestMapping(value = "/compliance/{questionId}/location", method = RequestMethod.GET)
  public ResponseEntity<?> findComplianceByLocation(@PathVariable Long questionId) {
    List<Event> events;
    Question question = questionRepository.findOne(questionId);

    if (question == null) {
      return new ResponseEntity<String>("Question not found.", HttpStatus.NOT_FOUND);
    }

    try {
      events = eventService.findEventsForCompliance(question);
      return new ResponseEntity<Map<String, Double>>(
          eventService.findComplianceByLocation(question, events), HttpStatus.OK);
    } catch (InvalidAnswerTypeException answerType) {
      return new ResponseEntity<String>(answerType.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Rest end point for getting compliance of a specific question grouped by a specified time frame
   * (i.e. week, month, year, quarter) Compliance is defined as percent of events with a boolean
   * question value to true.
   *
   * <p>Example: /events/compliance/19/week
   *
   * @param questionId - ID for Question
   * @param timeframe - week, month, year, quarter
   * @return ResponseEntity - 200 OK with JSON Map<String, Double> with compliance results - 400 Bad
   *     Request on incorrect time frame - 404 Not Found if question not found
   */
  @RequestMapping(value = "/compliance/{questionId}/{timeframe}", method = RequestMethod.GET)
  public ResponseEntity<?> findComplianceByTimeframe(
      @PathVariable String timeframe, @PathVariable Long questionId) {
    List<Event> events;
    Question question = questionRepository.findOne(questionId);

    if (question == null) {
      return new ResponseEntity<String>("Question not found.", HttpStatus.NOT_FOUND);
    }

    try {
      events = eventService.findEventsForCompliance(question);
      return new ResponseEntity<Map<String, Double>>(
          eventService.findComplianceByTimeframe(timeframe, question, events), HttpStatus.OK);
    } catch (InvalidAnswerTypeException answerType) {
      return new ResponseEntity<String>(answerType.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (InvalidTimeframeException timeFrame) {
      return new ResponseEntity<String>(timeFrame.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Event CSV dump
   *
   * @param response
   */
  @RequestMapping(value = "/export/csv", method = RequestMethod.GET)
  public ResponseEntity<?> exportEvent() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-Type", "text/csv; charset=utf-8");
    return new ResponseEntity<String>(
        reportService.createEventReport(), httpHeaders, HttpStatus.OK);
  }
}
