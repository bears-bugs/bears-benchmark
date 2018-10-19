/* *********************************************************************** *
 * project: org.matsim.*
 * CoalitionRandomFactory.java
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
package org.matsim.contrib.socnetsim.framework.replanning.strategies;

import java.util.Random;

import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.gbl.MatsimRandom;

import org.matsim.contrib.socnetsim.framework.replanning.NonInnovativeStrategyFactory;
import org.matsim.contrib.socnetsim.framework.replanning.grouping.ReplanningGroup;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.GroupLevelPlanSelector;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.WeightCalculator;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.coalitionselector.CoalitionSelector;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.coalitionselector.CoalitionSelector.ConflictSolver;

/**
 * @author thibautd
 */
public class CoalitionRandomFactory  extends NonInnovativeStrategyFactory {
	private final ConflictSolver conflictSolver;

	public CoalitionRandomFactory(
			final ConflictSolver conflictSolver) {
		this.conflictSolver = conflictSolver;
	}

	@Override
	public GroupLevelPlanSelector createSelector() {
		final Random random = MatsimRandom.getLocalInstance();
		return new CoalitionSelector(
				new WeightCalculator() {
					@Override
					public double getWeight(
							final Plan indivPlan,
							final ReplanningGroup group) {
						return random.nextDouble();
					}
				},
				conflictSolver);
	}
}

