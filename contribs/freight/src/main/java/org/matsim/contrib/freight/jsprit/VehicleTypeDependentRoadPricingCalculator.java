/*******************************************************************************
 * Copyright (c) 2011 Stefan Schroeder.
 * eMail: stefan.schroeder@kit.edu
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Stefan Schroeder - initial API and implementation
 ******************************************************************************/
package org.matsim.contrib.freight.jsprit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.roadpricing.RoadPricingScheme;
import org.matsim.roadpricing.RoadPricingSchemeImpl.Cost;


/**
 * Calculator that manages and calculates vehicle type dependent road pricing schemas.
 * 
 * @author stefan schröder
 *
 */
public class VehicleTypeDependentRoadPricingCalculator {
	
	static interface TollCalculator {
		double getTollAmount(Cost cost, Link link);
	}
	
	static class CordonCalc implements TollCalculator {

		
		@Override
		public double getTollAmount(Cost cost, Link link) {
			if(cost == null) return 0.0;
			return cost.amount;
		}
		
	}
	
	static class DistanceCalc implements TollCalculator{

		@Override
		public double getTollAmount(Cost cost, Link link) {
			if(cost == null) return 0.0;
			return cost.amount*link.getLength();
		}
		
	}
	
	
	private Map<Id<org.matsim.vehicles.VehicleType>, Collection<RoadPricingScheme>> schemes = new HashMap<>();

	private Map<String,TollCalculator> calculators = new HashMap<String, TollCalculator>();
	
	/**
	 * Gets an unmodifiable list of {@link RoadPricingScheme} for a {@link VehicleType} with input-id.
	 */
	public Collection<RoadPricingScheme> getPricingSchemes(Id<org.matsim.vehicles.VehicleType> vehicleType){
		Collection<RoadPricingScheme> collection = schemes.get(vehicleType);
		if(collection == null) return Collections.unmodifiableCollection(Collections.<RoadPricingScheme>emptyList());
		return Collections.unmodifiableCollection(collection);
	}
	
	/**
	 * Gets the toll-amount for vehicleType on {@link Link} link at time 'time'.
	 * 
	 * <p>In case of <code>RoadPricingScheme.TOLL_TYPE_CORDON</code> the toll amount on a link is equivalent to <code>rps.getLinkCostInfo(...).amount</code>.
	 * 
	 * <p>In case of <code>RoadPricingScheme.TOLL_TYPE_DISTANCE</code> the toll amount on a link is equivalent to <code>rps.getLinkCostInfo(...).amount*link.getLength()</code>.
	 * 
	 * @param vehicleType
	 * @param link
	 * @param time
	 * @return
	 */
	public double getTollAmount(Id<org.matsim.vehicles.VehicleType> vehicleType, Link link, double time){ 
		Collection<RoadPricingScheme> pricingSchemes = getPricingSchemes(vehicleType);
		if(pricingSchemes == null) return 0.0; 
		double toll = 0.0;
		for(RoadPricingScheme rps : pricingSchemes){
			Cost linkCostInfo = rps.getLinkCostInfo(link.getId(), time, null, null);
			toll += calculators.get(rps.getType()).getTollAmount(linkCostInfo,link);
		}
		return toll; 
	}
	
	
	public void addPricingScheme(String vehicleTypeId, RoadPricingScheme pricingScheme){
		Id<org.matsim.vehicles.VehicleType> typeId = Id.create(vehicleTypeId, org.matsim.vehicles.VehicleType.class);
		addPricingScheme(typeId, pricingScheme);
	}
	
	/**
	 * Adds a {@link RoadPricingScheme} for vehicleTypeId.
	 * 
	 * <p>Currently, only Cordon and Distance tolls can be calculated.
	 * 
	 * <p>In case of <code>RoadPricingScheme.TOLL_TYPE_CORDON</code> the toll amount on a link is equivalent to <code>rps.getLinkCostInfo(...).amount</code>.
	 * 
	 * <p>In case of <code>RoadPricingScheme.TOLL_TYPE_DISTANCE</code> the toll amount on a link is equivalent to <code>rps.getLinkCostInfo(...).amount*link.getLength()</code>.
	 * 
	 * 
	 * @param vehicleTypeId
	 * @param {@link RoadPricingScheme}
	 */
	public void addPricingScheme(Id<org.matsim.vehicles.VehicleType> vehicleTypeId, RoadPricingScheme pricingScheme){
		if(!schemes.containsKey(vehicleTypeId)){
			schemes.put(vehicleTypeId, new ArrayList<RoadPricingScheme>());
		}
		schemes.get(vehicleTypeId).add(pricingScheme);
		if(pricingScheme.getType().equals(RoadPricingScheme.TOLL_TYPE_CORDON)){
			calculators.put(pricingScheme.getType(), new CordonCalc());
		}
		else if(pricingScheme.getType().equals(RoadPricingScheme.TOLL_TYPE_DISTANCE)){
			calculators.put(pricingScheme.getType(), new DistanceCalc());
		}
        else throw new IllegalStateException("toll type specification missing. if you use xml add specification to root node like this\n"
            + "<roadpricing type=\"cordon\" name=\"cordon\"> if it is a cordon toll and \n<roadpricing type=\"distance\" name=\"distance\"> if it is a distance toll");
	}
	
	/**
	 * Removes {@link RoadPricingScheme} for vehicleTypeId.
	 * 
	 * @param vehicleTypeId
	 */
	public void removePricingSchemes(Id<org.matsim.vehicles.VehicleType> vehicleTypeId){
		schemes.remove(vehicleTypeId);
	}

	/**
	 * Checks whether there is a {@link RoadPricingScheme} for vehicleType 'typeId'.
	 * 
	 * @param typeId
	 * @return true if there is a pricing-scheme, false otherwise
	 */
	public boolean containsKey(Id<org.matsim.vehicles.VehicleType> typeId) {
		return schemes.containsKey(typeId);
	}
	
	/**
	 * Gets an unmodifiable map of pricing schemes.
	 */
	public Map<Id<org.matsim.vehicles.VehicleType>,Collection<RoadPricingScheme>> getSchemes(){
		return Collections.unmodifiableMap(schemes);
	}
}