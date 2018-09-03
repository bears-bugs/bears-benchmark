package edu.harvard.h2ms.repository;

import edu.harvard.h2ms.domain.core.WristBand;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WristBandRepository extends PagingAndSortingRepository<WristBand, Long> {}
