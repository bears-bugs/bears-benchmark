package edu.harvard.h2ms.web.controller;

import edu.harvard.h2ms.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(path="/events")
public class EventController {

    final Logger log = LoggerFactory.getLogger(EventController.class);

    private EventService eventService;

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Rest Endpoint for retrieving all events in H2MS systems and returns results
     * grouped by a specified timeframe (ie. week, month, year, quarter)
     * Ex. /events/countBy/week
     * @param timeframe - week, month, year, quarter
     * @return
     */
    @RequestMapping(value = "/count/{timeframe}", method = RequestMethod.GET)
    public Map<String, Long> findEventCountByTimeframe(@PathVariable String timeframe) {
        log.info("Searching for all events grouping by " + timeframe);
        return eventService.findEventCountByTimeframe(timeframe);
    }

}
