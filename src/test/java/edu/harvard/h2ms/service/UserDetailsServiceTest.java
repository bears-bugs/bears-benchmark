package edu.harvard.h2ms.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.UserRepository;


@RunWith(SpringRunner.class)
public class UserDetailsServiceTest {
	private User user;
	
	@TestConfiguration
	static class UserDetailsServiceContextConfiguration {
		@Bean
		public UserDetailsService userDetailsService() {
			return new UserDetailsServiceImpl();
		}
	}
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@MockBean
	private UserRepository userRepository;
	
	@Before
	public void setUp() {
		user = new User("John", "Quincy", "Adams", "jqadams@h2ms.org", "password", "Other");
		Mockito.when(userRepository.findOneByEmail("jqadams@h2ms.org")).thenReturn(user);
	}
	
	@Test
	@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
	public void testUsernameIsEmail() {
		assertThat(
				userDetailsService.loadUserByUsername("jqadams@h2ms.org"),
				is(user)
				);
	}
	
	@Test
	@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
	public void testLoadUserByUsernameIsCaseInsensitive() {
		assertThat(
				userDetailsService.loadUserByUsername("JqAdAmS@h2Ms.oRg"),
				is(user)
				);
	}
	
	@Test(expected=UsernameNotFoundException.class)
	public void testNonExistentUserThrows() {
		userDetailsService.loadUserByUsername("doesntexist@example.com");
	}
	

}
