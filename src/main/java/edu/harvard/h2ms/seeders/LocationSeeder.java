package edu.harvard.h2ms.seeders;

import edu.harvard.h2ms.domain.core.Location;
import edu.harvard.h2ms.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LocationSeeder {

  private LocationRepository locationRepository;

  @Autowired
  public LocationSeeder(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
  }

  @EventListener
  public void seed(ContextRefreshedEvent event) {
    seedLocationTable();
  }

  private void seedLocationTable() {
    Location hospital = new Location();
    hospital.setType("Hospital");
    hospital.setName("Massachusetts General Hospital");
    hospital.setCountry("USA");
    hospital.setAddress("55 Fruit Street Boston, MA");
    hospital.setZip("02114");
    locationRepository.save(hospital);

    Location clinic = new Location();
    clinic.setType("Clinic");
    clinic.setName("Massachusetts Health Clinic");
    clinic.setCountry("USA");
    clinic.setAddress("123 Anywhere St, Boston, MA");
    clinic.setZip("02114");
    locationRepository.save(clinic);

    // Wards
    Location ward1 = new Location();
    ward1.setType("Ward");
    ward1.setName("Emergency Room");
    ward1.setCountry("USA");
    ward1.setAddress("55 Fruit Street Boston, MA");
    ward1.setZip("02114");
    ward1.setParent(hospital);
    locationRepository.save(ward1);

    Location ward2 = new Location();
    ward2.setType("Ward");
    ward2.setName("Oncology");
    ward2.setCountry("USA");
    ward2.setAddress("55 Fruit Street Boston, MA");
    ward2.setZip("02114");
    ward2.setParent(hospital);
    locationRepository.save(ward2);

    // Rooms
    Location ward1room1 = new Location();
    ward1room1.setType("Patient Room");
    ward1room1.setName("Room 1");
    ward1room1.setCountry("USA");
    ward1room1.setAddress("55 Fruit Street Boston, MA");
    ward1room1.setZip("02114");
    ward1room1.setParent(ward1);
    locationRepository.save(ward1room1);

    Location ward1room2 = new Location();
    ward1room2.setType("Patient Room");
    ward1room2.setName("Room 2");
    ward1room2.setCountry("USA");
    ward1room2.setAddress("55 Fruit Street Boston, MA");
    ward1room2.setZip("02114");
    ward1room2.setParent(ward1);
    locationRepository.save(ward1room2);
  }
}
