package edu.harvard.h2ms.domain.core;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import edu.harvard.h2ms.repository.UserRepository;
import javax.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTest {
    @Autowired private TestEntityManager entityManager;

    @Autowired private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFirstNameRequired() {
        User user = new User(null, "Quincy", "Adams", "jqadams@h2ms.org", "password", "Other");
        entityManager.persist(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testLastNameRequired() {
        User user = new User("John", "Quincy", null, "jqadams@h2ms.org", "password", "Other");
        entityManager.persist(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmailRequired() {
        User user = new User("John", "Quincy", null, null, "password", "Other");
        entityManager.persist(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testPasswordRequired() {
        User user = new User("John", "Quincy", "Adams", "jqadams@h2ms.org", null, "Other");
        entityManager.persist(user);
    }

    @Test
    public void testPasswordIsHashed() {
        User user = new User("John", "Quincy", "Adams", "jqadams@h2ms.org", "password", "Other");
        entityManager.persist(user);

        User found = userRepository.findByEmail("jqadams@h2ms.org");

        // Password is not cleartext
        assertThat(user.getPassword(), is(not("password")));

        // Hashed password is "password"
        assertThat(passwordEncoder.matches("password", user.getPassword()), is(true));
    }
}
