package org.hisp.dhis.analytics.event;

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

import org.hisp.dhis.analytics.AggregationType;
import org.hisp.dhis.analytics.EventOutputType;
import org.hisp.dhis.analytics.SortOrder;
import org.hisp.dhis.common.DhisApiVersion;
import org.hisp.dhis.common.DisplayProperty;
import org.hisp.dhis.common.EventAnalyticalObject;
import org.hisp.dhis.common.OrganisationUnitSelectionMode;
import org.hisp.dhis.event.EventStatus;
import org.hisp.dhis.program.ProgramStatus;

import java.util.Date;
import java.util.Set;

/**
 * @author Lars Helge Overland
 */
public interface EventDataQueryService
{
    /**
     * Used for aggregate query.
     *  @param program the program identifier.
     * @param stage the program stage identifier.
     * @param startDate the start date.
     * @param endDate the end date.
     * @param dimension the set of dimensions.
     * @param filter the set of filters.
     * @param value the value dimension identifier.
     * @param aggregationType the aggregation type for the value dimension.
     * @param skipMeta whether to skip meta-data in the response.
     * @param skipData whether to skip data in the response.
     * @param skipRounding whether to skip rounding of values in response.
     * @param completedOnly whether to only include completed events.
     * @param hierarchyMeta whether to include hierarchy meta-data in the response.
     * @param showHierarchy whether to include hierarchy meta-data names in the response.
     * @param sortOrder the sort order of the aggregate values.
     * @param limit the max limit of records to return.
     * @param outputType the event output type.
     * @param eventStatus the event status.
     * @param collapseDataDimensions collapse data dimensions into a single dimension.
     * @param aggregateData return aggregated data values for data dimensions instead of items.
     * @param includeMetadataDetails whether to include metadata details in the response.
     * @param displayProperty the display property to use for meta-data.
     * @param userOrgUnit the user organisation unit to use, overrides current user.
     * @param apiVersion the API version used for the request.
     */
    EventQueryParams getFromUrl( String program, String stage, Date startDate, Date endDate,
        Set<String> dimension, Set<String> filter, String value, AggregationType aggregationType, boolean skipMeta,
        boolean skipData, boolean skipRounding, boolean completedOnly, boolean hierarchyMeta, boolean showHierarchy,
        SortOrder sortOrder, Integer limit, EventOutputType outputType, EventStatus eventStatus,
        ProgramStatus programStatus, boolean collapseDataDimensions, boolean aggregateData, boolean includeMetadataDetails,
        DisplayProperty displayProperty, Date relativePeriodDate, String userOrgUnit, DhisApiVersion apiVersion );

    /**
     * Used for event query.
     *  @param program the program identifier.
     * @param stage the program stage identifier.
     * @param startDate the start date.
     * @param endDate the end date.
     * @param dimension the set of dimensions.
     * @param filter the set of filters.
     * @param ouMode the organisation unit mode.
     * @param asc the dimensions to be sorted ascending.
     * @param desc the dimensions to be sorted descending.
     * @param skipMeta whether to skip meta-data in the response.
     * @param skipData whether to skip data in the response.
     * @param completedOnly whether to only include completed events.
     * @param hierarchyMeta whether to include hierarchy meta-data in the response.
     * @param coordinatesOnly whether to only return events which have coordinates.
     * @param includeMetadataDetails whether to include metadata details in the response.
     * @param displayProperty the display property to use for meta-data.
     * @param userOrgUnit the user organisation unit to use, overrides current user.
     * @param coordinateField the coordinate field to use for spatial analytics.
     * @param page the page number.
     * @param pageSize the page size.
     * @param apiVersion the API version used for the request.
     */
    EventQueryParams getFromUrl( String program, String stage, Date startDate, Date endDate,
        Set<String> dimension, Set<String> filter, OrganisationUnitSelectionMode ouMode, Set<String> asc,
        Set<String> desc, boolean skipMeta, boolean skipData, boolean completedOnly, boolean hierarchyMeta,
        boolean coordinatesOnly, boolean includeMetadataDetails, EventStatus eventStatus, ProgramStatus programStatus,
        DisplayProperty displayProperty, Date relativePeriodDate, String userOrgUnit, String coordinateField,
        Integer page, Integer pageSize, DhisApiVersion apiVersion );
    
    EventQueryParams getFromAnalyticalObject( EventAnalyticalObject object );
    
    /**
     * Returns the coordinate column field to use for the given coordinate field. Coordinate field
     * must match EVENT, a data element identifier or an attribute identifier.
     * 
     * @param coordinate the coordinate field.
     * @return the coordinate column field.
     * @throws IllegalQueryException if the coordinate field is not valid.
     */
    String getCoordinateField( String coordinate );
}
