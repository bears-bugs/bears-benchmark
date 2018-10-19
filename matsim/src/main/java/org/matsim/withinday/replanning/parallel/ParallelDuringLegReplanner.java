/* *********************************************************************** *
 * project: org.matsim.*
 * ParallelDuringLegReplanner.java
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

package org.matsim.withinday.replanning.parallel;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.withinday.replanning.replanners.interfaces.WithinDayDuringLegReplanner;
import org.matsim.withinday.replanning.replanners.interfaces.WithinDayDuringLegReplannerFactory;

/**
 * A class for running {@link WithinDayDuringLegReplanner} in parallel using threads.
 *
 * @author Christoph Dobler
 */
public class ParallelDuringLegReplanner extends ParallelReplanner<WithinDayDuringLegReplannerFactory> {

	public ParallelDuringLegReplanner(int numOfThreads, EventsManager eventsManager) {
		super(numOfThreads, eventsManager);
		this.init("ParallelDuringLegReplanner");
	}
	
}