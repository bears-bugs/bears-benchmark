package org.hisp.dhis.trackedentity;

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

import org.apache.commons.lang3.StringUtils;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.OrganisationUnitSelectionMode;
import org.hisp.dhis.common.QueryItem;
import org.hisp.dhis.common.QueryOperator;
import org.hisp.dhis.common.ValueType;
import org.hisp.dhis.fileresource.FileResource;
import org.hisp.dhis.fileresource.FileResourceService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.security.acl.AclService;
import org.hisp.dhis.system.util.DateUtils;
import org.hisp.dhis.system.util.MathUtils;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Abyot Asalefew
 */
@Transactional
public class DefaultTrackedEntityAttributeService
    implements TrackedEntityAttributeService
{
    private static final int VALUE_MAX_LENGTH = 50000;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    private TrackedEntityAttributeStore attributeStore;

    @Autowired
    private ProgramService programService;

    @Autowired
    private TrackedEntityTypeService trackedEntityTypeService;

    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private CurrentUserService currentUserService;
    
    @Autowired
    private AclService aclService;

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public void deleteTrackedEntityAttribute( TrackedEntityAttribute attribute )
    {
        attributeStore.delete( attribute );
    }

    @Override
    public List<TrackedEntityAttribute> getAllTrackedEntityAttributes()
    {
        return attributeStore.getAll();
    }

    @Override
    public TrackedEntityAttribute getTrackedEntityAttribute( int id )
    {
        return attributeStore.get( id );
    }

    @Override
    public int addTrackedEntityAttribute( TrackedEntityAttribute attribute )
    {
        attributeStore.save( attribute );
        return attribute.getId();
    }

    @Override
    public void updateTrackedEntityAttribute( TrackedEntityAttribute attribute )
    {
        attributeStore.update( attribute );
    }

    @Override
    public TrackedEntityAttribute getTrackedEntityAttributeByName( String name )
    {
        return attributeStore.getByName( name );
    }

    @Override
    public TrackedEntityAttribute getTrackedEntityAttributeByShortName( String shortName )
    {
        return attributeStore.getByShortName( shortName );
    }

    @Override
    public TrackedEntityAttribute getTrackedEntityAttributeByCode( String code )
    {
        return attributeStore.getByShortName( code );
    }

    @Override
    public TrackedEntityAttribute getTrackedEntityAttribute( String uid )
    {
        return attributeStore.getByUid( uid );
    }

    @Override
    public List<TrackedEntityAttribute> getTrackedEntityAttributesByDisplayOnVisitSchedule(
        boolean displayOnVisitSchedule )
    {
        return attributeStore.getByDisplayOnVisitSchedule( displayOnVisitSchedule );
    }

    @Override
    public List<TrackedEntityAttribute> getTrackedEntityAttributesDisplayInListNoProgram()
    {
        return attributeStore.getDisplayInListNoProgram();
    }

    @Override
    public String validateScope( TrackedEntityAttribute trackedEntityAttribute,
        String value, TrackedEntityInstance trackedEntityInstance, OrganisationUnit organisationUnit, Program program )
    {
        Assert.notNull( trackedEntityAttribute, "tracked entity attribute is required." );

        if ( !trackedEntityAttribute.isUnique() || value == null )
        {
            return null;
        }

        TrackedEntityInstanceQueryParams params = new TrackedEntityInstanceQueryParams();
        params.addAttribute( new QueryItem( trackedEntityAttribute, QueryOperator.EQ, value, trackedEntityAttribute.getValueType(),
            trackedEntityAttribute.getAggregationType(), trackedEntityAttribute.getOptionSet() ) );
        params.setInternalSearch( true );

        if ( trackedEntityAttribute.getOrgunitScope() && trackedEntityAttribute.getProgramScope() )
        {
            Assert.notNull( program, "program is required for program scope" );
            Assert.notNull( organisationUnit, "organisationUnit is required for org unit scope" );

            if ( !program.getOrganisationUnits().contains( organisationUnit ) )
            {
                return "Organisation unit is not assigned to program " + program.getUid();
            }

            params.setProgram( program );
            params.addOrganisationUnit( organisationUnit );
            params.setOrganisationUnitMode( OrganisationUnitSelectionMode.SELECTED );
        }
        else if ( trackedEntityAttribute.getOrgunitScope() )
        {
            Assert.notNull( organisationUnit, "organisation unit is required for org unit scope" );
            params.setOrganisationUnitMode( OrganisationUnitSelectionMode.SELECTED );
            params.addOrganisationUnit( organisationUnit );
        }
        else if ( trackedEntityAttribute.getProgramScope() )
        {
            Assert.notNull( program, "program is required for program scope" );
            params.setOrganisationUnitMode( OrganisationUnitSelectionMode.ALL );
            params.setProgram( program );
        }
        else
        {
            params.setOrganisationUnitMode( OrganisationUnitSelectionMode.ALL );
        }

        // TODO re-factor to avoid circular dependency

        TrackedEntityInstanceService trackedEntityInstanceService = (TrackedEntityInstanceService) applicationContext.getBean( TrackedEntityInstanceService.class );

        Grid instances = trackedEntityInstanceService.getTrackedEntityInstancesGrid( params );

        if ( !(instances.getHeight() == 0) )
        {
            if ( trackedEntityInstance == null || (instances.getHeight() == 1 && !instances.getRow( 0 ).contains( trackedEntityInstance.getUid() )) )
            {
                return "Non-unique attribute value '" + value + "' for attribute " + trackedEntityAttribute.getUid();
            }
        }

        return null;
    }

    @Override
    public String validateValueType( TrackedEntityAttribute trackedEntityAttribute, String value )
    {
        Assert.notNull( trackedEntityAttribute, "tracked entity attribute is required" );
        ValueType valueType = trackedEntityAttribute.getValueType();

        String errorValue = StringUtils.substring( value, 0, 30 );

        if ( value.length() > VALUE_MAX_LENGTH )
        {
            return "Value length is greater than 50000 chars for attribute " + trackedEntityAttribute.getUid();
        }

        if ( ValueType.NUMBER == valueType && !MathUtils.isNumeric( value ) )
        {
            return "Value '" + errorValue + "' is not a valid numeric type for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.BOOLEAN == valueType && !MathUtils.isBool( value ) )
        {
            return "Value '" + errorValue + "' is not a valid boolean type for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.DATE == valueType && DateUtils.parseDate( value ) == null )
        {
            return "Value '" + errorValue + "' is not a valid date type for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.TRUE_ONLY == valueType && !"true".equals( value ) )
        {
            return "Value '" + errorValue + "' is not true (true-only type) for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.USERNAME == valueType )
        {
            if ( userService.getUserCredentialsByUsername( value ) == null )
            {
                return "Value '" + errorValue + "' is not a valid username for attribute " + trackedEntityAttribute.getUid();
            }
        }
        else if ( ValueType.DATE == valueType && !DateUtils.dateIsValid( value ) )
        {
            return "Value '" + errorValue + "' is not a valid date for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.DATETIME == valueType && !DateUtils.dateTimeIsValid( value ) )
        {
            return "Value '" + errorValue + "' is not a valid datetime for attribute " + trackedEntityAttribute.getUid();
        }
        else if ( ValueType.IMAGE == valueType )
        {
            return validateImage( value );
        }
        else if ( trackedEntityAttribute.hasOptionSet() && !trackedEntityAttribute.isValidOptionValue( value ) )
        {
            return "Value '" + errorValue + "' is not a valid option for attribute " +
                trackedEntityAttribute.getUid() + " and option set " + trackedEntityAttribute.getOptionSet().getUid();
        }

        return null;
    }
    
    public Set<TrackedEntityAttribute> getAllUserReadableTrackedEntityAttributes()
    {
        Set<TrackedEntityAttribute> attributes = new HashSet<>();
        
        User user = currentUserService.getCurrentUser();        
        
        attributes = programService.getAllPrograms().stream().filter( program -> aclService.canDataRead( user, program ) ).collect( Collectors.toList() )
            .stream().map( Program::getTrackedEntityAttributes ).flatMap( Collection::stream ).collect( Collectors.toSet() );                
        
        attributes.addAll( trackedEntityTypeService.getAllTrackedEntityType().stream().filter( trackedEntityType -> aclService.canDataRead( user, trackedEntityType ) ).collect( Collectors.toList() )
            .stream().map( TrackedEntityType::getTrackedEntityAttributes ).flatMap( Collection::stream ).collect( Collectors.toSet() ) );        
        
        return attributes;
    }

    // -------------------------------------------------------------------------
    // ProgramTrackedEntityAttribute
    // -------------------------------------------------------------------------

    @Override
    public List<TrackedEntityAttribute> getAllSystemWideUniqueTrackedEntityAttributes()
    {
        return getAllTrackedEntityAttributes().stream().filter( ta -> ta.isSystemWideUnique() )
            .collect( Collectors.toList() );
    }

    private String validateImage( String uid )
    {
        FileResource fileResource = fileResourceService.getFileResource( uid );

        if ( fileResource == null )
        {
            return "Value '" + uid + "' is not the uid of a file";
        }
        else if ( !ValueType.VALID_IMAGE_FORMATS.contains( fileResource.getFormat() ) )
        {
            return "File resource with uid '" + uid + "' is not a valid image";
        }

        return null;
    }
}
