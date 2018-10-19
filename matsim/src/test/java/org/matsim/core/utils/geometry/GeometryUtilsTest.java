/**
 * 
 */
package org.matsim.core.utils.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.testcases.MatsimTestUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * @author kainagel
 *
 */
public class GeometryUtilsTest {

	@Rule public MatsimTestUtils utils = new MatsimTestUtils() ;

	@SuppressWarnings("static-method")
	@Test
	public final void testIntersectingLinks() {
		
		Config config = ConfigUtils.loadConfig( IOUtils.newUrl( ExamplesUtils.getTestScenarioURL("equil"), "config.xml" ) ) ;
		
		final Network network = ScenarioUtils.loadScenario(config).getNetwork();

		{
			double xx = -15001 ;
			LineString testSegment = new GeometryFactory().createLineString(new Coordinate[]{new Coordinate(xx,0),new Coordinate(xx,10000)}) ;

			List<Link> results = GeometryUtils.findIntersectingLinks(testSegment, network);

			List<Id<Link>> expecteds = new ArrayList<>() ;
			expecteds.add( Id.createLinkId(1) ) ;

			Assert.assertEquals(expecteds.size(), results.size()) ;
			for ( int ii=0 ; ii<expecteds.size() ; ii++ ) {
				Assert.assertEquals( "wrong link id;", expecteds.get(ii), results.get(ii).getId() ) ;
			}
		}
		{
			double xx = -14001 ;
			LineString testSegment = new GeometryFactory().createLineString(new Coordinate[]{new Coordinate(xx,0),new Coordinate(xx,10000)}) ;

			List<Link> results = GeometryUtils.findIntersectingLinks(testSegment, network);

			List<Id<Link>> expecteds = new ArrayList<>() ;
			expecteds.add( Id.createLinkId(2) ) ;
			expecteds.add( Id.createLinkId(3) ) ;
			expecteds.add( Id.createLinkId(4) ) ;
			expecteds.add( Id.createLinkId(5) ) ;
			expecteds.add( Id.createLinkId(6) ) ;

			Assert.assertEquals(expecteds.size(), results.size()) ;
			for ( int ii=0 ; ii<expecteds.size() ; ii++ ) {
				Assert.assertEquals( expecteds.get(ii), results.get(ii).getId() ) ;
			}
		}
		
	}

}
