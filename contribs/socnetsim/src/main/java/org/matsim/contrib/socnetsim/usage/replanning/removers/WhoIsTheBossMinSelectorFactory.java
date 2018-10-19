/* *********************************************************************** *
 * project: org.matsim.*
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

package org.matsim.contrib.socnetsim.usage.replanning.removers;

import com.google.inject.Inject;
import org.matsim.core.config.Config;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.contrib.socnetsim.framework.replanning.removers.AbstractDumbRemoverFactory;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.GroupLevelPlanSelector;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.IncompatiblePlansIdentifierFactory;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.InverseScoreWeight;
import org.matsim.contrib.socnetsim.framework.replanning.selectors.whoisthebossselector.WhoIsTheBossSelector;
import org.matsim.contrib.socnetsim.usage.replanning.GroupReplanningConfigGroup;

public class WhoIsTheBossMinSelectorFactory extends AbstractDumbRemoverFactory {
	private final IncompatiblePlansIdentifierFactory incompatiblePlansIdentifierFactory;

	@Inject
	public WhoIsTheBossMinSelectorFactory(
			final Config conf,
			final IncompatiblePlansIdentifierFactory incompatiblePlansIdentifierFactory) {
		super( getMaxPlansPerAgent( conf ) );
		this.incompatiblePlansIdentifierFactory = incompatiblePlansIdentifierFactory;
	}

	private static int getMaxPlansPerAgent(final Config conf) {
		final GroupReplanningConfigGroup group = (GroupReplanningConfigGroup) conf.getModule( GroupReplanningConfigGroup.GROUP_NAME );
		return group.getMaxPlansPerAgent();
	}

	@Override
	public GroupLevelPlanSelector createSelector() {
		return new WhoIsTheBossSelector(
				true ,
				MatsimRandom.getLocalInstance(),
				incompatiblePlansIdentifierFactory,
				new InverseScoreWeight() );
	}
}
