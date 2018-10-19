/* *********************************************************************** *
 * project: org.matsim.*												   *
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
package org.matsim.contrib.accessibility.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.contrib.accessibility.gis.GridUtils;
import org.matsim.contrib.matrixbasedptrouter.utils.BoundingBox;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.facilities.ActivityFacilities;
import org.matsim.facilities.ActivityFacilitiesFactory;
import org.matsim.facilities.ActivityFacilitiesFactoryImpl;
import org.matsim.facilities.ActivityFacility;
import org.matsim.facilities.ActivityOption;
import org.matsim.facilities.ActivityOptionImpl;
import org.matsim.facilities.FacilitiesUtils;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author dziemke
 */
public class AccessibilityUtils {
	public static final Logger LOG = Logger.getLogger(AccessibilityUtils.class);
	
	/**
	 * Collects all facilities of a given type that have been loaded to the sceanrio.
	 */
	public static ActivityFacilities collectActivityFacilitiesWithOptionOfType(Scenario scenario, String activityOptionType) {
		ActivityFacilities activityFacilities = FacilitiesUtils.createActivityFacilities(activityOptionType) ;
		for (ActivityFacility facility : scenario.getActivityFacilities().getFacilities().values()) {
			if (activityOptionType == null) { // no activity option type for facility given, use all of them
				activityFacilities.addActivityFacility(facility);
			} else {
				for (ActivityOption option : facility.getActivityOptions().values()) {
					if (option.getType().equals(activityOptionType)) {
						activityFacilities.addActivityFacility(facility);
					}
				}
			}
		}
		return activityFacilities;
	}

	/**
	 * Collects the types of all facilities that have been loaded to the scenario.
	 */
	public static List<String> collectAllFacilityOptionTypes(Scenario scenario) {
		List<String> activityOptionTypes = new ArrayList<>() ;
		for (ActivityFacility facility : scenario.getActivityFacilities().getFacilities().values()) {
			for (ActivityOption option : facility.getActivityOptions().values()) {
				// collect all activity types that are contained within the provided facilities file
				if (!activityOptionTypes.contains(option.getType())) {
					activityOptionTypes.add(option.getType()) ;
				}
			}
		}
		LOG.warn("The following activity option types where found within the activity facilities: " + activityOptionTypes);
		return activityOptionTypes;
	}
	
	public static void combineDifferentActivityOptionTypes(final Scenario scenario, String combinedType, final List<String> activityOptionsToBeIncluded) {
		ActivityOption markerOption = new ActivityOptionImpl(combinedType); 
		
		// Memorize all facilities that have certain activity options in a activity facilities container
		final ActivityFacilities consideredFacilities = FacilitiesUtils.createActivityFacilities();
		for (ActivityFacility facility : scenario.getActivityFacilities().getFacilities().values()) {
			for (ActivityOption option : facility.getActivityOptions().values()) {
				if (activityOptionsToBeIncluded.contains(option.getType())) {
					// if (!option.getType().equals(FacilityTypes.HOME) && !option.getType().equals(FacilityTypes.WORK) && !option.getType().equals("minor")) {
					if (!consideredFacilities.getFacilities().containsKey(facility.getId())) {
						consideredFacilities.addActivityFacility(facility);
					}
				}
			}
		}
		
		// Add  marker option to facilities to be considered
		for (ActivityFacility facility : consideredFacilities.getFacilities().values()) {
			facility.addActivityOption(markerOption);
		}
	}
	
	public static final ActivityFacilities createFacilityForEachLink( Network network ) {
		ActivityFacilities facilities = FacilitiesUtils.createActivityFacilities("LinkFacilities") ;
		ActivityFacilitiesFactory ff = facilities.getFactory() ;
		for ( Link link : network.getLinks().values() ) {
			ActivityFacility facility = ff.createActivityFacility( Id.create(link.getId(),ActivityFacility.class) , link.getCoord(), link.getId() ) ;
			facilities.addActivityFacility(facility);
		}
		return facilities ;
	}
	
	public static final ActivityFacilities createFacilityFromBuildingShapefile(String shapeFileName, String identifierCaption, String numberOfHouseholdsCaption) {
		ShapeFileReader shapeFileReader = new ShapeFileReader();
		Collection<SimpleFeature> features = shapeFileReader.readFileAndInitialize(shapeFileName);
		
		ActivityFacilities facilities = FacilitiesUtils.createActivityFacilities("DensitiyFacilities");
		ActivityFacilitiesFactory aff = facilities.getFactory();
		
		for (SimpleFeature feature : features) {
			String featureId = (String) feature.getAttribute(identifierCaption);
			Integer numberOfHouseholds = Integer.parseInt((String) feature.getAttribute(numberOfHouseholdsCaption));
			Geometry geometry = (Geometry) feature.getDefaultGeometry();
			Coord coord = CoordUtils.createCoord(geometry.getCentroid().getX(), geometry.getCentroid().getY());
			
			for (int i = 0; i < numberOfHouseholds; i++) {
				ActivityFacility facility = aff.createActivityFacility(Id.create(featureId + "_" + i, ActivityFacility.class), coord);
				facilities.addActivityFacility(facility);
			}
		}
		return facilities ;
	}
	
