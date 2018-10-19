/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2010 by the members listed in the COPYING,        *
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

package org.matsim.core.config.consistency;

import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.Test;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ActivityParams;
import org.matsim.pt.PtConstants;
import org.matsim.testcases.utils.LogCounter;

/**
 * @author mrieser
 */
public class ConfigConsistencyCheckerImplTest {



	@Test
	public void testCheckPlanCalcScore_DefaultsOk() {
		Config config = new Config();
		config.addCoreModules();

		LogCounter logger = new LogCounter(Level.WARN);
		try {
			logger.activiate();
			new ConfigConsistencyCheckerImpl().checkPlanCalcScore(config);
			Assert.assertEquals(0, logger.getWarnCount());
		} finally {
			// make sure counter is deactivated at the end
			logger.deactiviate();
		}
	}

	@Test
	public void testCheckPlanCalcScore_Traveling() {
		Config config = new Config();
		config.addCoreModules();

		config.planCalcScore().getModes().get(TransportMode.car).setMarginalUtilityOfTraveling(3.0);

		LogCounter logger = new LogCounter(Level.WARN);
		try {
			logger.activiate();
			new ConfigConsistencyCheckerImpl().checkPlanCalcScore(config);
			Assert.assertEquals(1, logger.getWarnCount());
		} finally {
			// make sure counter is deactivated at the end
			logger.deactiviate();
		}
	}

	@Test
	public void testCheckPlanCalcScore_TravelingPt() {
		Config config = new Config();
		config.addCoreModules();

		config.planCalcScore().getModes().get(TransportMode.pt).setMarginalUtilityOfTraveling(3.0);

		LogCounter logger = new LogCounter(Level.WARN);
		try {
			logger.activiate();
			new ConfigConsistencyCheckerImpl().checkPlanCalcScore(config);
			Assert.assertEquals(1, logger.getWarnCount());
		} finally {
			// make sure counter is deactivated at the end
			logger.deactiviate();
		}
	}

	@Test
	public void testCheckPlanCalcScore_TravelingBike() {
		Config config = new Config();
		config.addCoreModules();

		config.planCalcScore().getModes().get(TransportMode.bike).setMarginalUtilityOfTraveling(3.0);

		LogCounter logger = new LogCounter(Level.WARN);
		try {
			logger.activiate();
			new ConfigConsistencyCheckerImpl().checkPlanCalcScore(config);
			Assert.assertEquals(1, logger.getWarnCount());
		} finally {
			// make sure counter is deactivated at the end
			logger.deactiviate();
		}
	}

	@Test
	public void testCheckPlanCalcScore_TravelingWalk() {
		Config config = new Config();
		config.addCoreModules();

		config.planCalcScore().getModes().get(TransportMode.walk).setMarginalUtilityOfTraveling(3.0);

		LogCounter logger = new LogCounter(Level.WARN);
		try {
			logger.activiate();
			new ConfigConsistencyCheckerImpl().checkPlanCalcScore(config);
			Assert.assertEquals(1, logger.getWarnCount());
		} finally {
			// make sure counter is deactivated at the end
			logger.deactiviate();
		}
	}
	
	@Test
	public void testCheckPlanCalcScore_PtInteractionActivity() {
		Config config = new Config();
		config.addCoreModules();

		ActivityParams transitActivityParams = new ActivityParams(PtConstants.TRANSIT_ACTIVITY_TYPE);
		transitActivityParams.setClosingTime(1.) ;
		config.planCalcScore().addActivityParams(transitActivityParams);

		try {
			new ConfigConsistencyCheckerImpl().checkPlanCalcScore(config);
			Assert.assertEquals(0,1) ; // should never get here
		} catch ( Exception ee ){
			
			System.out.println("expected exception") ;
		}
		
		config.vspExperimental().setAbleToOverwritePtInteractionParams(true) ;
		
		try {
			new ConfigConsistencyCheckerImpl().checkPlanCalcScore(config);
		} catch ( Exception ee ){
			Assert.assertEquals(0,1) ; // should never get here
		}
		
	}


}
