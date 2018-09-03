package edu.harvard.h2ms.service;

import edu.harvard.h2ms.domain.core.Location;
import edu.harvard.h2ms.repository.LocationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LocationServiceTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LocationRepository locationRepository;


    /**
     * Inserts a Ward Location linked to a Hospital and
     * reads it via find by name api
     */
    @Test
    public void whenFindByName_thenReturnLocation(){

        // Given
        Location hospitalLocation = new Location();
        hospitalLocation.setName("Massachusetts General Hospital");
        hospitalLocation.setType("Hospital");
        hospitalLocation.setCountry("USA");
        hospitalLocation.setAddress("55 Fruit Street Boston, MA");
        hospitalLocation.setZip("02114");
        entityManager.persist(hospitalLocation);
        entityManager.flush();

        Location wardLocation = new Location();
        wardLocation.setName("MA General Ward");
        wardLocation.setType("Ward");
        wardLocation.setCountry("USA");
        wardLocation.setAddress("52 Fruit Street Boston, MA");
        wardLocation.setZip("02114");
        wardLocation.setParent(locationRepository.findByName("Massachusetts General Hospital"));
        entityManager.persist(wardLocation);
        entityManager.flush();

        // When
        Location found = locationRepository.findByName(wardLocation.getName());

        // Then
        assertThat(found.getName()).isEqualTo(wardLocation.getName());
        assertThat(found.getParent().getName()).isEqualTo(wardLocation.getParent().getName());
    }

}