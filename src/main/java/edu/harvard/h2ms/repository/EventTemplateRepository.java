package edu.harvard.h2ms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.harvard.h2ms.domain.core.EventTemplate;

public interface EventTemplateRepository extends PagingAndSortingRepository<EventTemplate, Long> {

}
