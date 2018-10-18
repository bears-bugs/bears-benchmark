package org.hisp.dhis.importexport.action.dxf2;

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
import org.hisp.dhis.commons.util.StreamUtils;
import org.hisp.dhis.dxf2.csv.CsvImportClass;
import org.hisp.dhis.dxf2.csv.CsvImportService;
import org.hisp.dhis.dxf2.gml.GmlImportService;
import org.hisp.dhis.dxf2.metadata.AtomicMode;
import org.hisp.dhis.dxf2.metadata.MetadataImportParams;
import org.hisp.dhis.dxf2.metadata.MetadataImportService;
import org.hisp.dhis.dxf2.metadata.objectbundle.ObjectBundleMode;
import org.hisp.dhis.importexport.ImportStrategy;
import org.hisp.dhis.importexport.action.util.ImportMetaDataCsvTask;
import org.hisp.dhis.importexport.action.util.ImportMetaDataGmlTask;
import org.hisp.dhis.importexport.action.util.ImportMetaDataTask;
import org.hisp.dhis.scheduling.JobConfiguration;
import org.hisp.dhis.scheduling.JobType;
import org.hisp.dhis.scheduling.SchedulingManager;
import org.hisp.dhis.schema.SchemaService;
import org.hisp.dhis.system.notification.Notifier;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class MetaDataImportAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    private MetadataImportService importService;

    @Autowired
    private CsvImportService csvImportService;

    @Autowired
    private GmlImportService gmlImportService;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private SchedulingManager schedulingManager;

    @Autowired
    private Notifier notifier;

    // -------------------------------------------------------------------------
    // Input
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

    private ImportStrategy strategy = ImportStrategy.CREATE_AND_UPDATE;

    public void setStrategy( String strategy )
    {
        this.strategy = ImportStrategy.valueOf( strategy );
    }

    private AtomicMode atomicMode = AtomicMode.NONE;

    public void setAtomicMode( AtomicMode atomicMode )
    {
        this.atomicMode = atomicMode;
    }

    private String importFormat;

    public void setImportFormat( String importFormat )
    {
        this.importFormat = importFormat;
    }

    private String classKey;

    public void setClassKey( String classKey )
    {
        this.classKey = classKey;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        strategy = strategy != null ? strategy : ImportStrategy.NEW_AND_UPDATES;

        User user = currentUserService.getCurrentUser();

        JobConfiguration jobId = new JobConfiguration( "inMemoryMetadataImport", JobType.METADATA_IMPORT, user.getUid(), true );

        notifier.clear( jobId );

        InputStream in = StreamUtils.wrapAndCheckCompressionFormat( new FileInputStream( upload ) );

        MetadataImportParams importParams = createMetadataImportParams( jobId, strategy, atomicMode, dryRun )
            .setFilename( uploadFileName );

        if ( "csv".equals( importFormat ) )
        {
            if ( classKey != null && CsvImportClass.classExists( classKey ) )
            {
                schedulingManager.executeJob( new ImportMetaDataCsvTask( importService, csvImportService, schemaService,
                    importParams, in, CsvImportClass.valueOf( classKey ) ) );
            }
        }
        else if ( "gml".equals( importFormat ) )
        {
            schedulingManager.executeJob( new ImportMetaDataGmlTask( gmlImportService, importParams, in ) );
        }
        else if ( "json".equals( importFormat ) || "xml".equals( importFormat ) )
        {
            schedulingManager
                .executeJob( new ImportMetaDataTask( importService, schemaService, importParams, in, importFormat ) );
        }

        return SUCCESS;
    }

    private MetadataImportParams createMetadataImportParams( JobConfiguration jobId, ImportStrategy strategy, AtomicMode atomicMode, boolean dryRun )
    {
        MetadataImportParams importParams = new MetadataImportParams();
        importParams.setId( jobId );
        importParams.setImportMode( dryRun ? ObjectBundleMode.VALIDATE : ObjectBundleMode.COMMIT );
        importParams.setAtomicMode( atomicMode );
        importParams.setImportStrategy( strategy );

        return importParams;
    }
}
