package hu.oe.nik.szfmv.detector.interfaces;

import hu.oe.nik.szfmv.environment.models.Collidable;

import java.awt.*;
import java.util.List;

/**
 * Interface for the Radar and the Ultrasonic sensor.
 */
public interface IRadarUltrasonic {
    List<Collidable> getCollidableObjects(Point a, Point b, Point c);
}