	/**
	 * Goes through a given set of measuring points and creates a facility on the measuring point if the
	 * nearest link from that measure point lies within a specified maximum allowed distance. The presence
	 * of such networkDensityFacilites can then be used to plot a density layer, e.g. to excluded tiles
	 * from being drawn if there is no network. The network density is thereby used as a proxy for settlement
	 * density if no information of settlement density is available.
	 */
	@Deprecated
	public static ActivityFacilities createNetworkDensityFacilities(Network network,
			ActivityFacilities measuringPoints, double maximumAllowedDistance) {
		// yyyyyy This method scares me ... since in the code it is used by code that does not really know the measuring points and so
		// only speculates about it.  kai, dec'16
		// yyyyyy Why not just create a facility for each link?  (The GridBasedAccessibilityListener aggregates those anyways ...).  kai, dec'16
		
		ActivityFacilitiesFactory aff = new ActivityFacilitiesFactoryImpl();
		ActivityFacilities networkDensityFacilities = FacilitiesUtils.createActivityFacilities("network_densities");

		for (ActivityFacility measuringPoint : measuringPoints.getFacilities().values() ) {
			Coord coord = measuringPoint.getCoord();
			Link link = NetworkUtils.getNearestLink(network, coord);
			// TODO check if this is good or if orthogonal projection etc. is more suitable
			double distance = CoordUtils.distancePointLinesegment(link.getFromNode().getCoord(), link.getToNode().getCoord(), coord);
			if (distance <= maximumAllowedDistance) {
				ActivityFacility facility = aff.createActivityFacility(measuringPoint.getId(), coord);
				networkDensityFacilities.addActivityFacility(facility);
			}
		}
		return networkDensityFacilities;
	}

	/**
	 * Creates measuring points based on the scenario's network and a specified cell size.
	 */
	public static ActivityFacilities createMeasuringPointsFromNetworkBounds(Network network, double cellSize) {
		BoundingBox boundingBox = BoundingBox.createBoundingBox(network);
		double xMin = boundingBox.getXMin();
		double xMax = boundingBox.getXMax();
		double yMin = boundingBox.getYMin();
		double yMax = boundingBox.getYMax();
		
		ActivityFacilities measuringPoints = GridUtils.createGridLayerByGridSizeByBoundingBoxV2(
				xMin, yMin, xMax, yMax, cellSize);
		return measuringPoints;
	}
	
	/**
	 * Calculates the sum of the values of a given list.
	 * 
	 * @param valueList
	 * @return sum
	 */
	public static double calculateSum(List<Double> valueList) {
		double sum = 0.;
		for (double i : valueList) {
			sum = sum + i;
		}
		return sum;
	}
	
	/**
	 * Calculates Gini coefficient of the values of a given values. The Gini Coefficient is equals to the half of
	 * the relative mean absolute difference (RMD).
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Gini_coefficient">
	 * @see <a href="https://en.wikipedia.org/wiki/Mean_absolute_difference#Relative_mean_absolute_difference">
	 * @param valueList
	 * @return giniCoefficient
	 */
	public static double calculateGiniCoefficient(List<Double> valueList) {
		int numberOfValues = valueList.size();
		double sumOfValues = calculateSum(valueList);
		double arithmeticMean = sumOfValues / numberOfValues;
		
		double sumOfAbsoluteDifferences = 0.;
		for (double i : valueList) {
			for (double j : valueList) {
				double absoulteDifference = Math.abs( i - j );
				sumOfAbsoluteDifferences = sumOfAbsoluteDifferences + absoulteDifference;
			}
		}
		double giniCoefficient = sumOfAbsoluteDifferences / (2 * Math.pow(numberOfValues, 2) * arithmeticMean);
		return giniCoefficient;
	}
	
	/**
	 * Creates facilities from plans. Note that a new additional facility is created for each activity.
	 * @param population
	 * @return
	 */
	public static ActivityFacilities createFacilitiesFromPlans(Population population) {
		ActivityFacilitiesFactory aff = new ActivityFacilitiesFactoryImpl();
		ActivityFacilities facilities = FacilitiesUtils.createActivityFacilities();
		
		for(Person person : population.getPersons().values()) {
			for(Plan plan : person.getPlans()) {
				Id <Person> personId = person.getId();
				
				for (PlanElement planElement : plan.getPlanElements()) {
					if (planElement instanceof Activity) {
						Activity activity = (Activity) planElement;
						
						Coord coord= activity.getCoord();
						if (coord == null) {
							throw new NullPointerException("Activity does not have any coordinates.");
						}
						
						String activityType = activity.getType();
						
						// In case an agent visits the same activity location twice, create another activity facility with a modified ID
						Integer i = 1;					
						Id<ActivityFacility> facilityId = Id.create(activityType + "_" + personId.toString() + "_" + i.toString(), ActivityFacility.class);
						while (facilities.getFacilities().containsKey(facilityId)) {
							i++;
							facilityId = Id.create(activityType + "_" + personId.toString() + "_" + i.toString(), ActivityFacility.class);
						}

						ActivityFacility facility = aff.createActivityFacility(facilityId, activity.getCoord());
						
						facility.addActivityOption(aff.createActivityOption(activityType));
						facilities.addActivityFacility(facility);
//						log.info("Created activity with option of type " + activityType + " and ID " + facilityId + ".");
					}
				}
			}
		}
//		FacilitiesWriter facilitiesWriter = new FacilitiesWriter(facilities);
//		facilitiesWriter.write("../../../public-svn/matsim/specifiy_location/...xml.gz");
		return facilities;
	}

	public static String getDate() {
		Calendar cal = Calendar.getInstance ();
		int month = cal.get(Calendar.MONTH) + 1;
		String monthStr = month + "";
		if (month < 10)
			monthStr = "0" + month;
		String date = cal.get(Calendar.YEAR) + "-" 
				+ monthStr + "-" + cal.get(Calendar.DAY_OF_MONTH);
		return date;
	}
}