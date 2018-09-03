package edu.harvard.h2ms.repository;

import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.domain.core.EventTemplate;
import edu.harvard.h2ms.domain.core.User;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {
  List<Event> findByEventTemplate(EventTemplate eventTemplate);

  Long countByObserver(User observer);
}
