package edu.harvard.h2ms.seeders;

import edu.harvard.h2ms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventSeeder {

    private EventRepository eventRepository;

    @Autowired
    public EventSeeder(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedEventTable();
    }

    private void seedEventTable() {
        //.. place holder
    }
}