/* *********************************************************************** *
 * project: org.matsim.*
 * AmberLogic
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
package org.matsim.contrib.signals.model;

import java.util.Set;

import org.matsim.api.core.v01.Id;


/**
 * @author dgrether
 *
 */
public interface AmberLogic {

	public Set<SignalGroupStateChangeRequest> processDropping(double now, Id<SignalSystem> systemId, Id<SignalGroup> signalGroupId);

	public Set<SignalGroupStateChangeRequest> processOnsets(double now, Id<SignalSystem> systemId, Id<SignalGroup> signalGroupId);

}
