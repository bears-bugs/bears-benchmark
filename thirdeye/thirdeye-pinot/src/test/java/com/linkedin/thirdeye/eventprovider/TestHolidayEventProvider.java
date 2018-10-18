package com.linkedin.thirdeye.eventprovider;

import com.google.common.collect.Lists;
import com.linkedin.thirdeye.anomaly.events.HolidayEventProvider;
import com.linkedin.thirdeye.anomaly.events.EventFilter;
import com.linkedin.thirdeye.anomaly.events.EventType;
import com.linkedin.thirdeye.datalayer.bao.DAOTestBase;
import com.linkedin.thirdeye.datalayer.bao.EventManager;
import com.linkedin.thirdeye.datalayer.dto.EventDTO;

import com.linkedin.thirdeye.datasource.DAORegistry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
public class TestHolidayEventProvider {



  public class TestEventManager {
    long testEventId;
    HolidayEventProvider holidayEventProvider = null;
    long hoursAgo5 = new DateTime().minusHours(5).getMillis();
    long hoursAgo4 = new DateTime().minusHours(4).getMillis();
    long hoursAgo3 = new DateTime().minusHours(3).getMillis();
    long hoursAgo2 = new DateTime().minusHours(2).getMillis();

    private DAOTestBase testDAOProvider;
    private EventManager eventDAO;
    @BeforeClass
    void beforeClass() {
      testDAOProvider = DAOTestBase.getInstance();
      DAORegistry daoRegistry = DAORegistry.getInstance();
      eventDAO = daoRegistry.getEventDAO();
      holidayEventProvider = new HolidayEventProvider();
    }

    @AfterClass(alwaysRun = true)
    void afterClass() {
      testDAOProvider.cleanup();
    }

    @Test
    public void testGetEvents() {
      EventDTO event1 = new EventDTO();
      event1.setName("event1");
      event1.setEventType(EventType.DEPLOYMENT.toString());
      eventDAO.save(event1);

      EventDTO event2 = new EventDTO();
      event2.setName("event2");
      event2.setEventType(EventType.HOLIDAY.toString());
      event2.setStartTime(hoursAgo4);
      event2.setEndTime(hoursAgo3);
      Map<String, List<String>> eventDimensionMap2 = new HashMap<>();
      eventDimensionMap2.put("country_code", Lists.newArrayList("peru", "brazil"));
      eventDimensionMap2.put("BrowserName", Lists.newArrayList("chrome"));
      event2.setTargetDimensionMap(eventDimensionMap2);
      eventDAO.save(event2);

      EventDTO event3 = new EventDTO();
      event3.setName("event3");
      event3.setStartTime(hoursAgo3);
      event3.setEndTime(hoursAgo2);
      event3.setEventType(EventType.HOLIDAY.toString());
      Map<String, List<String>> eventDimensionMap3 = new HashMap<>();
      eventDimensionMap3.put("country_code", Lists.newArrayList("srilanka", "india"));
      event3.setTargetDimensionMap(eventDimensionMap3);
      eventDAO.save(event3);

      EventDTO event4 = new EventDTO();
      event4.setName("event4");
      event4.setStartTime(hoursAgo4);
      event4.setEndTime(hoursAgo3);
      event4.setEventType(EventType.HOLIDAY.toString());
      Map<String, List<String>> eventDimensionMap4 = new HashMap<>();
      eventDimensionMap4.put("country_code", Lists.newArrayList("srilanka", "india"));
      event4.setTargetDimensionMap(eventDimensionMap3);
      eventDAO.save(event4);

      Assert.assertEquals(eventDAO.findAll().size(), 4);

      // invalid time
      EventFilter eventFilter = new EventFilter();
      List<EventDTO> events = holidayEventProvider.getEvents(eventFilter);
      Assert.assertEquals(events.size(), 0);

      // check that it gets all HOLIDAY events in time range, and only HOLIDAY events
      eventFilter.setStartTime(hoursAgo5);
      eventFilter.setEndTime(hoursAgo3);
      events = holidayEventProvider.getEvents(eventFilter);
      Assert.assertEquals(events.size(), 2);

      // check for HOLIDAY events in time range and filters
      Map<String, List<String>> filterMap = new HashMap<>();
      filterMap.put("country_code", Lists.newArrayList("india"));
      eventFilter.setTargetDimensionMap(filterMap);
      events = holidayEventProvider.getEvents(eventFilter);
      Assert.assertEquals(events.size(), 1);
    }
  }
}
