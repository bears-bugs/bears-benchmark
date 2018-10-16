package org.traccar;

import java.util.Date;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.traccar.database.IdentityManager;
import org.traccar.model.Position;
import org.traccar.model.Device;

public class FilterHandlerTest extends BaseTest {

    private FilterHandler filtingHandler;
    private FilterHandler passingHandler;

    @Before
    public void setUp() {
        passingHandler = new FilterHandler();
        filtingHandler = new FilterHandler();
        filtingHandler.setFilterInvalid(true);
        filtingHandler.setFilterZero(true);
        filtingHandler.setFilterDuplicate(true);
        filtingHandler.setFilterApproximate(true);
        filtingHandler.setFilterStatic(true);
        filtingHandler.setFilterDistance(10);
        filtingHandler.setFilterLimit(10);
        filtingHandler.setFilterFuture(5 * 60);
    }

    @After
    public void tearDown() {
        filtingHandler = null;
        passingHandler = null;
    }

    private Position createPosition(
            long deviceId,
            Date time,
            boolean valid,
            double latitude,
            double longitude,
            double altitude,
            double speed,
            double course) {

        Position p = new Position();
        p.setDeviceId(deviceId);
        p.setTime(time);
        p.setValid(valid);
        p.setLatitude(latitude);
        p.setLongitude(longitude);
        p.setAltitude(altitude);
        p.setSpeed(speed);
        p.setCourse(course);
        return p;
    }

    @Test
    public void testFilterInvalid() throws Exception {

        Position position = createPosition(0, new Date(), true, 10, 10, 10, 10, 10);

        assertNotNull(filtingHandler.decode(null, null, position));
        assertNotNull(passingHandler.decode(null, null, position));

        position = createPosition(0, new Date(Long.MAX_VALUE), true, 10, 10, 10, 10, 10);

        assertNull(filtingHandler.decode(null, null, position));
        assertNotNull(passingHandler.decode(null, null, position));

        position = createPosition(0, new Date(), false, 10, 10, 10, 10, 10);

        assertNull(filtingHandler.decode(null, null, position));
        assertNotNull(passingHandler.decode(null, null, position));
    }

}
