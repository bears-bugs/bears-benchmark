package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.exception.MissingPacketException;
import hu.oe.nik.szfmv.automatedcar.bus.powertrain.PowertrainPacket;
import hu.oe.nik.szfmv.automatedcar.input.enums.GearEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Powertrain system is responsible for the movement of the car.
 */
public class PowertrainSystem extends SystemComponent implements IPowertrainSystem {

    private static final Logger LOGGER = LogManager.getLogger(PowertrainSystem.class);
    private static final double WIND_RESISTANCE = 1.5;
    private static final double REFRESH_RATE = 40;          // 1 sec / 0.025 sec
    private static final int PERCENTAGE = 100;
    private static final double SLOWDOWN_DEGREE = 0.3;
    private static double speed;                            // Unit: m/s
    private static PowertrainPacket powertrainPacket;

    private GearEnum gearState;

    private int expectedRPM;
    private int actualRPM;
    private int gasPedalPosition;
    private int brakePedalPosition;
    private int shiftLevel;
    private double orientationVector;    // it is a unit vector which reflects the car's orientation

    /**
     * Creates a powertrain system that connects the Virtual Function Bus
     *
     * @param virtualFunctionBus {@link VirtualFunctionBus} used to connect {@link SystemComponent}s
     */
    public PowertrainSystem(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
        powertrainPacket = new PowertrainPacket();
        virtualFunctionBus.powertrainPacket = powertrainPacket;

        gearState = GearEnum.P;
        speed = 0;
        expectedRPM = CarSpecifications.IDLE_RPM;
        actualRPM = CarSpecifications.IDLE_RPM;
    }

    /**
     * Test constructor
     *
     * @param virtualFunctionBus VirtualFunctionBus
     * @param speed              speed
     */
    public PowertrainSystem(VirtualFunctionBus virtualFunctionBus, double speed) {
        super(virtualFunctionBus);
        powertrainPacket = new PowertrainPacket();
        virtualFunctionBus.powertrainPacket = powertrainPacket;

        gearState = virtualFunctionBus.samplePacket.getGearState();
        this.speed = speed;
        gasPedalPosition = virtualFunctionBus.samplePacket.getGaspedalPosition();
        brakePedalPosition = virtualFunctionBus.samplePacket.getBrakepedalPosition();

        expectedRPM = CarSpecifications.IDLE_RPM;
        actualRPM = CarSpecifications.IDLE_RPM;
    }

    /**
     * Calculates the speed difference considering the gas- and brake pedal position, actual shift level, actual RPM,
     * and gear ratios
     * Based on: http://www.asawicki.info/Mirror/Car%20Physics%20for%20Games/Car%20Physics%20for%20Games.html
     *
     * @return speed difference in m/s
     */
    private double calculateSpeedDifference() {
        boolean isAccelerate = actualRPM > expectedRPM;
        boolean isBraking = brakePedalPosition > 0;
        double speedDelta;

        if (isAccelerate) {
            speedDelta = orientationVector * (actualRPM * CarSpecifications.GEAR_RATIOS.get(shiftLevel)
                    / (CarSpecifications.WEIGHT * WIND_RESISTANCE));
        } else {
            speedDelta = -1 * orientationVector * (double) CarSpecifications.ENGINE_BRAKE_TORQUE *
                    WIND_RESISTANCE / PERCENTAGE;
        }

        if (isBraking) {
            speedDelta = -1 * orientationVector * ((CarSpecifications.MAX_BRAKE_SPEED / PERCENTAGE)
                    * brakePedalPosition);
        }
        LOGGER.debug(":: calculateSpeedDifference() method called:\n{ IsAccelerate: " + isAccelerate
                + ", IsBraking: " + isBraking + ", Speed difference (per sec): " + speedDelta
                + ", Shift level: " + shiftLevel + ", Actual RPM: " + actualRPM + ". Actual speed: " + speed + " }");

        return speedDelta / REFRESH_RATE;
    }

    /**
     * Calculates the RPM considering the gas pedal position, and send this value to VirtualFunctionBus
     *
     * @param gaspedalPosition pas pedal position value
     * @return actual RPM
     */
    public int calculateExpectedRPM(int gaspedalPosition) {
        if (gaspedalPosition == 0) {
            powertrainPacket.setRpm(CarSpecifications.IDLE_RPM);
            return CarSpecifications.IDLE_RPM;
        } else {
            double multiplier = ((double) (CarSpecifications.MAX_RPM - CarSpecifications.IDLE_RPM) / PERCENTAGE);
            int actualRpm = (int) ((gaspedalPosition * multiplier) + CarSpecifications.IDLE_RPM);
            powertrainPacket.setRpm(actualRpm);
            return actualRpm;
        }
    }

