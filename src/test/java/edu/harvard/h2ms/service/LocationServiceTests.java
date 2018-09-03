package edu.harvard.h2ms.service;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import edu.harvard.h2ms.domain.core.Location;
import edu.harvard.h2ms.repository.LocationRepository;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LocationServiceTests {
  @Autowired private TestEntityManager entityManager;

  @Autowired private LocationRepository locationRepository;

  private Location hospitalLocation, wardLocation;

  @Before
  public void setUp() {
    hospitalLocation = new Location();
    hospitalLocation.setName("Massachusetts General Hospital");
    hospitalLocation.setType("Hospital");
    hospitalLocation.setCountry("USA");
    hospitalLocation.setAddress("55 Fruit Street Boston, MA");
    hospitalLocation.setZip("02114");
    entityManager.persist(hospitalLocation);
    entityManager.flush();

    wardLocation = new Location();
    wardLocation.setName("MA General Ward");
    wardLocation.setType("Ward");
    wardLocation.setCountry("USA");
    wardLocation.setAddress("52 Fruit Street Boston, MA");
    wardLocation.setZip("02114");
    wardLocation.setParent(locationRepository.findByName("Massachusetts General Hospital"));

    entityManager.persist(wardLocation);
    entityManager.flush();
  }

  /** Inserts a Ward Location linked to a Hospital and reads it via find by name api */
  @Test
  public void whenFindByName_thenReturnLocation() {
    Location found = locationRepository.findByName(wardLocation.getName());
    assertThat(found, is(wardLocation));
    assertThat(found.getParent(), is(hospitalLocation));
  }

  @Test
  /** Tests that location service findTopLevel only returns top level entities. */
  public void whenFindTopLevel_thenReturnsTopLevelOnly() {
    Set<Location> topLevel = locationRepository.findTopLevel();
    assertThat(topLevel, contains(hospitalLocation));
    assertThat(topLevel, not(contains(wardLocation)));
  }
}
