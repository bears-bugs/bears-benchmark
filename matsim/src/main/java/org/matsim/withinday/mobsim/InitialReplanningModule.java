/* *********************************************************************** *
 * project: org.matsim.*
 * InitialReplanningModule.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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

package org.matsim.withinday.mobsim;

import org.matsim.withinday.replanning.parallel.ParallelInitialReplanner;
import org.matsim.withinday.replanning.replanners.interfaces.WithinDayInitialReplannerFactory;

public class InitialReplanningModule extends WithinDayReplanningModule<WithinDayInitialReplannerFactory> {
	
	public InitialReplanningModule(ParallelInitialReplanner parallelInitialReplanner) {
		this.parallelReplanner = parallelInitialReplanner;
	}
	
}
