package org.hisp.dhis.dimension;

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

import static org.hisp.dhis.organisationunit.OrganisationUnit.KEY_LEVEL;
import static org.hisp.dhis.organisationunit.OrganisationUnit.KEY_USER_ORGUNIT;
import static org.hisp.dhis.period.RelativePeriodEnum.LAST_12_MONTHS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.hisp.dhis.common.DimensionalObjectUtils.*;
import static org.hisp.dhis.expression.ExpressionService.SYMBOL_WILDCARD;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.common.*;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementGroupSet;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramDataElementDimensionItem;
import org.hisp.dhis.program.ProgramTrackedEntityAttributeDimensionItem;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.trackedentity.TrackedEntityAttribute;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

/**
 * @author Lars Helge Overland
 */
public class DimensionServiceTest
    extends DhisSpringTest
{
    private DataElement deA;
    private DataElement deB;
    
    private DataElementCategoryOptionCombo cocA;
    
    private DataSet dsA;
    
    private Program prA;
    
    private TrackedEntityAttribute atA;
    
    private Period peA;
    private Period peB;
    
    private DimensionalItemObject peLast12Months;
    
    private OrganisationUnit ouA;
    private OrganisationUnit ouB;
    private OrganisationUnit ouC;
    private OrganisationUnit ouD;
    private OrganisationUnit ouE;
    
    private DimensionalItemObject ouUser;
    private DimensionalItemObject ouLevel2;

    private DataElementGroupSet deGroupSetA;
    
    private DataElementGroup deGroupA;
    private DataElementGroup deGroupB;
    private DataElementGroup deGroupC;

    private OrganisationUnitGroupSet ouGroupSetA;
    
    private OrganisationUnitGroup ouGroupA;
    private OrganisationUnitGroup ouGroupB;
    private OrganisationUnitGroup ouGroupC;
        
    @Autowired
    private DataElementService dataElementService;
    
    @Autowired
    private OrganisationUnitService organisationUnitService;
    
    @Autowired
    private OrganisationUnitGroupService organisationUnitGroupService;
    
    @Autowired
    private DataSetService dataSetService;
    
    @Autowired
    private DataElementCategoryService categoryService;
    
    @Autowired
    private IdentifiableObjectManager idObjectManager;
    
    @Autowired
    private DimensionService dimensionService;
    
    @Override
    public void setUpTest()
    {
        deA = createDataElement( 'A' );
        deB = createDataElement( 'B' );
        
        dataElementService.addDataElement( deA );
        dataElementService.addDataElement( deB );
        
        cocA = categoryService.getDefaultDataElementCategoryOptionCombo();
        
        dsA = createDataSet( 'A' );
        
        dataSetService.addDataSet( dsA );
        
        prA = createProgram( 'A' );
        
        idObjectManager.save( prA );
        
        atA = createTrackedEntityAttribute( 'A' );
        
        idObjectManager.save( atA );
        
        peA = createPeriod( "201201" );
        peB = createPeriod( "201202" );
        peLast12Months = new BaseDimensionalItemObject( LAST_12_MONTHS.toString() );
        
        peA.setUid( "201201" );
        peB.setUid( "201202" );
        
        ouA = createOrganisationUnit( 'A' );
        ouB = createOrganisationUnit( 'B' );
        ouC = createOrganisationUnit( 'C' );
        ouD = createOrganisationUnit( 'D' );
        ouE = createOrganisationUnit( 'E' );
        
        ouB.updateParent( ouA );
        ouC.updateParent( ouA );
        ouD.updateParent( ouB );
        ouE.updateParent( ouB );
        
        organisationUnitService.addOrganisationUnit( ouA );
        organisationUnitService.addOrganisationUnit( ouB );
        organisationUnitService.addOrganisationUnit( ouC );
        organisationUnitService.addOrganisationUnit( ouD );
        organisationUnitService.addOrganisationUnit( ouE );

        String level2 = KEY_LEVEL + 2;
        
        ouUser = new BaseDimensionalItemObject( KEY_USER_ORGUNIT );
        ouLevel2 = new BaseDimensionalItemObject( level2 );
        
        deGroupSetA = createDataElementGroupSet( 'A' );
        
        dataElementService.addDataElementGroupSet( deGroupSetA );
        
        deGroupA = createDataElementGroup( 'A' );
        deGroupB = createDataElementGroup( 'B' );
        deGroupC = createDataElementGroup( 'C' );
        
        deGroupA.getGroupSets().add( deGroupSetA );
        deGroupB.getGroupSets().add( deGroupSetA );
        deGroupC.getGroupSets().add( deGroupSetA );
        
        dataElementService.addDataElementGroup( deGroupA );
        dataElementService.addDataElementGroup( deGroupB );
        dataElementService.addDataElementGroup( deGroupC );
        
        deGroupSetA.getMembers().add( deGroupA );
        deGroupSetA.getMembers().add( deGroupB );
        deGroupSetA.getMembers().add( deGroupC );
        
        dataElementService.updateDataElementGroupSet( deGroupSetA );
        
        ouGroupSetA = createOrganisationUnitGroupSet( 'A' );
        
        organisationUnitGroupService.addOrganisationUnitGroupSet( ouGroupSetA );
        
        ouGroupA = createOrganisationUnitGroup( 'A' );
        ouGroupB = createOrganisationUnitGroup( 'B' );
        ouGroupC = createOrganisationUnitGroup( 'C' );
        
        ouGroupA.getGroupSets().add( ouGroupSetA );
        ouGroupB.getGroupSets().add( ouGroupSetA );
        ouGroupC.getGroupSets().add( ouGroupSetA );
        
        organisationUnitGroupService.addOrganisationUnitGroup( ouGroupA );
        organisationUnitGroupService.addOrganisationUnitGroup( ouGroupB );
        organisationUnitGroupService.addOrganisationUnitGroup( ouGroupC );
        
        ouGroupSetA.getOrganisationUnitGroups().add( ouGroupA );
        ouGroupSetA.getOrganisationUnitGroups().add( ouGroupB );
        ouGroupSetA.getOrganisationUnitGroups().add( ouGroupC );
        
        organisationUnitGroupService.updateOrganisationUnitGroupSet( ouGroupSetA );
    }
        
    @Test
    public void testMergeAnalyticalObject()
    {
        ReportTable reportTable = new ReportTable();
        
        reportTable.getColumns().add( new BaseDimensionalObject( DimensionalObject.DATA_X_DIM_ID, DimensionType.DATA_X, Lists.newArrayList( deA, deB ) ) );
        reportTable.getRows().add( new BaseDimensionalObject( DimensionalObject.ORGUNIT_DIM_ID, DimensionType.ORGANISATION_UNIT, Lists.newArrayList( ouA, ouB, ouC, ouD, ouE ) ) );
        reportTable.getFilters().add( new BaseDimensionalObject( DimensionalObject.PERIOD_DIM_ID, DimensionType.PERIOD, Lists.newArrayList( peA, peB ) ) );
        
        dimensionService.mergeAnalyticalObject( reportTable );
        
        assertEquals( 2, reportTable.getDataDimensionItems().size() );
        assertEquals( 2, reportTable.getPeriods().size() );
        assertEquals( 5, reportTable.getOrganisationUnits().size() );
    }
    
    @Test
    public void testMergeAnalyticalObjectUserOrgUnit()
    {
        ReportTable reportTable = new ReportTable();

        reportTable.getColumns().add( new BaseDimensionalObject( DimensionalObject.DATA_X_DIM_ID, DimensionType.DATA_X, Lists.newArrayList( deA, deB ) ) );
        reportTable.getRows().add( new BaseDimensionalObject( DimensionalObject.ORGUNIT_DIM_ID, DimensionType.ORGANISATION_UNIT, Lists.newArrayList( ouUser ) ) );
        reportTable.getFilters().add( new BaseDimensionalObject( DimensionalObject.PERIOD_DIM_ID, DimensionType.PERIOD, Lists.newArrayList( peA ) ) );
        
        dimensionService.mergeAnalyticalObject( reportTable );
        
        assertEquals( 2, reportTable.getDataDimensionItems().size() );
        assertEquals( 1, reportTable.getPeriods().size() );
        assertEquals( 0, reportTable.getOrganisationUnits().size() );
        assertTrue( reportTable.isUserOrganisationUnit() );        
    }

    @Test
    public void testMergeAnalyticalObjectOrgUnitLevel()
    {
        ReportTable reportTable = new ReportTable();

        reportTable.getColumns().add( new BaseDimensionalObject( DimensionalObject.DATA_X_DIM_ID, DimensionType.DATA_X, Lists.newArrayList( deA, deB ) ) );
        reportTable.getRows().add( new BaseDimensionalObject( DimensionalObject.ORGUNIT_DIM_ID, DimensionType.ORGANISATION_UNIT, Lists.newArrayList( ouLevel2, ouA ) ) );
        reportTable.getFilters().add( new BaseDimensionalObject( DimensionalObject.PERIOD_DIM_ID, DimensionType.PERIOD, Lists.newArrayList( peA ) ) );
        
        dimensionService.mergeAnalyticalObject( reportTable );
        
        assertEquals( 2, reportTable.getDataDimensionItems().size() );
        assertEquals( 1, reportTable.getPeriods().size() );
        assertEquals( 1, reportTable.getOrganisationUnits().size() );
        assertEquals( Integer.valueOf( 2 ), reportTable.getOrganisationUnitLevels().get( 0 ) );
    }

    @Test
    public void testMergeAnalyticalObjectRelativePeriods()
    {
        ReportTable reportTable = new ReportTable();
        
        reportTable.getColumns().add( new BaseDimensionalObject( DimensionalObject.DATA_X_DIM_ID, DimensionType.DATA_X, Lists.newArrayList( deA, deB ) ) );
        reportTable.getRows().add( new BaseDimensionalObject( DimensionalObject.ORGUNIT_DIM_ID, DimensionType.ORGANISATION_UNIT, Lists.newArrayList( ouA, ouB, ouC, ouD, ouE ) ) );
        reportTable.getFilters().add( new BaseDimensionalObject( DimensionalObject.PERIOD_DIM_ID, DimensionType.PERIOD, Lists.newArrayList( peLast12Months ) ) );
        
        dimensionService.mergeAnalyticalObject( reportTable );
        
        assertEquals( 2, reportTable.getDataDimensionItems().size() );
        assertEquals( 0, reportTable.getPeriods().size() );
        assertTrue( reportTable.getRelatives().isLast12Months() );
        assertEquals( 5, reportTable.getOrganisationUnits().size() );
    }

    @Test
    public void testMergeAnalyticalObjectOrgUnitGroupSet()
    {
        ReportTable reportTable = new ReportTable();
        
        reportTable.getColumns().add( new BaseDimensionalObject( DimensionalObject.DATA_X_DIM_ID, DimensionType.DATA_X, Lists.newArrayList( deA, deB ) ) );
        reportTable.getRows().add( ouGroupSetA );
        reportTable.getFilters().add( new BaseDimensionalObject( DimensionalObject.PERIOD_DIM_ID, DimensionType.PERIOD, Lists.newArrayList( peA, peB ) ) );
        
        dimensionService.mergeAnalyticalObject( reportTable );
        
        assertEquals( 2, reportTable.getDataDimensionItems().size() );
        assertEquals( 2, reportTable.getPeriods().size() );
        assertEquals( 1, reportTable.getOrganisationUnitGroupSetDimensions().size() );
        assertEquals( 3, reportTable.getOrganisationUnitGroupSetDimensions().get( 0 ).getItems().size() );
    }    

    @Test
    public void testMergeAnalyticalObjectDataElementGroupSet()
    {
        ReportTable reportTable = new ReportTable();
        
        reportTable.getColumns().add( new BaseDimensionalObject( DimensionalObject.DATA_X_DIM_ID, DimensionType.DATA_X, Lists.newArrayList( deA, deB ) ) );
        reportTable.getRows().add( deGroupSetA );
        reportTable.getFilters().add( new BaseDimensionalObject( DimensionalObject.PERIOD_DIM_ID, DimensionType.PERIOD, Lists.newArrayList( peA, peB ) ) );

        dimensionService.mergeAnalyticalObject( reportTable );
        
        assertEquals( 2, reportTable.getDataDimensionItems().size() );
        assertEquals( 2, reportTable.getPeriods().size() );
        assertEquals( 1, reportTable.getDataElementGroupSetDimensions().size() );
        assertEquals( 3, reportTable.getDataElementGroupSetDimensions().get( 0 ).getItems().size() );
    }        
    
    @Test
    public void testGetDimensionalItemObject()
    {
        String idA = deA.getUid();
        String idB = prA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + deA.getUid();
        String idC = prA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + atA.getUid();
        String idD = dsA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + ReportingRateMetric.REPORTING_RATE.name();
        String idE = dsA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + "UNKNOWN_METRIC";
        String idF = deA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + cocA.getUid();
        String idG = deA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + SYMBOL_WILDCARD;
        String idH = deA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + "UNKNOWN_SYMBOL";
        String idI = deA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + cocA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + cocA.getUid();
        String idJ = deA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + cocA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + SYMBOL_WILDCARD;
        String idK = deA.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + SYMBOL_WILDCARD + COMPOSITE_DIM_OBJECT_PLAIN_SEP + cocA.getUid();
        
        ProgramDataElementDimensionItem pdeA = new ProgramDataElementDimensionItem( prA, deA );
        ProgramTrackedEntityAttributeDimensionItem ptaA = new ProgramTrackedEntityAttributeDimensionItem( prA, atA );
        ReportingRate rrA = new ReportingRate( dsA, ReportingRateMetric.REPORTING_RATE );
        DataElementOperand deoA = new DataElementOperand( deA, cocA );
        DataElementOperand deoB = new DataElementOperand( deA, null );
        DataElementOperand deoC = new DataElementOperand( deA, cocA, cocA );
        DataElementOperand deoD = new DataElementOperand( deA, cocA, null );
        DataElementOperand deoE = new DataElementOperand( deA, null, cocA );
        
        assertNotNull( dimensionService.getDataDimensionalItemObject( idA ) );
        assertEquals( deA, dimensionService.getDataDimensionalItemObject( idA ) );
        
        assertNotNull( dimensionService.getDataDimensionalItemObject( idB ) );
        assertEquals( pdeA, dimensionService.getDataDimensionalItemObject( idB ) );
        
        assertNotNull( dimensionService.getDataDimensionalItemObject( idC ) );
        assertEquals( ptaA, dimensionService.getDataDimensionalItemObject( idC ) );
        
        assertNotNull( dimensionService.getDataDimensionalItemObject( idD ) );
        assertEquals( rrA, dimensionService.getDataDimensionalItemObject( idD ) );
        
        assertNull( dimensionService.getDataDimensionalItemObject( idE ) );
        
        assertNotNull( dimensionService.getDataDimensionalItemObject( idF ) );
        assertEquals( deoA, dimensionService.getDataDimensionalItemObject( idF ) );
        
        assertNotNull( dimensionService.getDataDimensionalItemObject( idG ) );
        assertEquals( deoB, dimensionService.getDataDimensionalItemObject( idG ) );

        assertNull( dimensionService.getDataDimensionalItemObject( idH ) );

        assertNotNull( dimensionService.getDataDimensionalItemObject( idI ) );
        assertEquals( deoC, dimensionService.getDataDimensionalItemObject( idI ) );

        assertNotNull( dimensionService.getDataDimensionalItemObject( idJ ) );
        assertEquals( deoD, dimensionService.getDataDimensionalItemObject( idJ ) );

        assertNotNull( dimensionService.getDataDimensionalItemObject( idK ) );
        assertEquals( deoE, dimensionService.getDataDimensionalItemObject( idK ) );
    }
}
