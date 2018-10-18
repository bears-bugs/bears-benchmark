package org.hisp.dhis.api.mobile.model;

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class ProgramStage
    extends Model
{
    private String clientVersion;

    private List<DataElement> dataElements;

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    @Override
    public String getClientVersion()
    {
        return clientVersion;
    }

    @Override
    public void setClientVersion( String clientVersion )
    {
        this.clientVersion = clientVersion;
    }

    @Override
    public void serialize( DataOutputStream dout )
        throws IOException
    {
        if ( this.getClientVersion().equals( DataStreamSerializable.TWO_POINT_EIGHT ) )
        {
            this.serializeVersion2_8( dout );
        }
        else if ( this.getClientVersion().equals( DataStreamSerializable.TWO_POINT_NINE ) )
        {
            this.serializeVersion2_9( dout );
        }
        else if ( this.getClientVersion().equals( DataStreamSerializable.TWO_POINT_TEN ) )
        {
            this.serializeVersion2_10( dout );
        }
    }

    @Override
    public void serializeVersion2_8( DataOutputStream dout )
        throws IOException
    {
        dout.writeInt( this.getId() );
        dout.writeUTF( this.getName() );

        dout.writeInt( dataElements.size() );

        for ( DataElement de : dataElements )
        {
            de.setClientVersion( TWO_POINT_EIGHT );
            de.serialize( dout );
        }
    }

    @Override
    public void serializeVersion2_9( DataOutputStream dout )
        throws IOException
    {
        dout.writeInt( this.getId() );
        dout.writeUTF( this.getName() );

        dout.writeInt( dataElements.size() );

        for ( DataElement de : dataElements )
        {
            de.setClientVersion( TWO_POINT_NINE );
            de.serialize( dout );
        }
    }
    
    @Override
    public void serializeVersion2_10( DataOutputStream dout )
        throws IOException
    {
        dout.writeInt( this.getId() );
        dout.writeUTF( this.getName() );

        dout.writeInt( dataElements.size() );

        for ( DataElement de : dataElements )
        {
            de.setClientVersion( TWO_POINT_TEN );
            de.serialize( dout );
        }
    }

    @Override
    public void deSerialize( DataInputStream dataInputStream )
        throws IOException
    {
        // FIXME: Get implementation from client
    }

}