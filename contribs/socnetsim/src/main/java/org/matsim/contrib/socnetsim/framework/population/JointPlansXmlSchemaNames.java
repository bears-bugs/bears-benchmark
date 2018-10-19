/* *********************************************************************** *
 * project: org.matsim.*
 * JointPlansXmlSchemaNames.java
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
package org.matsim.contrib.socnetsim.framework.population;

/**
 * @author thibautd
 */
public class JointPlansXmlSchemaNames {
	private JointPlansXmlSchemaNames() {}

	public static String ROOT_TAG = "jointPlans";
	public static String JOINT_PLAN_TAG = "jointPlan";
	public static String PLAN_TAG = "individualPlan";

	public static String PERSON_ATT = "personId";
	public static String PLAN_NR_ATT = "planNr";
}

