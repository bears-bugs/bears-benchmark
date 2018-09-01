package edu.harvard.h2ms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.harvard.h2ms.domain.core.Reader;

public interface ReaderRepository extends PagingAndSortingRepository<Reader, Long> {

}
