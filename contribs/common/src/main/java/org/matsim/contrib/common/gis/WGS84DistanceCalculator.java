/* *********************************************************************** *
 * project: org.matsim.*
 * WGS84DistanceCalculator.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2010 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.matsim.contrib.common.gis;

import com.vividsolutions.jts.geom.Point;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;

/**
 * @author illenberger
 *
 */
public class WGS84DistanceCalculator implements DistanceCalculator {

	private static WGS84DistanceCalculator instance;
	
	public static WGS84DistanceCalculator getInstance() {
		if(instance == null)
			instance = new WGS84DistanceCalculator();
		
		return instance;
	}
	
	private final GeodeticCalculator geoCalc = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
	
	@Override
	public double distance(Point p1, Point p2) {
		geoCalc.setStartingGeographicPoint(p1.getX(), p1.getY());
		geoCalc.setDestinationGeographicPoint(p2.getX(), p2.getY());
		return geoCalc.getOrthodromicDistance();
	}

}
