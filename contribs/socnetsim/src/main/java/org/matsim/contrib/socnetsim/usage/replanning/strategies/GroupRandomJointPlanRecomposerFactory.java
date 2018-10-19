/* *********************************************************************** *
 * project: org.matsim.*
 * GroupRandomJointPlanRecomposerFactory.java
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

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.gbl.MatsimRandom;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.matsim.contrib.socnetsim.framework.population.JointPlans;
import org.matsim.contrib.socnetsim.framework.replanning.GroupPlanStrategy;
import org.matsim.contrib.socnetsim.usage.replanning.GroupPlanStrategyFactoryUtils;
import org.matsim.contrib.socnetsim.framework.replanning.modules.PlanLinkIdentifier;
import org.matsim.contrib.socnetsim.framework.replanning.modules.PlanLinkIdentifier.Strong;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.EmptyIncompatiblePlansIdentifierFactory;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.highestweightselection.RandomGroupLevelSelector;

/**
 * @author thibautd
 */
public class GroupRandomJointPlanRecomposerFactory implements Provider<GroupPlanStrategy> {

	private final Scenario sc;
	private final PlanLinkIdentifier planLinkIdentifier;

	@Inject
	public GroupRandomJointPlanRecomposerFactory( Scenario sc , @Strong PlanLinkIdentifier planLinkIdentifier ) {
		this.sc = sc;
		this.planLinkIdentifier = planLinkIdentifier;
	}

	@Override
	public GroupPlanStrategy get() {
		// Note that this breaks incompatibility constraints, but not
		// joint plans constraints. Thus, it is not such a "recomposition"
		// as a grouping of joint plans.
		final GroupPlanStrategy strategy = new GroupPlanStrategy(
				new RandomGroupLevelSelector(
					MatsimRandom.getLocalInstance(),
					new EmptyIncompatiblePlansIdentifierFactory() ) );

		// recompose
		strategy.addStrategyModule(
				GroupPlanStrategyFactoryUtils.createRecomposeJointPlansModule(
						sc.getConfig(),
						((JointPlans) sc.getScenarioElement(JointPlans.ELEMENT_NAME)).getFactory(),
						planLinkIdentifier));

		return strategy;

	}
}

