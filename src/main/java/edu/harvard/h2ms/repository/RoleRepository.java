package edu.harvard.h2ms.repository;

import edu.harvard.h2ms.domain.core.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {}
