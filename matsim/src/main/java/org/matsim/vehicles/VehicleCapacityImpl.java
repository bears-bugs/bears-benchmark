/* *********************************************************************** *
 * project: org.matsim.*
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

package org.matsim.vehicles;


/**
 * @author dgrether
 */
public class VehicleCapacityImpl implements VehicleCapacity {

	private Integer seats = null;
	private Integer standingRoom = null;
	private FreightCapacity freightCap = null;
	
	public VehicleCapacityImpl(){}
	
	@Override
	public FreightCapacity getFreightCapacity() {
		return freightCap;
	}

	@Override
	public Integer getSeats() {
		return seats;
	}

	@Override
	public Integer getStandingRoom() {
		return standingRoom;
	}

	@Override
	public void setFreightCapacity(FreightCapacity freightCap) {
		this.freightCap = freightCap;
	}

	@Override
	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	@Override
	public void setStandingRoom(Integer standingRoom) {
		this.standingRoom = standingRoom;
	}
	
}
