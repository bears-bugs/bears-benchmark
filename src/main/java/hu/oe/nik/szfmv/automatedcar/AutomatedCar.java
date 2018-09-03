package hu.oe.nik.szfmv.automatedcar;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.exception.MissingPacketException;
import hu.oe.nik.szfmv.automatedcar.bus.packets.car.CarPacket;
import hu.oe.nik.szfmv.automatedcar.bus.packets.input.ReadOnlyInputPacket;
import hu.oe.nik.szfmv.automatedcar.bus.powertrain.ReadOnlyPowertrainPacket;
import hu.oe.nik.szfmv.automatedcar.sensors.UltrasonicSensor;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.*;
import hu.oe.nik.szfmv.environment.WorldObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class AutomatedCar extends WorldObject {

    private static final Logger LOGGER = LogManager.getLogger(AutomatedCar.class);
    private final VirtualFunctionBus virtualFunctionBus = new VirtualFunctionBus();
    private double wheelBase;
    private double halfWidth;
    private PowertrainSystem powertrainSystem;
    private SteeringSystem steeringSystem;
    private SteeringWheel steeringWheel;
    private final List<UltrasonicSensor> ultrasonicSensors = new ArrayList<>();

    /**
     * Constructor of the AutomatedCar class
     *
     * @param x             the initial x coordinate of the car
     * @param y             the initial y coordinate of the car
     * @param imageFileName name of the image file used displaying the car on the course display
     */

    public AutomatedCar(int x, int y, String imageFileName) {
        super(x, y, imageFileName);

        final int carTestX = 200;
        final int carTestY = 200;
        final int fullCircle = 360;
        final int carTestRotation = 90;
        final int carWidth = 108;
        final int carHeight = 240;

        setLocation(new Point(carTestX, carTestY));
        setRotation(Math.toRadians(fullCircle - carTestRotation));
        wheelBase = carHeight;
        halfWidth = carWidth / 2;
        this.setWidth(carWidth);
        this.setHeight(carHeight);

        generateShape();

        virtualFunctionBus.carPacket = new CarPacket(this.getX(), this.getY(), this.getRotation());
        new GasBrake(virtualFunctionBus);
        new Index(virtualFunctionBus);
        new GearShift(virtualFunctionBus);
        new SensorsVisualizer(virtualFunctionBus);
        powertrainSystem = new PowertrainSystem(virtualFunctionBus);
        steeringSystem = new SteeringSystem(virtualFunctionBus);
        steeringWheel = new SteeringWheel(virtualFunctionBus);

        new Driver(virtualFunctionBus);
    }


    /**
     * Provides a sample method for modifying the position of the car.
     */
    public void drive() {
        try {
            virtualFunctionBus.loop();
            calculatePositionAndOrientation();
            generateShape();
        } catch (MissingPacketException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Calculates the new x and y coordinates of the {@link AutomatedCar} using the powertrain and the steering systems.
     */
    private void calculatePositionAndOrientation() {

        final double testSpeed = virtualFunctionBus.powertrainPacket.getSpeed();
        double angularSpeed = 0;
        final double fps = 1;
        final int threeQuarterCircle = 270;
        try {
            angularSpeed = SteeringMethods.getSteerAngle(-this.getInputValues().getSteeringWheelPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
        double carHeading = Math.toRadians(threeQuarterCircle) - rotation;
        double halfWheelBase = wheelBase / 2;

        Point2D carPosition = new Point2D.Double(getCarValues().getX() + halfWidth,
                getCarValues().getY() + halfWheelBase);
        Point2D frontWheel = SteeringMethods.getFrontWheel(carHeading, halfWheelBase, carPosition);
        Point2D backWheel = SteeringMethods.getBackWheel(carHeading, halfWheelBase, carPosition);

        Point2D backWheelDisplacement = SteeringMethods.getBackWheelDisplacement(carHeading, testSpeed, fps);
        Point2D frontWheelDisplacement =
                SteeringMethods.getFrontWheelDisplacement(carHeading, angularSpeed, testSpeed, fps);

        frontWheel = SteeringMethods.getNewFrontWheelPosition(frontWheel, frontWheelDisplacement);
        backWheel = SteeringMethods.getNewBackWheelPosition(backWheel, backWheelDisplacement);

        carPosition = SteeringMethods.getCarPosition(frontWheel, backWheel);
        carHeading = SteeringMethods.getCarHeading(frontWheel, backWheel);

        this.setX((int) (carPosition.getX() - halfWidth));
        this.setY((int) (carPosition.getY() - halfWheelBase));
        rotation = Math.toRadians(threeQuarterCircle) - carHeading;

        getCarValues().setX((int) (this.getX()));
        getCarValues().setY((int) (this.getY()));
        getCarValues().setRotation(this.getRotation());
    }

    /**
     * Gets the input values as required by the dashboard.
     *
     * @return input packet containing the values that are displayed on the dashboard
     */
    public ReadOnlyInputPacket getInputValues() {
        return virtualFunctionBus.inputPacket;
    }


    /**
     * Gets the car values which needs to change the car position
     *
     * @return car packet
     */
    public CarPacket getCarValues() {
        return virtualFunctionBus.carPacket;
    }

    /**
     * Gets the powertrain values as required by the dashboard.
     *
     * @return powertrain packet containing the values that are displayed on the dashboard
     */
    public ReadOnlyPowertrainPacket getPowertrainValues() {
        return virtualFunctionBus.powertrainPacket;
    }

    /**
     * Gets the list of ultrasonic sensors
     * @return the list of ultrasonic sensors
     */
    public List<UltrasonicSensor> getUltrasonicSensors() {
        return ultrasonicSensors;
    }
}