package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import java.util.ArrayList;
import java.util.Arrays;

public final class CarSpecifications {

    public static final int MAX_RPM = 7400;       // unit: rpm
    public static final int IDLE_RPM = 740;        // unit: rpm
    public static final double MAX_FORWARD_SPEED = 118.0605;
    public static final double MAX_REVERSE_SPEED = -5.278;
    public static final double MIN_FORWARD_SPEED = 4.3888;
    public static final double MIN_REVERSE_SPEED = -3.3888;
    public static final int ENGINE_BRAKE_TORQUE = 70;         // unit: Nm
    public static final int WEIGHT = 1360;       // unit: kg
    public static final double MAX_BRAKE_SPEED = 25;
    public static final int GEARBOX_MAX_LEVEL = 6;
    public static final int GEARBOX_MIN_LEVEL = 1;

    /**
     * Gears: R, 1, 2, 3, 4, 5, 6
     */
    public static final ArrayList<Double> GEAR_RATIOS =
            new ArrayList<>(Arrays.asList(2.90, 2.66, 1.78, 1.30, 1.00, 0.74, 0.50));

    /**
     * 15, 30, 50, 70, 100, 130 km/h
     */
    public static final ArrayList<Double> GEAR_SHIFT_LEVEL_SPEED =
            new ArrayList<>(Arrays.asList(1.3888, 5.5555, 9.7222, 13.8888, 22.2222, 30.5555, Double.MAX_VALUE));
}
