package edu.harvard.h2ms.service;

import java.util.List;
import java.util.Map;

import edu.harvard.h2ms.domain.core.Event;
import org.json.JSONObject;

/**
 * Event Service ...
 */
public interface EventService {

	/**
	 * Retrieves all events in H2MS systems and returns results
	 * grouped by a specified timeframe (ie. week, month, year, quarter)
	 * @param timeframe - week, month, year, quarter
	 * @return
	 */
	Map<String, Long> findEventCountByTimeframe(String timeframe);

}
