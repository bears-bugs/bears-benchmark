package edu.harvard.h2ms.repository;

import edu.harvard.h2ms.domain.core.EventTemplate;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventTemplateRepository extends PagingAndSortingRepository<EventTemplate, Long> {
  EventTemplate findByName(String name);
}
