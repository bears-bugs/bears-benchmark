package hu.oe.nik.szfmv.automatedcar.steering;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.SteeringMethods;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SteerAngleTest extends AutomatedCar {

    public SteerAngleTest() {
        super(0, 0, null);
    }

    @Test
    public void noSteeringTest() {
        Double angle = null;
        try {
            angle = SteeringMethods.getSteerAngle(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertThat((int) Math.abs(angle), is(0));
        }
    }

    @Test
    public void fullSteeringTest() {
        Double angle = null;
        try {
            angle = SteeringMethods.getSteerAngle(-100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //assertEquals(new Double(-60), angle);
        assertThat(angle, is(-Math.toRadians(-60)));


        try {
            angle = SteeringMethods.getSteerAngle(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //assertEquals(new Double(60), angle);
        assertThat(angle, is(-Math.toRadians(60)));

    }

    @Test
    public void loopSteeringTest() {
        for (int n = -100; n <= 100; n += 5) {
            Double angle = null;
            try {
                angle = SteeringMethods.getSteerAngle(n);
            } catch (Exception e) {
                e.printStackTrace();
            }

            assertThat(angle, is(-Math.toRadians(n * 0.6)));
        }
    }

    @Test
    public void invalidNegativeSteeringTest() {
        Boolean thrown = false;
        try {
            Double angle = SteeringMethods.getSteerAngle(-100.1);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void invalidPositiveSteeringTest() {
        Boolean thrown = false;
        try {
            Double angle = SteeringMethods.getSteerAngle(+100.1);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

}
