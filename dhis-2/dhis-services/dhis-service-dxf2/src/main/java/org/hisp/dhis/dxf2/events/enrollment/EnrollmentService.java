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

import org.hisp.dhis.dxf2.common.ImportOptions;
import org.hisp.dhis.dxf2.events.TrackedEntityInstanceParams;
import org.hisp.dhis.dxf2.importsummary.ImportSummaries;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceQueryParams;
import org.hisp.dhis.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.user.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public interface EnrollmentService
{
    int FLUSH_FREQUENCY = 100;

    // -------------------------------------------------------------------------
    // READ
    // -------------------------------------------------------------------------

    Enrollment getEnrollment( String id );

    Enrollment getEnrollment( ProgramInstance programInstance );

    Enrollment getEnrollment( ProgramInstance programInstance, TrackedEntityInstanceParams params );

    List<Enrollment> getEnrollments( Iterable<ProgramInstance> programInstances );

    Enrollments getEnrollments( ProgramInstanceQueryParams params );

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------

    ImportSummaries addEnrollmentsJson( InputStream inputStream, ImportOptions importOptions ) throws IOException;

    ImportSummaries addEnrollmentsXml( InputStream inputStream, ImportOptions importOptions ) throws IOException;

    Enrollment getEnrollment( User user, ProgramInstance programInstance, TrackedEntityInstanceParams params );

    ImportSummaries addEnrollments( List<Enrollment> enrollments, ImportOptions importOptions, TrackedEntityInstance trackedEntityInstance, boolean clearSession );

    ImportSummary addEnrollment( Enrollment enrollment, ImportOptions importOptions );

    ImportSummary addEnrollment( Enrollment enrollment, ImportOptions importOptions, User user, TrackedEntityInstance trackedEntityInstance );

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------

    ImportSummary updateEnrollmentJson( String id, InputStream inputStream, ImportOptions importOptions ) throws IOException;

    ImportSummary updateEnrollmentForNoteJson( String id, InputStream inputStream ) throws IOException;

    ImportSummary updateEnrollmentXml( String id, InputStream inputStream, ImportOptions importOptions ) throws IOException;

    ImportSummaries updateEnrollments( List<Enrollment> enrollments, ImportOptions importOptions, TrackedEntityInstance trackedEntityInstance, boolean clearSession );

    ImportSummary updateEnrollment( Enrollment enrollment, ImportOptions importOptions );

    ImportSummary updateEnrollment( Enrollment enrollment, ImportOptions importOptions, User user, TrackedEntityInstance trackedEntityInstance );

    ImportSummary updateEnrollmentForNote( Enrollment enrollment );

    void cancelEnrollment( String uid );

    void completeEnrollment( String uid );

    void incompleteEnrollment( String uid );

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    ImportSummary deleteEnrollment( String uid );

    ImportSummaries deleteEnrollments( List<String> uids );

}
