package org.hisp.dhis.mapping;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author Lars Helge Overland
 */
public class MappingServiceTest
    extends DhisSpringTest
{
    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private MappingService mappingService;

    private IndicatorGroup indicatorGroup;

    private IndicatorType indicatorType;

    private Indicator indicator;

    private DataElement dataElement;

    private DataElementGroup dataElementGroup;

    private PeriodType periodType;

    private Period period;

    private ExternalMapLayer externalMapLayerA;

    private ExternalMapLayer externalMapLayerB;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
    {
        OrganisationUnit organisationUnit = createOrganisationUnit( 'A' );
        OrganisationUnitLevel organisationUnitLevel = new OrganisationUnitLevel( 1, "Level" );

        organisationUnitService.addOrganisationUnit( organisationUnit );
        organisationUnitService.addOrganisationUnitLevel( organisationUnitLevel );

        indicatorGroup = createIndicatorGroup( 'A' );
        indicatorService.addIndicatorGroup( indicatorGroup );

        indicatorType = createIndicatorType( 'A' );
        indicatorService.addIndicatorType( indicatorType );

        indicator = createIndicator( 'A', indicatorType );
        indicatorService.addIndicator( indicator );

        dataElement = createDataElement( 'A' );
        dataElementService.addDataElement( dataElement );

        dataElementGroup = createDataElementGroup( 'A' );
        dataElementGroup.getMembers().add( dataElement );
        dataElementService.addDataElementGroup( dataElementGroup );

        periodType = periodService.getPeriodTypeByName( MonthlyPeriodType.NAME );
        period = createPeriod( periodType, getDate( 2000, 1, 1 ), getDate( 2000, 2, 1 ) );
        periodService.addPeriod( period );

        externalMapLayerA = new ExternalMapLayer( "A" );
        externalMapLayerA.setMapService( MapService.TMS );
        externalMapLayerA.setUrl( "testurl" );
        externalMapLayerA.setImageFormat( ImageFormat.JPG );
        externalMapLayerA.setMapLayerPosition( MapLayerPosition.BASEMAP );

        externalMapLayerB = new ExternalMapLayer( "B" );
        externalMapLayerB.setMapService( MapService.WMS );
        externalMapLayerB.setUrl( "testurl" );
        externalMapLayerB.setImageFormat( ImageFormat.JPG );
        externalMapLayerB.setMapLayerPosition( MapLayerPosition.BASEMAP );

    }

    @Test
    public void testAddExternalMapLayer()
    {
        int id = mappingService.addExternalMapLayer( externalMapLayerA );

        assertNotNull( mappingService.getExternalMapLayer( id ));
    }

    @Test
    public void testDeleteExternalMapLayer()
    {
        int id = mappingService.addExternalMapLayer( externalMapLayerA );

        mappingService.deleteExternalMapLayer( externalMapLayerA );

        assertNull( mappingService.getExternalMapLayer( id ) );
    }

    @Test
    public void testGetAllExternalMapLayer()
    {
        mappingService.addExternalMapLayer( externalMapLayerA );
        mappingService.addExternalMapLayer( externalMapLayerB );

        assertEquals( 2, mappingService.getAllExternalMapLayers().size() );
    }
}
