package org.matsim.contrib.freight.carrier;

import java.util.HashMap;
import java.util.Map;

import org.matsim.vehicles.FreightCapacity;

public class CarrierFreightCapacity implements FreightCapacity{

	public final static String LOADING_AID = "loadingAid"; 
	
	private Map<String, Double> customDimensions = new HashMap<String, Double>();
	
	private double volumeInCubicMeters;
	
	private double weightInKilo;
	
	public void setCapacity(String dimensionKey, Double value){
		customDimensions.put(dimensionKey,value);
	}
	
	public Double getCapacity(String dimensionKey){
		return customDimensions.get(dimensionKey);
	}
	
	@Override
	public double getVolume() {
		return volumeInCubicMeters;
	}

	@Override
	public void setVolume(double cubicMeters) {
		volumeInCubicMeters = cubicMeters;
	}

	public void setWeight(double kilos){
		weightInKilo = kilos;
	}
	
	public double getWeight(){
		return weightInKilo;
	}

}
