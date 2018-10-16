package org.traccar.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.traccar.BaseTest;
import org.traccar.model.Event;
import org.traccar.model.Position;

public class CommandResultEventHandlerTest extends BaseTest {

    @Test
    public void testCommandResultEventHandler() throws Exception {
        
        CommandResultEventHandler commandResultEventHandler = new CommandResultEventHandler();
        
        Position position = new Position();
        position.set(Position.KEY_RESULT, "Test Result");
        Collection<Event> events = commandResultEventHandler.analyzePosition(position);
        assertNotNull(events);
        Event event = (Event) events.toArray()[0];
        assertEquals(Event.TYPE_COMMAND_RESULT, event.getType());
    }

}
