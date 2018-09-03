package hu.oe.nik.szfmv.detector.interfaces;

import hu.oe.nik.szfmv.environment.WorldObject;

import java.awt.*;
import java.util.List;

/**
 * Interface for the Camera sensor
 */
public interface ICamera {
    List<WorldObject> getWorldObjects(Point a, Point b, Point c);
}
