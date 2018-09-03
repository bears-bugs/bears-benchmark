package edu.harvard.h2ms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.harvard.h2ms.domain.core.User;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	User findOneByEmail(String email);
	User findByFirstName( String firstName);
	User findByMiddleName( String middleName);
	User findByLastName( String lastName);
	User findByEmail( String email);
	User findByResetToken(String resetToken);

	
}
