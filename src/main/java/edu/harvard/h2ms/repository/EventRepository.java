package edu.harvard.h2ms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.harvard.h2ms.domain.core.Event;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

}