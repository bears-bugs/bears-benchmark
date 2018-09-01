package edu.harvard.h2ms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.harvard.h2ms.domain.core.WristBand;

public interface WristBandRepository extends PagingAndSortingRepository<WristBand, Long> {

}
