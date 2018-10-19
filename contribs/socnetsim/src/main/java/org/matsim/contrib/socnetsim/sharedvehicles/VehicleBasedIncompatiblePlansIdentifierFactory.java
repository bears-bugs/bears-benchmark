/* *********************************************************************** *
 * project: org.matsim.*
 * VehicleBasedIncompatiblePlansIdentifierFactory.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2013 by the members listed in the COPYING,        *
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
package org.matsim.contrib.socnetsim.sharedvehicles;

import java.util.Collection;
import java.util.Set;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;

import org.matsim.contrib.socnetsim.framework.population.JointPlans;
import org.matsim.contrib.socnetsim.framework.replanning.grouping.ReplanningGroup;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.IncompatiblePlansIdentifier;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.IncompatiblePlansIdentifierFactory;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.IncompatiblePlansIdentifierImpl;

/**
 * @author thibautd
 */
public class VehicleBasedIncompatiblePlansIdentifierFactory implements IncompatiblePlansIdentifierFactory {
	private final Collection<String> modes;

	public VehicleBasedIncompatiblePlansIdentifierFactory(final Collection<String> modes) {
		this.modes = modes;
	}

	@Override
	public IncompatiblePlansIdentifier createIdentifier(
			final JointPlans jointPlans,
			final ReplanningGroup group) {
		final IncompatiblePlansIdentifierImpl identifier = new IncompatiblePlansIdentifierImpl();

		for ( Person person : group.getPersons() ) {
			for ( Plan plan : person.getPlans() ) {
				final Set<Id> vehicles = SharedVehicleUtils.getVehiclesInPlan( plan , modes );
				identifier.put( plan , vehicles );
			}
		}
		
		return identifier;
	}
}

