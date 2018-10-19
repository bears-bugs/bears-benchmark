/* *********************************************************************** *
 * project: org.matsim.*
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

package org.matsim.contrib.taxi.data;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.data.Request;
import org.matsim.contrib.dvrp.passenger.PassengerRequest;
import org.matsim.contrib.taxi.schedule.TaxiDropoffTask;
import org.matsim.contrib.taxi.schedule.TaxiPickupTask;
import org.matsim.core.mobsim.framework.MobsimPassengerAgent;

/**
 * @author michalm
 */
public class TaxiRequest implements PassengerRequest {
	public enum TaxiRequestStatus {
		// INACTIVE, // invisible to the dispatcher (ARTIFICIAL STATE!)
		UNPLANNED, // submitted by the CUSTOMER and received by the DISPATCHER
		PLANNED, // planned - included into one of the routes

		// we have to carry out the request
		PICKUP, RIDE, DROPOFF,

		PERFORMED, //
		REJECTED, // rejected by the DISPATCHER
		// CANCELLED, // canceled by the CUSTOMER
		;
	}

	private final Id<Request> id;
	private final double submissionTime;
	private final double earliestStartTime;

	private boolean rejected = false;
	
	private final MobsimPassengerAgent passenger;
	private final Link fromLink;
	private final Link toLink;

	private TaxiPickupTask pickupTask;
	private TaxiDropoffTask dropoffTask;

	public TaxiRequest(Id<Request> id, MobsimPassengerAgent passenger, Link fromLink, Link toLink,
			double earliestStartTime, double submissionTime) {
		this.id = id;
		this.submissionTime = submissionTime;
		this.earliestStartTime = earliestStartTime;
		this.passenger = passenger;
		this.fromLink = fromLink;
		this.toLink = toLink;
	}

	@Override
	public Id<Request> getId() {
		return id;
	}

	@Override
	public double getSubmissionTime() {
		return submissionTime;
	}

	@Override
	public double getEarliestStartTime() {
		return earliestStartTime;
	}

	@Override
	public Link getFromLink() {
		return fromLink;
	}

	@Override
	public Link getToLink() {
		return toLink;
	}

	@Override
	public MobsimPassengerAgent getPassenger() {
		return passenger;
	}
	
	@Override
	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public TaxiPickupTask getPickupTask() {
		return pickupTask;
	}

	public void setPickupTask(TaxiPickupTask pickupTask) {
		this.pickupTask = pickupTask;
	}

	public TaxiDropoffTask getDropoffTask() {
		return dropoffTask;
	}

	public void setDropoffTask(TaxiDropoffTask dropoffTask) {
		this.dropoffTask = dropoffTask;
	}

	public TaxiRequestStatus getStatus() {
		if (pickupTask == null) {
			return TaxiRequestStatus.UNPLANNED;
		}

		switch (pickupTask.getStatus()) {
			case PLANNED:
				return TaxiRequestStatus.PLANNED;

			case STARTED:
				return TaxiRequestStatus.PICKUP;

			case PERFORMED:// continue
		}

		switch (dropoffTask.getStatus()) {
			case PLANNED:
				return TaxiRequestStatus.RIDE;

			case STARTED:
				return TaxiRequestStatus.DROPOFF;

			case PERFORMED:
				return TaxiRequestStatus.PERFORMED;
		}

		throw new IllegalStateException("Unreachable code");
	}

	@Override
	public String toString() {
		return Request.toString(this);
	}
}
