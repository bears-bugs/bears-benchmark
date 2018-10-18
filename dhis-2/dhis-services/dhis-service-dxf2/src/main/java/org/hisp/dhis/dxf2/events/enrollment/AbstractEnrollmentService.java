package org.hisp.dhis.dxf2.events.enrollment;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hisp.dhis.common.IdSchemes;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.common.IllegalQueryException;
import org.hisp.dhis.common.OrganisationUnitSelectionMode;
import org.hisp.dhis.common.Pager;
import org.hisp.dhis.common.exception.InvalidIdentifierReferenceException;
import org.hisp.dhis.commons.collection.CachingMap;
import org.hisp.dhis.dbms.DbmsManager;
import org.hisp.dhis.dxf2.common.ImportOptions;
import org.hisp.dhis.dxf2.events.TrackedEntityInstanceParams;
import org.hisp.dhis.dxf2.events.TrackerAccessManager;
import org.hisp.dhis.dxf2.events.event.Coordinate;
import org.hisp.dhis.dxf2.events.event.Event;
import org.hisp.dhis.dxf2.events.event.EventService;
import org.hisp.dhis.dxf2.events.event.Note;
import org.hisp.dhis.dxf2.events.trackedentity.Attribute;
import org.hisp.dhis.dxf2.events.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.dxf2.events.trackedentity.TrackedEntityInstanceService;
import org.hisp.dhis.dxf2.importsummary.ImportConflict;
import org.hisp.dhis.dxf2.importsummary.ImportStatus;
import org.hisp.dhis.dxf2.importsummary.ImportSummaries;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.hisp.dhis.i18n.I18nManager;
import org.hisp.dhis.importexport.ImportStrategy;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceQueryParams;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.ProgramStatus;
import org.hisp.dhis.program.ProgramTrackedEntityAttribute;
import org.hisp.dhis.query.Query;
import org.hisp.dhis.query.QueryService;
import org.hisp.dhis.query.Restrictions;
import org.hisp.dhis.schema.SchemaService;
import org.hisp.dhis.security.Authorities;
import org.hisp.dhis.system.callable.IdentifiableObjectCallable;
import org.hisp.dhis.system.util.DateUtils;
import org.hisp.dhis.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.trackedentity.TrackedEntityAttributeService;
import org.hisp.dhis.trackedentityattributevalue.TrackedEntityAttributeValue;
import org.hisp.dhis.trackedentityattributevalue.TrackedEntityAttributeValueService;
import org.hisp.dhis.trackedentitycomment.TrackedEntityComment;
import org.hisp.dhis.trackedentitycomment.TrackedEntityCommentService;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public abstract class AbstractEnrollmentService
    implements EnrollmentService
{
    @Autowired
    protected ProgramInstanceService programInstanceService;

    @Autowired
    protected ProgramStageInstanceService programStageInstanceService;

    @Autowired
    protected ProgramService programService;

    @Autowired
    protected TrackedEntityInstanceService trackedEntityInstanceService;

    @Autowired
    protected org.hisp.dhis.trackedentity.TrackedEntityInstanceService teiService;

    @Autowired
    protected TrackedEntityAttributeService trackedEntityAttributeService;

    @Autowired
    protected TrackedEntityAttributeValueService trackedEntityAttributeValueService;

    @Autowired
    protected CurrentUserService currentUserService;

    @Autowired
    protected TrackedEntityCommentService commentService;

    @Autowired
    protected IdentifiableObjectManager manager;

    @Autowired
    protected I18nManager i18nManager;

    @Autowired
    protected UserService userService;

    @Autowired
    protected DbmsManager dbmsManager;

    @Autowired
    protected EventService eventService;

    @Autowired
    protected TrackerAccessManager trackerAccessManager;

    @Autowired
    protected SchemaService schemaService;

    @Autowired
    protected QueryService queryService;

    private CachingMap<String, OrganisationUnit> organisationUnitCache = new CachingMap<>();

    private CachingMap<String, Program> programCache = new CachingMap<>();

    private CachingMap<String, TrackedEntityAttribute> trackedEntityAttributeCache = new CachingMap<>();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // -------------------------------------------------------------------------
    // READ
    // -------------------------------------------------------------------------

    public Enrollments getEnrollments( ProgramInstanceQueryParams params )
    {
        Enrollments enrollments = new Enrollments();

        if ( !params.isPaging() && !params.isSkipPaging() )
        {
            params.setDefaultPaging();
        }

        if ( params.isPaging() )
        {
            int count = 0;

            if ( params.isTotalPages() )
            {
                count = programInstanceService.countProgramInstances( params );
            }

            Pager pager = new Pager( params.getPageWithDefault(), count, params.getPageSizeWithDefault() );

            enrollments.setPager( pager );
        }

        List<ProgramInstance> programInstances = programInstanceService.getProgramInstances( params );
        enrollments.setEnrollments( getEnrollments( programInstances ) );

        return enrollments;
    }

    @Override
    public List<Enrollment> getEnrollments( Iterable<ProgramInstance> programInstances )
    {
        List<Enrollment> enrollments = new ArrayList<>();
        User user = currentUserService.getCurrentUser();

        for ( ProgramInstance programInstance : programInstances )
        {
            if ( programInstance != null && programInstance.getEntityInstance() != null
                && trackerAccessManager.canRead( user, programInstance ).isEmpty() )
            {
                enrollments.add( getEnrollment( user, programInstance, TrackedEntityInstanceParams.FALSE ) );
            }
        }

        return enrollments;
    }

    @Override
    public Enrollment getEnrollment( String id )
    {
        ProgramInstance programInstance = programInstanceService.getProgramInstance( id );
        return programInstance != null ? getEnrollment( programInstance ) : null;
    }

    @Override
    public Enrollment getEnrollment( ProgramInstance programInstance )
    {
        return getEnrollment( currentUserService.getCurrentUser(), programInstance, TrackedEntityInstanceParams.FALSE );
    }

    @Override
    public Enrollment getEnrollment( ProgramInstance programInstance, TrackedEntityInstanceParams params )
    {
        return getEnrollment( currentUserService.getCurrentUser(), programInstance, params );
    }

    @Override
    public Enrollment getEnrollment( User user, ProgramInstance programInstance, TrackedEntityInstanceParams params )
    {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollment( programInstance.getUid() );
        List<String> errors = trackerAccessManager.canRead( user, programInstance );

        if ( !errors.isEmpty() )
        {
            throw new IllegalQueryException( errors.toString() );
        }

        if ( programInstance.getEntityInstance() != null )
        {
            enrollment.setTrackedEntityType( programInstance.getEntityInstance().getTrackedEntityType().getUid() );
            enrollment.setTrackedEntityInstance( programInstance.getEntityInstance().getUid() );
        }

        if ( programInstance.getOrganisationUnit() != null )
        {
            enrollment.setOrgUnit( programInstance.getOrganisationUnit().getUid() );
            enrollment.setOrgUnitName( programInstance.getOrganisationUnit().getName() );
        }

        if ( programInstance.getProgram().getCaptureCoordinates() )
        {
            Coordinate coordinate = null;

            if ( programInstance.getLongitude() != null && programInstance.getLatitude() != null )
            {
                coordinate = new Coordinate( programInstance.getLongitude(), programInstance.getLatitude() );

                try
                {
                    List<Double> list = OBJECT_MAPPER.readValue( coordinate.getCoordinateString(), new TypeReference<List<Double>>()
                    {
                    } );

                    coordinate.setLongitude( list.get( 0 ) );
                    coordinate.setLatitude( list.get( 1 ) );
                }
                catch ( IOException ignored )
                {
                }
            }

            if ( coordinate != null && coordinate.isValid() )
            {
                enrollment.setCoordinate( coordinate );
            }
        }

        enrollment.setCreated( DateUtils.getIso8601NoTz( programInstance.getCreated() ) );
        enrollment.setCreatedAtClient( DateUtils.getIso8601NoTz( programInstance.getCreatedAtClient() ) );
        enrollment.setLastUpdated( DateUtils.getIso8601NoTz( programInstance.getLastUpdated() ) );
        enrollment.setLastUpdatedAtClient( DateUtils.getIso8601NoTz( programInstance.getLastUpdatedAtClient() ) );
        enrollment.setProgram( programInstance.getProgram().getUid() );
        enrollment.setStatus( EnrollmentStatus.fromProgramStatus( programInstance.getStatus() ) );
        enrollment.setEnrollmentDate( programInstance.getEnrollmentDate() );
        enrollment.setIncidentDate( programInstance.getIncidentDate() );
        enrollment.setFollowup( programInstance.getFollowup() );
        enrollment.setCompletedDate( programInstance.getEndDate() );
        enrollment.setCompletedBy( programInstance.getCompletedBy() );
        enrollment.setStoredBy( programInstance.getStoredBy() );
        enrollment.setDeleted( programInstance.isDeleted() );

        List<TrackedEntityComment> comments = programInstance.getComments();

        for ( TrackedEntityComment comment : comments )
        {
            Note note = new Note();

            note.setValue( comment.getCommentText() );
            note.setStoredBy( comment.getCreator() );

            if ( comment.getCreatedDate() != null )
            {
                note.setStoredDate( comment.getCreatedDate().toString() );
            }

            enrollment.getNotes().add( note );
        }

        if ( params.isIncludeEvents() )
        {
            for ( ProgramStageInstance programStageInstance : programInstance.getProgramStageInstances() )
            {
                if ( !programStageInstance.isDeleted() && trackerAccessManager.canRead( user, programStageInstance ).isEmpty() )
                {
                    enrollment.getEvents().add( eventService.getEvent( programStageInstance ) );
                }
            }
        }

        return enrollment;
    }

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------

    @Override
    public ImportSummaries addEnrollments( List<Enrollment> enrollments, ImportOptions importOptions, org.hisp.dhis.trackedentity.TrackedEntityInstance daoTrackedEntityInstance, boolean clearSession )
    {
        if ( importOptions == null )
        {
            importOptions = new ImportOptions();
        }

        User user = currentUserService.getCurrentUser();
        List<List<Enrollment>> partitions = Lists.partition( enrollments, FLUSH_FREQUENCY );

        ImportSummaries importSummaries = new ImportSummaries();

        for ( List<Enrollment> _enrollments : partitions )
        {
            prepareCaches( _enrollments, user );

            for ( Enrollment enrollment : _enrollments )
            {
                importSummaries.addImportSummary( addEnrollment( enrollment, importOptions, user, daoTrackedEntityInstance ) );
            }

            if ( clearSession && enrollments.size() >= FLUSH_FREQUENCY )
            {
                clearSession();
            }
        }

        return importSummaries;
    }

    @Override
    public ImportSummary addEnrollment( Enrollment enrollment, ImportOptions importOptions )
    {
        return addEnrollment( enrollment, importOptions, currentUserService.getCurrentUser(), null );
    }

    @Override
    public ImportSummary addEnrollment( Enrollment enrollment, ImportOptions importOptions, User user, org.hisp.dhis.trackedentity.TrackedEntityInstance daoTrackedEntityInstance )
    {
        String storedBy = enrollment.getStoredBy() != null && enrollment.getStoredBy().length() < 31 ?
            enrollment.getStoredBy() : (user != null ? user.getUsername() : "system-process");

        if ( importOptions == null )
        {
            importOptions = new ImportOptions();
        }

        if ( daoTrackedEntityInstance == null )
        {
            daoTrackedEntityInstance = getTrackedEntityInstance( enrollment.getTrackedEntityInstance() );
        }

        Program program = getProgram( importOptions.getIdSchemes(), enrollment.getProgram() );

        ImportSummary importSummary = validateRequest( program, daoTrackedEntityInstance, enrollment, importOptions );
        if ( importSummary.getStatus() != ImportStatus.SUCCESS )
        {
            return importSummary;
        }

        OrganisationUnit organisationUnit = getOrganisationUnit( importOptions.getIdSchemes(), enrollment.getOrgUnit() );

        List<String> errors = trackerAccessManager.canWrite( user, new ProgramInstance( program, daoTrackedEntityInstance, organisationUnit ) );
        if ( !errors.isEmpty() )
        {
            return new ImportSummary( ImportStatus.ERROR, errors.toString() );
        }

        ProgramInstance programInstance = programInstanceService.enrollTrackedEntityInstance( daoTrackedEntityInstance, program,
            enrollment.getEnrollmentDate(), enrollment.getIncidentDate(), organisationUnit, enrollment.getEnrollment() );

        importSummary = validateProgramInstance( program, programInstance, enrollment, importOptions );
        if ( importSummary.getStatus() != ImportStatus.SUCCESS )
        {
            return importSummary;
        }

        updateCoordinates( program, enrollment, programInstance );
        updateAttributeValues( enrollment, importOptions );
        updateDateFields( enrollment, programInstance );
        programInstance.setFollowup( enrollment.getFollowup() );
        programInstance.setStoredBy( storedBy );

        programInstanceService.updateProgramInstance( programInstance );

        saveTrackedEntityComment( programInstance, enrollment );

        importSummary.setReference( programInstance.getUid() );
        importSummary.getImportCount().incrementImported();

        importOptions.setStrategy( ImportStrategy.CREATE_AND_UPDATE );
        importSummary.setEvents( handleEvents( enrollment, programInstance, importOptions ) );

        return importSummary;
    }

    private ImportSummary validateProgramInstance( Program program, ProgramInstance programInstance, Enrollment enrollment, ImportOptions importOptions )
    {

        ImportSummary importSummary = new ImportSummary( enrollment.getEnrollment() );

        if ( programInstance == null )
        {
            importSummary.setStatus( ImportStatus.ERROR );
            importSummary.setDescription( "Could not enroll TrackedEntityInstance "
                + enrollment.getTrackedEntityInstance() + " into program " + enrollment.getProgram() );
            importSummary.incrementIgnored();

            return importSummary;
        }

        if ( program.getDisplayIncidentDate() && programInstance.getIncidentDate() == null )
        {
            importSummary.setStatus( ImportStatus.ERROR );
            importSummary.setDescription( "DisplayIncidentDate is true but IncidentDate is null " );
            importSummary.incrementIgnored();
        }

        return importSummary;
    }

    private ImportSummary validateRequest( Program program, org.hisp.dhis.trackedentity.TrackedEntityInstance entityInstance,
        Enrollment enrollment, ImportOptions importOptions )
    {
        ImportSummary importSummary = new ImportSummary( enrollment.getEnrollment() );

        ProgramInstanceQueryParams params = new ProgramInstanceQueryParams();
        params.setOrganisationUnitMode( OrganisationUnitSelectionMode.ALL );
        params.setSkipPaging( true );
        params.setProgram( program );
        params.setTrackedEntityInstance( entityInstance );
        params.setProgramStatus( ProgramStatus.ACTIVE );

        List<Enrollment> enrollments = getEnrollments( programInstanceService.getProgramInstances( params ) );

        if ( !enrollments.isEmpty() )
        {
            importSummary.setStatus( ImportStatus.ERROR );
            importSummary.setDescription( "TrackedEntityInstance " + entityInstance.getUid()
                + " already have an active enrollment in program " + program.getUid() );
            importSummary.incrementIgnored();

            return importSummary;
        }

        if ( program.getOnlyEnrollOnce() )
        {
            params.setProgramStatus( ProgramStatus.COMPLETED );

            enrollments = getEnrollments( programInstanceService.getProgramInstances( params ) );

            if ( !enrollments.isEmpty() )
            {
                importSummary.setStatus( ImportStatus.ERROR );
                importSummary.setDescription( "TrackedEntityInstance " + entityInstance.getUid()
                    + " already have a completed enrollment in program " + program.getUid() + ", and this program is" +
                    " configured to only allow enrolling one time." );
                importSummary.incrementIgnored();

                return importSummary;
            }
        }

        Set<ImportConflict> importConflicts = new HashSet<>( checkAttributes( enrollment, importOptions ) );

        if ( !importConflicts.isEmpty() )
        {
            importSummary.setConflicts( importConflicts );
            importSummary.setStatus( ImportStatus.ERROR );
            importSummary.incrementIgnored();
        }

        return importSummary;
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------

    @Override
    public ImportSummaries updateEnrollments( List<Enrollment> enrollments, ImportOptions importOptions, org.hisp.dhis.trackedentity.TrackedEntityInstance daoTrackedEntityInstance, boolean clearSession )
    {
        User user = currentUserService.getCurrentUser();
        List<List<Enrollment>> partitions = Lists.partition( enrollments, FLUSH_FREQUENCY );

        ImportSummaries importSummaries = new ImportSummaries();

        for ( List<Enrollment> _enrollments : partitions )
        {
            prepareCaches( _enrollments, user );

            for ( Enrollment enrollment : _enrollments )
            {
                importSummaries.addImportSummary( updateEnrollment( enrollment, importOptions, user, daoTrackedEntityInstance ) );
            }

            if ( clearSession && enrollments.size() >= FLUSH_FREQUENCY )
            {
                clearSession();
            }
        }

        return importSummaries;
    }

    @Override
    public ImportSummary updateEnrollment( Enrollment enrollment, ImportOptions importOptions )
    {
        return updateEnrollment( enrollment, importOptions, currentUserService.getCurrentUser(), null );
    }

    @Override
    public ImportSummary updateEnrollment( Enrollment enrollment, ImportOptions importOptions, User user, org.hisp.dhis.trackedentity.TrackedEntityInstance daoTrackedEntityInstance )
    {
        if ( importOptions == null )
        {
            importOptions = new ImportOptions();
        }

        if ( enrollment == null || enrollment.getEnrollment() == null )
        {
            return new ImportSummary( ImportStatus.ERROR, "No enrollment or enrollment ID was supplied" ).incrementIgnored();
        }

        ImportSummary importSummary = new ImportSummary( enrollment.getEnrollment() );

        ProgramInstance programInstance = programInstanceService.getProgramInstance( enrollment.getEnrollment() );

        if ( programInstance == null )
        {
            return new ImportSummary( ImportStatus.ERROR, "Enrollment ID was not valid." ).incrementIgnored();
        }

        List<String> errors = trackerAccessManager.canWrite( user, programInstance );

        if ( !errors.isEmpty() )
        {
            return new ImportSummary( ImportStatus.ERROR, errors.toString() );
        }

        Set<ImportConflict> importConflicts = new HashSet<>( checkAttributes( enrollment, importOptions ) );

        if ( !importConflicts.isEmpty() )
        {
            importSummary.setStatus( ImportStatus.ERROR );
            importSummary.setConflicts( importConflicts );
            importSummary.getImportCount().incrementIgnored();

            return importSummary;
        }

        if ( daoTrackedEntityInstance == null )
        {
            daoTrackedEntityInstance = getTrackedEntityInstance( enrollment.getTrackedEntityInstance() );
        }

        Program program = getProgram( importOptions.getIdSchemes(), enrollment.getProgram() );

        programInstance.setProgram( program );
        //TODO: Do I need to set TEI at all? Can it change? If not, then no need for it.
        programInstance.setEntityInstance( daoTrackedEntityInstance );

        if ( enrollment.getIncidentDate() != null )
        {
            programInstance.setIncidentDate( enrollment.getIncidentDate() );
        }

        if ( enrollment.getEnrollmentDate() != null )
        {
            programInstance.setEnrollmentDate( enrollment.getEnrollmentDate() );
        }

        if ( enrollment.getOrgUnit() != null )
        {
            OrganisationUnit organisationUnit = getOrganisationUnit( importOptions.getIdSchemes(), enrollment.getOrgUnit() );
            programInstance.setOrganisationUnit( organisationUnit );
        }

        programInstance.setFollowup( enrollment.getFollowup() );

        if ( program.getDisplayIncidentDate() && programInstance.getIncidentDate() == null )
        {
            importSummary.setStatus( ImportStatus.ERROR );
            importSummary.setDescription( "DisplayIncidentDate is true but IncidentDate is null " );
            importSummary.incrementIgnored();

            return importSummary;
        }

        updateCoordinates( program, enrollment, programInstance );

        if ( EnrollmentStatus.fromProgramStatus( programInstance.getStatus() ) != enrollment.getStatus() )
        {
            if ( EnrollmentStatus.CANCELLED == enrollment.getStatus() )
            {
                programInstanceService.cancelProgramInstanceStatus( programInstance );
            }
            else if ( EnrollmentStatus.COMPLETED == enrollment.getStatus() )
            {
                programInstanceService.completeProgramInstanceStatus( programInstance );
            }
            else if ( EnrollmentStatus.ACTIVE == enrollment.getStatus() )
            {
                programInstanceService.incompleteProgramInstanceStatus( programInstance );
            }
        }

        updateAttributeValues( enrollment, importOptions );
        updateDateFields( enrollment, programInstance );

        programInstanceService.updateProgramInstance( programInstance );
        //TODO: Do I need to update daoTrackedEntityInstance at all? Nothing changed as I am aware of.
        teiService.updateTrackedEntityInstance( daoTrackedEntityInstance );

        saveTrackedEntityComment( programInstance, enrollment );

        importSummary.setReference( enrollment.getEnrollment() );
        importSummary.getImportCount().incrementUpdated();

        importOptions.setStrategy( ImportStrategy.CREATE_AND_UPDATE );
        importSummary.setEvents( handleEvents( enrollment, programInstance, importOptions ) );

        return importSummary;
    }

    @Override
    public ImportSummary updateEnrollmentForNote( Enrollment enrollment )
    {
        if ( enrollment == null || enrollment.getEnrollment() == null )
        {
            return new ImportSummary( ImportStatus.ERROR, "No enrollment or enrollment ID was supplied" ).incrementIgnored();
        }

        ImportSummary importSummary = new ImportSummary( enrollment.getEnrollment() );

        ProgramInstance programInstance = programInstanceService.getProgramInstance( enrollment.getEnrollment() );

        if ( programInstance == null )
        {
            return new ImportSummary( ImportStatus.ERROR, "Enrollment ID was not valid." ).incrementIgnored();
        }

        saveTrackedEntityComment( programInstance, enrollment );

        importSummary.setReference( enrollment.getEnrollment() );
        importSummary.getImportCount().incrementUpdated();

        return importSummary;
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    @Override
    public ImportSummary deleteEnrollment( String uid )
    {
        User user = currentUserService.getCurrentUser();
        
        ProgramInstance programInstance = programInstanceService.getProgramInstance( uid );

        if ( programInstance != null )
        {            
            if( !programInstance.getProgramStageInstances().isEmpty() && user != null && !user.isAuthorized( Authorities.F_ENROLLMENT_CASCADE_DELETE.getAuthority() ) )
            {                
                return new ImportSummary( ImportStatus.ERROR, "The enrollment to be deleted has associated events. Deletion requires special authority: " + i18nManager.getI18n().getString( Authorities.F_ENROLLMENT_CASCADE_DELETE.getAuthority() ) ).incrementIgnored();                                
            }
            
            programInstanceService.deleteProgramInstance( programInstance );
            teiService.updateTrackedEntityInstance( programInstance.getEntityInstance() );
            return new ImportSummary( ImportStatus.SUCCESS, "Deletion of enrollment " + uid + " was successful." ).incrementDeleted();
        }

        return new ImportSummary( ImportStatus.ERROR, "ID " + uid + " does not point to a valid enrollment" ).incrementIgnored();
    }

    @Override
    public ImportSummaries deleteEnrollments( List<String> uids )
    {
        ImportSummaries importSummaries = new ImportSummaries();
        int counter = 0;

        for ( String uid : uids )
        {
            importSummaries.addImportSummary( deleteEnrollment( uid ) );

            if ( counter % FLUSH_FREQUENCY == 0 )
            {
                clearSession();
            }

            counter++;
        }

        return importSummaries;
    }

    @Override
    public void cancelEnrollment( String uid )
    {
        ProgramInstance programInstance = programInstanceService.getProgramInstance( uid );
        programInstanceService.cancelProgramInstanceStatus( programInstance );
        teiService.updateTrackedEntityInstance( programInstance.getEntityInstance() );
    }

    @Override
    public void completeEnrollment( String uid )
    {
        ProgramInstance programInstance = programInstanceService.getProgramInstance( uid );
        programInstanceService.completeProgramInstanceStatus( programInstance );
        teiService.updateTrackedEntityInstance( programInstance.getEntityInstance() );
    }

    @Override
    public void incompleteEnrollment( String uid )
    {
        ProgramInstance programInstance = programInstanceService.getProgramInstance( uid );
        programInstanceService.incompleteProgramInstanceStatus( programInstance );
        teiService.updateTrackedEntityInstance( programInstance.getEntityInstance() );
    }

    // -------------------------------------------------------------------------
    // HELPERS
    // -------------------------------------------------------------------------

    private ImportSummaries handleEvents( Enrollment enrollment, ProgramInstance programInstance, ImportOptions importOptions )
    {
        List<Event> create = new ArrayList<>();
        List<Event> update = new ArrayList<>();

        for ( Event event : enrollment.getEvents() )
        {
            event.setEnrollment( enrollment.getEnrollment() );
            event.setProgram( programInstance.getProgram().getUid() );
            event.setTrackedEntityInstance( enrollment.getTrackedEntityInstance() );

            if ( !programStageInstanceService.programStageInstanceExists( event.getEvent() ) )
            {
                create.add( event );
            }
            else
            {
                update.add( event );
            }
        }

        ImportSummaries importSummaries = new ImportSummaries();
        importSummaries.addImportSummaries( eventService.addEvents( create, importOptions, false ) );
        importSummaries.addImportSummaries( eventService.updateEvents( update, false, false ) );

        return importSummaries;
    }

    private void prepareCaches( List<Enrollment> enrollments, User user )
    {
        Collection<String> orgUnits = enrollments.stream().map( Enrollment::getOrgUnit ).collect( Collectors.toSet() );

        if ( !orgUnits.isEmpty() )
        {
            Query query = Query.from( schemaService.getDynamicSchema( OrganisationUnit.class ) );
            query.setUser( user );
            query.add( Restrictions.in( "id", orgUnits ) );
            queryService.query( query ).forEach( ou -> organisationUnitCache.put( ou.getUid(), (OrganisationUnit) ou ) );
        }

        Collection<String> programs = enrollments.stream().map( Enrollment::getProgram ).collect( Collectors.toSet() );

        if ( !programs.isEmpty() )
        {
            Query query = Query.from( schemaService.getDynamicSchema( Program.class ) );
            query.setUser( user );
            query.add( Restrictions.in( "id", programs ) );
            queryService.query( query ).forEach( pr -> programCache.put( pr.getUid(), (Program) pr ) );
        }

        Collection<String> trackedEntityAttributes = new HashSet<>();
        enrollments.forEach( e -> e.getAttributes().forEach( at -> trackedEntityAttributes.add( at.getAttribute() ) ) );

        if ( !trackedEntityAttributes.isEmpty() )
        {
            Query query = Query.from( schemaService.getDynamicSchema( TrackedEntityAttribute.class ) );
            query.setUser( user );
            query.add( Restrictions.in( "id", trackedEntityAttributes ) );
            queryService.query( query ).forEach( tea -> trackedEntityAttributeCache.put( tea.getUid(), (TrackedEntityAttribute) tea ) );
        }
    }

    private void updateCoordinates( Program program, Enrollment enrollment, ProgramInstance programInstance )
    {
        if ( program.getCaptureCoordinates() )
        {
            if ( enrollment.getCoordinate() != null && enrollment.getCoordinate().isValid() )
            {
                programInstance.setLatitude( enrollment.getCoordinate().getLatitude() );
                programInstance.setLongitude( enrollment.getCoordinate().getLongitude() );
            }
            else
            {
                programInstance.setLatitude( null );
                programInstance.setLongitude( null );
            }
        }
    }

    private List<ImportConflict> checkAttributes( Enrollment enrollment, ImportOptions importOptions )
    {
        List<ImportConflict> importConflicts = new ArrayList<>();

        Program program = getProgram( importOptions.getIdSchemes(), enrollment.getProgram() );
        org.hisp.dhis.trackedentity.TrackedEntityInstance trackedEntityInstance = teiService.getTrackedEntityInstance(
            enrollment.getTrackedEntityInstance() );

        Map<TrackedEntityAttribute, Boolean> mandatoryMap = Maps.newHashMap();
        Map<String, String> attributeValueMap = Maps.newHashMap();

        for ( ProgramTrackedEntityAttribute programTrackedEntityAttribute : program.getProgramAttributes() )
        {
            mandatoryMap.put( programTrackedEntityAttribute.getAttribute(), programTrackedEntityAttribute.isMandatory() );
        }

        // ignore attributes which do not belong to this program
        trackedEntityInstance.getTrackedEntityAttributeValues().stream().
            filter( value -> mandatoryMap.containsKey( value.getAttribute() ) ).
            forEach( value -> attributeValueMap.put( value.getAttribute().getUid(), value.getValue() ) );

        for ( Attribute attribute : enrollment.getAttributes() )
        {
            attributeValueMap.put( attribute.getAttribute(), attribute.getValue() );
            importConflicts.addAll( validateAttributeType( attribute, importOptions ) );
        }

        TrackedEntityInstance instance = trackedEntityInstanceService.getTrackedEntityInstance( enrollment.getTrackedEntityInstance() );

        for ( TrackedEntityAttribute trackedEntityAttribute : mandatoryMap.keySet() )
        {
            Boolean mandatory = mandatoryMap.get( trackedEntityAttribute );

            if ( mandatory && !attributeValueMap.containsKey( trackedEntityAttribute.getUid() ) )
            {
                importConflicts.add( new ImportConflict( "Attribute.attribute", "Missing mandatory attribute "
                    + trackedEntityAttribute.getUid() ) );
                continue;
            }

            if ( trackedEntityAttribute.isUnique() )
            {
                OrganisationUnit organisationUnit = manager.get( OrganisationUnit.class, instance.getOrgUnit() );

                importConflicts.addAll( checkScope( trackedEntityInstance, trackedEntityAttribute,
                    attributeValueMap.get( trackedEntityAttribute.getUid() ), organisationUnit, program ) );
            }

            attributeValueMap.remove( trackedEntityAttribute.getUid() );
        }

        if ( !attributeValueMap.isEmpty() )
        {
            importConflicts.add( new ImportConflict( "Attribute.attribute",
                "Only program attributes is allowed for enrollment " + attributeValueMap ) );
        }

        if ( !program.getSelectEnrollmentDatesInFuture() )
        {
            if ( Objects.nonNull( enrollment.getEnrollmentDate() ) && enrollment.getEnrollmentDate().after( new Date() ) )
            {
                importConflicts.add( new ImportConflict( "Enrollment.date", "Enrollment Date can't be future date :" + enrollment
                    .getEnrollmentDate() ) );
            }
        }

        if ( !program.getSelectIncidentDatesInFuture() )
        {
            if ( Objects.nonNull( enrollment.getIncidentDate() ) && enrollment.getIncidentDate().after( new Date() ) )
            {
                importConflicts.add( new ImportConflict( "Enrollment.incidentDate", "Incident Date can't be future date :" + enrollment
                    .getIncidentDate() ) );
            }
        }

        return importConflicts;
    }

    private List<ImportConflict> checkScope( org.hisp.dhis.trackedentity.TrackedEntityInstance trackedEntityInstance,
        TrackedEntityAttribute trackedEntityAttribute, String value, OrganisationUnit organisationUnit, Program program )
    {
        List<ImportConflict> importConflicts = new ArrayList<>();

        if ( trackedEntityAttribute == null || value == null )
        {
            return importConflicts;
        }

        String errorMessage = trackedEntityAttributeService.validateScope( trackedEntityAttribute, value, trackedEntityInstance,
            organisationUnit, program );

        if ( errorMessage != null )
        {
            importConflicts.add( new ImportConflict( "Attribute.value", errorMessage ) );
        }

        return importConflicts;
    }

    private void updateAttributeValues( Enrollment enrollment, ImportOptions importOptions )
    {
        org.hisp.dhis.trackedentity.TrackedEntityInstance trackedEntityInstance = teiService.
            getTrackedEntityInstance( enrollment.getTrackedEntityInstance() );
        Map<String, String> attributeValueMap = Maps.newHashMap();

        for ( Attribute attribute : enrollment.getAttributes() )
        {
            attributeValueMap.put( attribute.getAttribute(), attribute.getValue() );
        }

        trackedEntityInstance.getTrackedEntityAttributeValues().stream().
            filter( value -> attributeValueMap.containsKey( value.getAttribute().getUid() ) ).
            forEach( value ->
            {
                String newValue = attributeValueMap.get( value.getAttribute().getUid() );
                value.setValue( newValue );

                trackedEntityAttributeValueService.updateTrackedEntityAttributeValue( value );

                attributeValueMap.remove( value.getAttribute().getUid() );
            } );

        for ( String key : attributeValueMap.keySet() )
        {
            TrackedEntityAttribute attribute = getTrackedEntityAttribute( importOptions.getIdSchemes(), key );

            if ( attribute != null )
            {
                TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
                value.setValue( attributeValueMap.get( key ) );
                value.setAttribute( attribute );

                trackedEntityAttributeValueService.addTrackedEntityAttributeValue( value );
                trackedEntityInstance.addAttributeValue( value );
            }
        }
    }

    private org.hisp.dhis.trackedentity.TrackedEntityInstance getTrackedEntityInstance( String teiUID )
    {
        org.hisp.dhis.trackedentity.TrackedEntityInstance entityInstance = teiService.
            getTrackedEntityInstance( teiUID );

        if ( entityInstance == null )
        {
            throw new InvalidIdentifierReferenceException( "TrackedEntityInstance does not exist." );
        }

        return entityInstance;
    }

    private List<ImportConflict> validateAttributeType( Attribute attribute, ImportOptions importOptions )
    {
        List<ImportConflict> importConflicts = Lists.newArrayList();
        TrackedEntityAttribute teAttribute = getTrackedEntityAttribute( importOptions.getIdSchemes(), attribute.getAttribute() );

        if ( teAttribute == null )
        {
            importConflicts.add( new ImportConflict( "Attribute.attribute", "Does not point to a valid attribute." ) );
            return importConflicts;
        }

        String errorMessage = trackedEntityAttributeService.validateValueType( teAttribute, attribute.getValue() );

        if ( errorMessage != null )
        {
            importConflicts.add( new ImportConflict( "Attribute.value", errorMessage ) );
        }

        return importConflicts;
    }

    private void saveTrackedEntityComment( ProgramInstance programInstance, Enrollment enrollment )
    {
        String storedBy = currentUserService.getCurrentUsername();

        for ( Note note : enrollment.getNotes() )
        {
            TrackedEntityComment comment = new TrackedEntityComment();
            comment.setCreator( storedBy );
            comment.setCreatedDate( new Date() );
            comment.setCommentText( note.getValue() );

            commentService.addTrackedEntityComment( comment );

            programInstance.getComments().add( comment );

            programInstanceService.updateProgramInstance( programInstance );
        }
    }

    private OrganisationUnit getOrganisationUnit( IdSchemes idSchemes, String id )
    {
        return organisationUnitCache.get( id, new IdentifiableObjectCallable<>( manager, OrganisationUnit.class, idSchemes.getOrgUnitIdScheme(), id ) );
    }

    private Program getProgram( IdSchemes idSchemes, String id )
    {
        return programCache.get( id, new IdentifiableObjectCallable<>( manager, Program.class, idSchemes.getProgramIdScheme(), id ) );
    }

    private TrackedEntityAttribute getTrackedEntityAttribute( IdSchemes idSchemes, String id )
    {
        return trackedEntityAttributeCache.get( id, new IdentifiableObjectCallable<>( manager, TrackedEntityAttribute.class, idSchemes.getTrackedEntityAttributeIdScheme(), id ) );
    }

    private void clearSession()
    {
        organisationUnitCache.clear();
        programCache.clear();
        trackedEntityAttributeCache.clear();

        dbmsManager.clearSession();
    }

    private void updateDateFields( Enrollment enrollment, ProgramInstance programInstance )
    {
        programInstance.setAutoFields();

        Date createdAtClient = DateUtils.parseDate( enrollment.getCreatedAtClient() );

        if ( createdAtClient != null )
        {
            programInstance.setCreatedAtClient( createdAtClient );
        }

        String lastUpdatedAtClient = enrollment.getLastUpdatedAtClient();

        if ( lastUpdatedAtClient != null )
        {
            programInstance.setLastUpdatedAtClient( DateUtils.parseDate( lastUpdatedAtClient ) );
        }
    }
}
