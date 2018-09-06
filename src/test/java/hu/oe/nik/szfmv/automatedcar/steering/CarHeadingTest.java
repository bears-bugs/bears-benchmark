package hu.oe.nik.szfmv.automatedcar.steering;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.SteeringMethods;
import org.junit.Assert;
import org.junit.Test;

import java.awt.geom.Point2D;

public class CarHeadingTest extends AutomatedCar {
    final double THRESHOLD = 0.0001d;

    public CarHeadingTest() {
        super(0, 0, null);
    }

    @Test
    public void FacingEastTest() {

        double expectedDegs = 0;
        double heading = CalculateHeadingFromWheelsState(new Point2D.Double(50, 0),
                new Point2D.Double(-50, 0));

        Assert.assertEquals(expectedDegs, heading, THRESHOLD);
    }

    @Test
    public void FacingSouthTest() {

        double expectedDegs = 90;
        double heading = CalculateHeadingFromWheelsState(new Point2D.Double(0, 50),
                new Point2D.Double(-0, -50));

        Assert.assertEquals(expectedDegs, heading, THRESHOLD);
    }

    @Test
    public void FacingNorthTest() {

        double expectedDegs = 90;
        double heading = CalculateHeadingFromWheelsState(new Point2D.Double(40.001, 50),
                new Point2D.Double(40.001, 33.1));

        Assert.assertEquals(expectedDegs, heading, THRESHOLD);
    }

    @Test
    public void FacingWestTest() {

        double expectedDegs = 180;
        double heading = CalculateHeadingFromWheelsState(new Point2D.Double(40.001, 33.1),
                new Point2D.Double(50, 33.1));

        Assert.assertEquals(expectedDegs, heading, THRESHOLD);
    }

    @Test
    public void FacingSouthWestTest() {
        double expectedDegs = 135;
        //calc'd using http://www.cleavebooks.co.uk/scol/calrtri.htm
        double heading = CalculateHeadingFromWheelsState(new Point2D.Double(-35.3553, 35.3553),
                new Point2D.Double(35.3553, -35.3553));

        Assert.assertEquals(expectedDegs, heading, THRESHOLD);
    }

    @Test
    public void FacingSouthEastTest() {

        double expectedDegs = -45;
        double heading = CalculateHeadingFromWheelsState(new Point2D.Double(35.3553, -35.3553),
                new Point2D.Double(-35.3553, 35.3553));

        Assert.assertEquals(expectedDegs, heading, THRESHOLD);

    }

    private double CalculateHeadingFromWheelsState(Point2D.Double frontWheel, Point2D.Double backWheel) {

        return Math.toDegrees(SteeringMethods.getCarHeading(frontWheel, backWheel));


    }
}
