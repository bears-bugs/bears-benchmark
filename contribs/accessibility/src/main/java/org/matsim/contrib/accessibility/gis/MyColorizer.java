/* *********************************************************************** *
 * project: org.matsim.*
 * MyColorizer.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2011 by the members listed in the COPYING,        *
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

/**
 * 
 */
package org.matsim.contrib.accessibility.gis;

import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.hash.TObjectDoubleHashMap;

import java.awt.Color;

/**
 * @author illenberger
 * @author thomas
 */
public final class MyColorizer implements Colorizable{
	
	private final TObjectDoubleHashMap values;
	
	private double max;
	
	private double min;
	
	private boolean logscale;
	
	public MyColorizer(TObjectDoubleHashMap<?> values) {
		this.values = values;
		
		max = - Double.MAX_VALUE;
		min = Double.MAX_VALUE;
		
		TObjectDoubleIterator<?> it = values.iterator();
		for(int i = 0; i < values.size(); i++) {
			it.advance();
			max = Math.max(max, it.value());
			min = Math.min(min, it.value());
		}
	}
	
	public void setLogscale(boolean logscale) {
		this.logscale = logscale;
	}
	
	public boolean isLogscale() {
		return logscale;
	}
	
	@SuppressWarnings("unchecked")
	protected double getValue(Object object) {
		double r = values.get(object);

		return r;
	}
	
	@Override
	public Color getColor(Object object) {
		double val = getValue(object);
		double color = 0;
		if (logscale) {
			double min2 = Math.log(min + 1);
			double max2 = Math.log(max + 1);
			color = (Math.log(val + 1) - min2) / (max2 - min2);
		} else {
			color = (val - min) / (max - min);
		}

		return ColorUtils.getGRBColor(color);
	}

}

