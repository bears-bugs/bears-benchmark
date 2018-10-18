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

public class SMSCommand
    implements DataStreamSerializable
{
    private String name;

    private String separator;

    private String codeSeparator;

    private List<SMSCode> smsCodes;

    private String clientVersion;

    private int dataSetId;

    @Override
    public void serialize( DataOutputStream dataOutputStream )
        throws IOException
    {
        this.serializeVersion2_10( dataOutputStream );
    }

    @Override
    public void deSerialize( DataInputStream dataInputStream )
        throws IOException
    {
        // no need for deserialize
    }

    @Override
    public void serializeVersion2_8( DataOutputStream dataOutputStream )
        throws IOException
    {
        // does not exist in version 2.8
    }

    @Override
    public void serializeVersion2_9( DataOutputStream dataOutputStream )
        throws IOException
    {
        // does not exist in version 2.9
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getSeparator()
    {
        return separator;
    }

    public void setSeparator( String separator )
    {
        this.separator = separator;
    }

    public String getCodeSeparator()
    {
        return codeSeparator;
    }

    public void setCodeSeparator( String codeSeparator )
    {
        this.codeSeparator = codeSeparator;
    }

    public List<SMSCode> getSmsCodes()
    {
        return smsCodes;
    }

    public void setSmsCodes( List<SMSCode> smsCodes )
    {
        this.smsCodes = smsCodes;
    }

    public String getClientVersion()
    {
        return clientVersion;
    }

    public void setClientVersion( String clientVersion )
    {
        this.clientVersion = clientVersion;
    }

    public int getDataSetId()
    {
        return dataSetId;
    }

    public void setDataSetId( int dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    @Override
    public void serializeVersion2_10( DataOutputStream dataOutputStream )
        throws IOException
    {
        dataOutputStream.writeUTF( this.name );
        if ( this.separator != null )
        {
            dataOutputStream.writeUTF( this.separator );
        }
        else
        {
            dataOutputStream.writeUTF( "" );
        }

        if ( this.codeSeparator != null )
        {
            dataOutputStream.writeUTF( this.codeSeparator );
        }
        else
        {
            dataOutputStream.writeUTF( "" );
        }
        
        dataOutputStream.writeInt( this.dataSetId );

        if ( this.smsCodes == null )
        {
            dataOutputStream.writeInt( 0 );
        }
        else
        {
            dataOutputStream.writeInt( smsCodes.size() );
            for ( SMSCode smsCode : smsCodes )
            {
                smsCode.serialize( dataOutputStream );
            }
        }
    }

}
