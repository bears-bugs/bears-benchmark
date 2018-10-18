package org.hisp.dhis.system.util;

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lars Helge Overland
 */
public class JacksonUtils
{
    private final static ObjectMapper jsonMapper = new ObjectMapper();

    static
    {
        jsonMapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
        jsonMapper.disable( SerializationFeature.WRITE_EMPTY_JSON_ARRAYS );
        jsonMapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        jsonMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
    }

    public static <T> T fromJson( byte[] src, Class<T> clazz )
    {
        try
        {
            return jsonMapper.readValue( src, clazz );
        }
        catch ( IOException ex )
        {
            throw new UncheckedIOException( ex );
        }
    }
    
    public static <T> T fromJson( String string, Class<T> clazz )
    {
        try
        {
            return jsonMapper.readValue( string, clazz );
        }
        catch ( IOException ex )
        {
            throw new UncheckedIOException( ex );
        }
    }

    public static <T> String toJson( T object )
    {
        try
        {
            return jsonMapper.writeValueAsString( object );
        }
        catch ( IOException ex )
        {
            throw new UncheckedIOException( ex );
        }
    }

    public static <T, U> Map<T, U> fromJsonToMap( String object )
        throws IOException
    {
        TypeReference<HashMap<T, U>> typeRef = new TypeReference<HashMap<T, U>>() {};

        return jsonMapper.readValue( object, typeRef );
    }

    public static void fromObjectToReponse( HttpServletResponse response, Object clazz)
    {
        response.setStatus( HttpServletResponse.SC_ACCEPTED );
        response.setContentType( "application/json" );

        PrintWriter jsonResponse;
        try
        {
            jsonResponse = response.getWriter();
            jsonResponse.print( toJson( clazz ) );
            jsonResponse.flush();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
}
