package org.hisp.dhis.importexport.action.event;

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

import com.opensymphony.xwork2.Action;
import org.hisp.dhis.common.IdentifiableProperty;
import org.hisp.dhis.commons.util.StreamUtils;
import org.hisp.dhis.dxf2.common.ImportOptions;
import org.hisp.dhis.dxf2.events.event.Event;
import org.hisp.dhis.dxf2.events.event.EventService;
import org.hisp.dhis.dxf2.events.event.Events;
import org.hisp.dhis.dxf2.events.event.ImportEventTask;
import org.hisp.dhis.dxf2.events.event.ImportEventsTask;
import org.hisp.dhis.dxf2.events.event.csv.CsvEventService;
import org.hisp.dhis.scheduling.JobConfiguration;
import org.hisp.dhis.scheduling.JobType;
import org.hisp.dhis.scheduling.SchedulingManager;
import org.hisp.dhis.system.notification.Notifier;
import org.hisp.dhis.user.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class ImportEventAction
    implements Action
{
    public static final String FORMAT_CSV = "csv";

    public static final String FORMAT_JSON = "json";

    public static final String FORMAT_XML = "xml";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    private SchedulingManager schedulingManager;

    @Autowired
    private Notifier notifier;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private EventService eventService;

    @Autowired
    private CsvEventService csvEventService;

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private File upload;

    public void setUpload( File upload )
    {
        this.upload = upload;
    }

    private String uploadFileName;

    public void setUploadFileName( String uploadFileName )
    {
        this.uploadFileName = uploadFileName;
    }

    private boolean dryRun;

    public void setDryRun( boolean dryRun )
    {
        this.dryRun = dryRun;
    }

    public String payloadFormat;

    public void setPayloadFormat( String payloadFormat )
    {
        this.payloadFormat = payloadFormat;
    }

    private IdentifiableProperty orgUnitIdScheme = IdentifiableProperty.UID;

    public void setOrgUnitIdScheme( IdentifiableProperty orgUnitIdScheme )
    {
        this.orgUnitIdScheme = orgUnitIdScheme;
    }

    private IdentifiableProperty eventIdScheme = IdentifiableProperty.UID;

    public void setEventIdScheme( IdentifiableProperty eventIdScheme )
    {
        this.eventIdScheme = eventIdScheme;
    }

    private boolean skipFirst;

    public void setSkipFirst( boolean skipFirst )
    {
        this.skipFirst = skipFirst;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        JobConfiguration jobId = new JobConfiguration( null, JobType.EVENT_IMPORT, currentUserService.getCurrentUser().getUid(), true );

        notifier.clear( jobId );

        InputStream in = new FileInputStream( upload );
        in = StreamUtils.wrapAndCheckCompressionFormat( in );

        ImportOptions importOptions = new ImportOptions()
            .setDryRun( dryRun )
            .setOrgUnitIdScheme( orgUnitIdScheme.toString() )
            .setEventIdScheme( eventIdScheme.toString() )
            .setFilename( uploadFileName );

        if ( FORMAT_CSV.equals( payloadFormat ) )
        {
            Events events = csvEventService.readEvents( in, skipFirst );
            schedulingManager.executeJob( new ImportEventsTask( events.getEvents(), eventService, importOptions, jobId ) );
        }
        else
        {
            List<Event> events;

            if ( FORMAT_JSON.equals( payloadFormat ) )
            {
                events = eventService.getEventsJson( in );
            }
            else
            {
                events = eventService.getEventsXml( in );
            }

            schedulingManager.executeJob( new ImportEventTask( events, eventService, importOptions, jobId ) );
        }

        return SUCCESS;
    }
}
