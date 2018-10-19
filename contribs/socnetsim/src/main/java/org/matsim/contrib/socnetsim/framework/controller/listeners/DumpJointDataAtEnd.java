/* *********************************************************************** *
 * project: org.matsim.*
 * DumpJointDataAtEnd.java
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
package org.matsim.contrib.socnetsim.framework.controller.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.socnetsim.framework.population.JointPlans;
import org.matsim.contrib.socnetsim.framework.population.JointPlansXmlWriter;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.events.ShutdownEvent;
import org.matsim.core.controler.listener.ShutdownListener;

/**
 * @author thibautd
 */
@Singleton
public class DumpJointDataAtEnd implements ShutdownListener {
	private final Scenario scenario;
	private final JointPlans jointPlans;
	private final OutputDirectoryHierarchy controlerIO;

	@Inject
	public DumpJointDataAtEnd(
			final Scenario scenarioData,
			final JointPlans jointPlans,
			final OutputDirectoryHierarchy controlerIO) {
		this.scenario = scenarioData;
		this.jointPlans = jointPlans;
		this.controlerIO = controlerIO;
	}

	@Override
	public void notifyShutdown(final ShutdownEvent event) {
		dumpJointPlans();
	}

	private void dumpJointPlans() {
		JointPlansXmlWriter.write(
				scenario.getPopulation(),
				jointPlans,
				controlerIO.getOutputFilename( "output_jointPlans.xml.gz" ) );
	}
}

