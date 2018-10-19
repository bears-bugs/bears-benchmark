package org.matsim.core.controler;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.groups.FacilitiesConfigGroup;
import org.matsim.core.config.groups.GlobalConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.TransportModeNetworkFilter;
import org.matsim.core.population.algorithms.AbstractPersonAlgorithm;
import org.matsim.core.population.algorithms.ParallelPersonAlgorithmUtils;
import org.matsim.core.population.algorithms.PersonPrepareForSim;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.PlanRouter;
import org.matsim.core.router.TripRouter;
import org.matsim.core.scenario.Lockable;
import org.matsim.facilities.ActivityFacilities;
import org.matsim.facilities.FacilitiesFromPopulation;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class PrepareForMobsimImpl implements PrepareForMobsim {
	// I think it is ok to have this public final.  Since one may want to use it as a delegate.  kai, may'18
	// yyyyyy but how should that work with a non-public constructor? kai, jun'18
	
	// yyyy There is currently a lot of overlap between PrepareForSimImpl and PrepareForMobsimImpl.
	// This should be removed.  kai, jun'18
	
	private static Logger log = Logger.getLogger(PrepareForMobsimImpl.class);
	
	private final GlobalConfigGroup globalConfigGroup;
	private final Scenario scenario;
	private final Network network;
	private final Population population;
	private final ActivityFacilities activityFacilities;
	private final Provider<TripRouter> tripRouterProvider;
	private final QSimConfigGroup qSimConfigGroup;
	private final FacilitiesConfigGroup facilitiesConfigGroup;
	
	@Inject
	PrepareForMobsimImpl(GlobalConfigGroup globalConfigGroup, Scenario scenario, Network network,
				Population population, ActivityFacilities activityFacilities, Provider<TripRouter> tripRouterProvider,
				QSimConfigGroup qSimConfigGroup, FacilitiesConfigGroup facilitiesConfigGroup) {
		this.globalConfigGroup = globalConfigGroup;
		this.scenario = scenario;
		this.network = network;
		this.population = population;
		this.activityFacilities = activityFacilities;
		this.tripRouterProvider = tripRouterProvider;
		this.qSimConfigGroup = qSimConfigGroup;
		this.facilitiesConfigGroup = facilitiesConfigGroup;
	}
	
	
	@Override
	public void run() {
		/*
		 * Create single-mode network here and hand it over to PersonPrepareForSim. Otherwise, each instance would create its
		 * own single-mode network. However, this assumes that the main mode is car - which PersonPrepareForSim also does. Should
		 * be probably adapted in a way that other main modes are possible as well. cdobler, oct'15.
		 * This is now only used for xy2links, which is the "street address" of the activity location of facility, and here for the time being we indeed
		 *  assume that it can be reached by car.  kai, jul'18
		 */
		final Network carOnlyNetwork;
		if (NetworkUtils.isMultimodal(network)) {
			log.info("Network seems to be multimodal. Create car-only network which is handed over to PersonPrepareForSim.");
			TransportModeNetworkFilter filter = new TransportModeNetworkFilter(network);
			carOnlyNetwork = NetworkUtils.createNetwork();
			HashSet<String> modes = new HashSet<>();
			modes.add(TransportMode.car);
			filter.filter(carOnlyNetwork, modes);
		} else {
			carOnlyNetwork = network;
		}
		
		// make sure all routes are calculated.
		ParallelPersonAlgorithmUtils.run(population, globalConfigGroup.getNumberOfThreads(),
				new ParallelPersonAlgorithmUtils.PersonAlgorithmProvider() {
					@Override
					public AbstractPersonAlgorithm getPersonAlgorithm() {
						return new PersonPrepareForSim(new PlanRouter(tripRouterProvider.get(), activityFacilities), scenario, carOnlyNetwork );
					}
					// yyyyyy This prepared network is only used for computing the distance.  So the full network would
					// actually be better than the car-only network, without doing damage elsewhere.  No?  kai, jul'18
				}
		);
		
		// yy Could now set the vehicle IDs in the routes.  But can as well also do this later (currently in PopulationAgentSource).  kai, jun'18
		
	}
	
}
