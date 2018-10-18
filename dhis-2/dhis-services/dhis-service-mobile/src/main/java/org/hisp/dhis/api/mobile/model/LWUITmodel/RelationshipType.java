package org.hisp.dhis.api.mobile.model.LWUITmodel;

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

import org.hisp.dhis.api.mobile.model.Model;

public class RelationshipType
    extends Model
{
    private int id;

    private String aIsToB;

    private String bIsToA;
    
    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public void setId( int id )
    {
        this.id = id;
    }

    public String getAIsToB()
    {
        return aIsToB;
    }

    public void setAIsToB( String aIsToB )
    {
        this.aIsToB = aIsToB;
    }

    public String getBIsToA()
    {
        return bIsToA;
    }

    public void setBIsToA( String bIsToA )
    {
        this.bIsToA = bIsToA;
    }

    @Override
    public void serialize( DataOutputStream dataOutputStream )
        throws IOException
    {
        dataOutputStream.writeInt( this.id );
        dataOutputStream.writeUTF( this.getName() );
        dataOutputStream.writeUTF( this.aIsToB );
        dataOutputStream.writeUTF( this.bIsToA );
    }
    
    @Override
    public void deSerialize( DataInputStream dataInputStream )
        throws IOException
    {
        this.id = dataInputStream.readInt();
        this.setName( dataInputStream.readUTF() );
        this.aIsToB = dataInputStream.readUTF();
        this.bIsToA = dataInputStream.readUTF();
    }

    @Override
    public void serializeVersion2_8( DataOutputStream dataOutputStream )
        throws IOException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void serializeVersion2_9( DataOutputStream dataOutputStream )
        throws IOException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void serializeVersion2_10( DataOutputStream dataOutputStream )
        throws IOException
    {
        // TODO Auto-generated method stub
    }
}