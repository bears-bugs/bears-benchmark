package edu.harvard.h2ms.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.domain.core.EventTemplate;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {
	List<Event> findByEventTemplate(EventTemplate eventTemplate);
}