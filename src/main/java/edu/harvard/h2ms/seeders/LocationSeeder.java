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
    public LocationSeeder(LocationRepository locationRepository)
    {
        this.locationRepository = locationRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedLocationTable();
    }

    private void seedLocationTable() {
        Location location = new Location();
        location.setType("Hospital");
        location.setName("Massachusetts General Hospital");
        location.setCountry("USA");
        location.setAddress("55 Fruit Street Boston, MA");
        location.setZip("02114");
        locationRepository.save(location);
    }

}
