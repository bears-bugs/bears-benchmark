package edu.harvard.h2ms.service;

import com.google.common.collect.Lists;
import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.repository.EventRepository;
import edu.harvard.h2ms.service.utils.H2msRestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service("eventService")
@Repository
@Transactional
public class EventServiceImpl implements EventService {

    final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private EventRepository eventRepository;

    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieves all events from the H2MS system, extracts the timestamps and parses them,
     * returns count per distinctly parsed timestamp
     * Ex. If system has 1 event with timestamp "2018-03-21T17:58:05.742+0000",
     *     results returned is {"March (2018)":1}
     * @param timeframe - week, month, year, quarter
     * @return
     */
    @Transactional(readOnly=true)
    public Map<String, Long> findEventCountByTimeframe(String timeframe) {

        List<Event> events = Lists.newArrayList(eventRepository.findAll());
        log.info("No. of events found: {}", events.size());

        List<String> parsedTimestamps = H2msRestUtils.extractParsedTimestamps(events, timeframe);
        log.info("Parsed {} timestamps by {}", parsedTimestamps.size(), timeframe);

        return H2msRestUtils.frequencyCounter(parsedTimestamps);
    }

}