    /**
     * Manage the automated gearbox levels
     *
     * @param speedDelta Speed difference input decide to the car is accelerate or slowing down
     */
    private void gearShiftWatcher(double speedDelta) {
        int shiftLevelChange = 0;

        if (speedDelta > 0 && shiftLevel < CarSpecifications.GEARBOX_MAX_LEVEL) {
            while (CarSpecifications.GEAR_SHIFT_LEVEL_SPEED.get(shiftLevel + shiftLevelChange) < Math.abs(speed)) {
                shiftLevelChange++;
            }
            if ((shiftLevelChange > 0)) {
                shiftLevel += shiftLevelChange;
                LOGGER.debug(":: gearShiftWatcher() method called: Need to shifting up. New shiftlevel: "
                        + shiftLevel);
            } else {
                LOGGER.debug(":: gearShiftWatcher() method called: Don't need to shift.");
            }
        }

        if (speedDelta < 0 && shiftLevel > CarSpecifications.GEARBOX_MIN_LEVEL) {
            while (CarSpecifications.GEAR_SHIFT_LEVEL_SPEED.get(shiftLevel + shiftLevelChange) > Math.abs(speed)) {
                if (shiftLevel > CarSpecifications.GEARBOX_MIN_LEVEL) {
                    shiftLevelChange--;
                }
            }
            if ((shiftLevelChange < 0)) {
                shiftLevel += shiftLevelChange;
                LOGGER.debug(":: gearShiftWatcher() method called: Need to shifting down. New shiftlevel: "
                        + shiftLevel);
            } else {
                LOGGER.debug(":: gearShiftWatcher() method called: Don't need to shift.");
            }
        }
    }

    @Override
    public void loop() throws MissingPacketException {
        getVirtualFunctionBusSignals();
        actualRPM = calculateExpectedRPM(gasPedalPosition);
        doPowertrain();
    }

    /**
     * This method for UnitTest
     */
    public void loopTest() {
        this.gearState = virtualFunctionBus.samplePacket.getGearState();
        this.gasPedalPosition = virtualFunctionBus.samplePacket.getGaspedalPosition();
        this.brakePedalPosition = virtualFunctionBus.samplePacket.getBrakepedalPosition();

        this.actualRPM = calculateExpectedRPM(gasPedalPosition);
        doPowertrain();
    }

    /**
     * Modifies the actual speed and send to VirtualFunctionBus
     */
    private void doPowertrain() {
        double speedDelta = calculateSpeedDifference();

        switch (gearState) {
            case R:
                orientationVector = -1;
                shiftLevel = 0;

                if (brakePedalPosition == 0) {
                    LOGGER.debug(":: doPowertrain() method called: Slowing down to minimum speed");
                    // Acceleration
                    if (speedDelta < 0 && (speed > CarSpecifications.MAX_REVERSE_SPEED)) {
                        adjustSpeed(speedDelta);
                    }
                    // Enginebreak
                    if (speedDelta > 0 && speed < CarSpecifications.MIN_REVERSE_SPEED) {
                        adjustSpeed(speedDelta);
                    }


                } else {
                    LOGGER.debug(":: doPowertrain() method called: Braking, allow to stop to zero");
                    if (speed < 0) {
                        adjustSpeed(speedDelta);
                    }
                    if (speed > 0) {
                        speed = 0;
                        powertrainPacket.setSpeed(speed);
                    }
                }

                break;

            case D:
                orientationVector = 1;
                shiftLevel = 1;
                gearShiftWatcher(speedDelta);

                if (brakePedalPosition == 0) {
                    LOGGER.debug(":: doPowertrain() method called: Accelerate to maximum speed");
                    // Acceleration
                    if (speedDelta > 0 && speed < CarSpecifications.MAX_FORWARD_SPEED) {
                        adjustSpeed(speedDelta);
                    }
                    // Enginebrake
                    if (speedDelta < 0 && speed > CarSpecifications.MIN_FORWARD_SPEED) {
                        adjustSpeed(speedDelta);
                    }
                } else {
                    LOGGER.debug(":: doPowertrain() method called: Braking, allow to stop to zero");
                    if (speed > 0) {
                        adjustSpeed(speedDelta);
                    }
                    if (speed < 0) {
                        speed = 0;
                        powertrainPacket.setSpeed(speed);
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * Adjust new speed
     *
     * @param speedDelta speed delta
     */
    private static void adjustSpeed(double speedDelta) {
        speed += speedDelta;
        powertrainPacket.setSpeed(speed);
    }

    /**
     * This method slowing down the car 30 percent of the speed, when its collide traffic sign or NPC car.
     */
    public static void carCollide() {
        double speedDelta = -1 * speed * SLOWDOWN_DEGREE;
        adjustSpeed(speedDelta);
    }

    @Override
    public void getVirtualFunctionBusSignals() throws MissingPacketException {
        if (virtualFunctionBus.inputPacket == null) {
            throw new MissingPacketException("Powertrain try to read InputPacket signals from VirtualFunctionBus, " +
                    "but InputPacket was not initiated");
        } else {
            gasPedalPosition = virtualFunctionBus.inputPacket.getGasPedalPosition();
            brakePedalPosition = virtualFunctionBus.inputPacket.getBreakPedalPosition();
            gearState = virtualFunctionBus.inputPacket.getGearState();
        }
    }
}
