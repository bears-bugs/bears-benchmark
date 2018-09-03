package edu.harvard.h2ms.seeders;

import static java.util.Arrays.asList;

import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder {
    private UserRepository userRepository;

    @Autowired
    public UserSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUserTable();
    }

    private void seedUserTable() {

        if (userRepository.count() == 0) {
            List<List<String>> records = asList(asList("Default", "User", "admin@h2ms.org"));

            for (List<String> record : records) {
                String firstName = record.get(0);
                String lastName = record.get(1);
                String email = record.get(2);

                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPassword("password");
                user.setType("Admin");
                userRepository.save(user);
            }
        }
    }
}
