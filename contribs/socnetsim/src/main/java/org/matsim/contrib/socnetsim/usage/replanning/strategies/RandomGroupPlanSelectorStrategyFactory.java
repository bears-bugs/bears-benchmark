/* *********************************************************************** *
 * project: org.matsim.*
 * RandomGroupPlanSelectorStrategyFactory.java
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
package org.matsim.contrib.socnetsim.usage.replanning.strategies;

import org.matsim.core.gbl.MatsimRandom;

import com.google.inject.Inject;

import org.matsim.contrib.socnetsim.framework.replanning.NonInnovativeStrategyFactory;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.GroupLevelPlanSelector;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.IncompatiblePlansIdentifierFactory;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.highestweightselection.RandomGroupLevelSelector;

/**
 * @author thibautd
 */
public class RandomGroupPlanSelectorStrategyFactory extends NonInnovativeStrategyFactory {

	private final IncompatiblePlansIdentifierFactory incompatiblePlansIdentifierFactory;

	@Inject
	public RandomGroupPlanSelectorStrategyFactory( IncompatiblePlansIdentifierFactory incompatiblePlansIdentifierFactory ) {
		this.incompatiblePlansIdentifierFactory = incompatiblePlansIdentifierFactory;
	}

	@Override
	public GroupLevelPlanSelector createSelector() {
		return new RandomGroupLevelSelector(
			MatsimRandom.getLocalInstance(),
			incompatiblePlansIdentifierFactory );
	}
}

