/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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

package org.matsim.households;

import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.utils.objectattributes.attributable.Attributable;
import org.matsim.vehicles.Vehicle;

/**
 * @author dgrether
 */
public interface Household extends Attributable {

	public Id<Household> getId();
	
	public List<Id<Person>> getMemberIds();
	
	/**
	 * This returns an Income, not a number.  The Income type contains a method `getIncomePeriod()'. 
	 */
	public Income getIncome();
	
	public List<Id<Vehicle>> getVehicleIds();

	public void setIncome(Income income);

}
