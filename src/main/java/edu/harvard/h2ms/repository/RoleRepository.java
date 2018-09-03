package edu.harvard.h2ms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.harvard.h2ms.domain.core.Role;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
	
}
