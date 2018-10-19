/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2017 by the members listed in the COPYING,        *
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

package org.matsim.contrib.av.flow;

import org.matsim.api.core.v01.Id;
import org.matsim.contrib.dvrp.vrpagent.VrpAgentSource;
import org.matsim.core.controler.AbstractModule;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleTypeImpl;

import com.google.inject.name.Names;
/**
 * This module allows to de- or increase the amount of flow capacity used by a DVRP vehicle,
 * which may be useful for modelling autonomous vehicles within the Queue model.
 * 
 * When using this model in science, please consider citing 
 * M. Maciejewski, J. Bischoff; Congestion Effects of Autonomous Taxi Fleets; 16-11;
 * available from <a href="http://www.vsp.tu-berlin.de/publications/vspwp/">http://www.vsp.tu-berlin.de/publications/vspwp/</a>
 *
 */
public class AvIncreasedCapacityModule extends AbstractModule {
	private final VehicleType vehicleType;

	/**
	 * @param flowEfficiencyFactor: A factor of 1.0 == same flow for dvrp vehicles as for cars (default),
	 *                               a factor of > 1: increased efficiency, e.g. a value of 2.0 would mean that two AVs
	 *                               would need only the flow capacity of 1 ordinary vehicle.
	 *                               A factor below 1 would mean that more capacity is used.
	 */
	public AvIncreasedCapacityModule(double flowEfficiencyFactor) {
		this(flowEfficiencyFactor, new VehicleTypeImpl(Id.create("autonomousVehicleType", VehicleType.class)));
	}

	public AvIncreasedCapacityModule(double flowEfficiencyFactor, VehicleType vehicleType) {
		vehicleType.setFlowEfficiencyFactor(flowEfficiencyFactor);//XXX set the factor here - not settable via XML
		this.vehicleType = vehicleType;
	}

	@Override
	public void install() {
		bind(VehicleType.class).annotatedWith(Names.named(VrpAgentSource.DVRP_VEHICLE_TYPE)).toInstance(vehicleType);
	}
}
