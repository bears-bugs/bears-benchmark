package org.hisp.dhis.dataadmin.action.sqlview;

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
import org.hisp.dhis.attribute.Attribute;
import org.hisp.dhis.attribute.AttributeService;
import org.hisp.dhis.attribute.comparator.AttributeSortOrderComparator;
import org.hisp.dhis.sqlview.SqlView;
import org.hisp.dhis.sqlview.SqlViewService;
import org.hisp.dhis.system.util.AttributeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Removes a existing regular expression from the database.
 *
 * @author Dang Duy Hieu
 */
public class GetSqlViewObjectAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SqlViewService sqlViewService;

    public void setSqlViewService( SqlViewService sqlViewService )
    {
        this.sqlViewService = sqlViewService;
    }

    @Autowired
    private AttributeService attributeService;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private SqlView sqlViewObject;

    public SqlView getSqlViewObject()
    {
        return sqlViewObject;
    }

    private List<Attribute> attributes;

    public List<Attribute> getAttributes()
    {
        return attributes;
    }

    private Map<Integer, String> attributeValues = new HashMap<>();

    public Map<Integer, String> getAttributeValues()
    {
        return attributeValues;
    }

    // -------------------------------------------------------------------------
    // Action
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        sqlViewObject = sqlViewService.getSqlView( id );

        attributeValues = AttributeUtils.getAttributeValueMap( sqlViewObject.getAttributeValues() );

        attributes = new ArrayList<>( attributeService.getAttributes( SqlView.class ) );

        Collections.sort( attributes, AttributeSortOrderComparator.INSTANCE );

        return SUCCESS;
    }
}
