package org.hisp.dhis.dbms;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hisp.dhis.cache.HibernateCacheManager;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lars Helge Overland
 */
public class HibernateDbmsManager
    implements DbmsManager
{
    private static final Log log = LogFactory.getLog( HibernateDbmsManager.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    private HibernateCacheManager cacheManager;

    public void setCacheManager( HibernateCacheManager cacheManager )
    {
        this.cacheManager = cacheManager;
    }

    // -------------------------------------------------------------------------
    // DbmsManager implementation
    // -------------------------------------------------------------------------

    @Override
    public void emptyDatabase()
    {
        emptyTable( "translation" );
        emptyTable( "importobject" );
        emptyTable( "importdatavalue" );
        emptyTable( "constant" );
        emptyTable( "sqlview" );

        emptyTable( "smscodes" );
        emptyTable( "smscommandcodes" );
        emptyTable( "smscommands" );
        emptyTable( "incomingsms" );

        emptyTable( "datavalue_audit" );
        emptyTable( "datavalueaudit" );
        emptyTable( "datavalue" );
        emptyTable( "completedatasetregistration" );

        emptyTable( "pushanalysisrecipientusergroups" );
        emptyTable( "pushanalysis" );

        emptyTable( "dashboarditem_users" );
        emptyTable( "dashboarditem_resources" );
        emptyTable( "dashboarditem_reports" );
        emptyTable( "dashboard_items" );
        emptyTable( "dashboarditem" );
        emptyTable( "dashboardusergroupaccesses" );
        emptyTable( "dashboarduseraccesses" );
        emptyTable( "dashboard" );

        emptyTable( "interpretation_comments" );
        emptyTable( "interpretationcommenttranslations" );
        emptyTable( "interpretationcomment" );
        emptyTable( "interpretationtranslations" );
        emptyTable( "interpretationusergroupaccesses" );
        emptyTable( "interpretation" );

        emptyTable( "reportusergroupaccesses" );
        emptyTable( "report" );

        emptyTable( "reporttable_categorydimensions" );
        emptyTable( "reporttable_categoryoptiongroupsetdimensions" );
        emptyTable( "reporttable_columns" );
        emptyTable( "reporttable_datadimensionitems" );
        emptyTable( "reporttable_dataelementgroupsetdimensions" );
        emptyTable( "reporttable_filters" );
        emptyTable( "reporttable_itemorgunitgroups" );
        emptyTable( "reporttable_organisationunits" );
        emptyTable( "reporttable_orgunitgroupsetdimensions" );
        emptyTable( "reporttable_orgunitlevels" );
        emptyTable( "reporttable_periods" );
        emptyTable( "reporttable_rows" );
        emptyTable( "reporttableusergroupaccesses" );
        emptyTable( "reporttabletranslations" );
        emptyTable( "reporttable" );

        emptyTable( "chart_categorydimensions" );
        emptyTable( "chart_categoryoptiongroupsetdimensions" );
        emptyTable( "chart_datadimensionitems" );
        emptyTable( "chart_dataelementgroupsetdimensions" );
        emptyTable( "chart_filters" );
        emptyTable( "chart_itemorgunitgroups" );
        emptyTable( "chart_organisationunits" );
        emptyTable( "chart_orgunitgroupsetdimensions" );
        emptyTable( "chart_orgunitlevels" );
        emptyTable( "chart_periods" );
        emptyTable( "chartusergroupaccesses" );
        emptyTable( "charttranslations" );
        emptyTable( "chart" );

        emptyTable( "eventreport_attributedimensions" );
        emptyTable( "eventreport_columns" );
        emptyTable( "eventreport_dataelementdimensions" );
        emptyTable( "eventreport_filters" );
        emptyTable( "eventreport_itemorgunitgroups" );
        emptyTable( "eventreport_organisationunits" );
        emptyTable( "eventreport_orgunitgroupsetdimensions" );
        emptyTable( "eventreport_orgunitlevels" );
        emptyTable( "eventreport_periods" );
        emptyTable( "eventreport_programindicatordimensions" );
        emptyTable( "eventreport_rows" );
        emptyTable( "eventreportusergroupaccesses" );
        emptyTable( "eventreporttranslations" );
        emptyTable( "eventreport" );

        emptyTable( "eventchart_attributedimensions" );
        emptyTable( "eventchart_columns" );
        emptyTable( "eventchart_dataelementdimensions" );
        emptyTable( "eventchart_filters" );
        emptyTable( "eventchart_itemorgunitgroups" );
        emptyTable( "eventchart_organisationunits" );
        emptyTable( "eventchart_orgunitgroupsetdimensions" );
        emptyTable( "eventchart_orgunitlevels" );
        emptyTable( "eventchart_periods" );
        emptyTable( "eventchart_programindicatordimensions" );
        emptyTable( "eventchart_rows" );
        emptyTable( "eventchartusergroupaccesses" );
        emptyTable( "eventcharttranslations" );
        emptyTable( "eventchart" );
        
        emptyTable( "dataelementgroupsetdimension_items" );
        emptyTable( "dataelementgroupsetdimension" );
        emptyTable( "categoryoptiongroupsetdimension" );
        emptyTable( "categoryoptiongroupsetdimension_items" );
        emptyTable( "orgunitgroupsetdimension_items" );
        emptyTable( "orgunitgroupsetdimension" );

        emptyTable( "program_userroles" );

        emptyTable( "users_catdimensionconstraints" );
        emptyTable( "users_cogsdimensionconstraints" );
        emptyTable( "userrolemembers" );
        emptyTable( "userroledataset" );
        emptyTable( "userroleauthorities" );
        emptyTable( "userdatavieworgunits" );
        emptyTable( "usermembership" );
        emptyTable( "userrole" );

        emptyTable( "orgunitgroupsetmembers" );
        emptyTable( "orgunitgroupset" );
        emptyTable( "orgunitgroupsetusergroupaccesses" );

        emptyTable( "orgunitgroupmembers" );
        emptyTable( "orgunitgroup" );
        emptyTable( "orgunitgroupusergroupaccesses" );

        emptyTable( "validationrulegroupusergroupstoalert" );
        emptyTable( "validationrulegroupmembers" );
        emptyTable( "validationrulegroup" );
        emptyTable( "validationrulegroupusergroupaccesses" );

        emptyTable( "validationresult" );

        emptyTable( "validationrule" );
        emptyTable( "validationruleusergroupaccesses" );

        emptyTable( "dataapproval" );

        emptyTable( "lockexception" );

        emptyTable( "sectiondataelements" );
        emptyTable( "section" );

        emptyTable( "datasetsource" );
        emptyTable( "datasetelement" );
        emptyTable( "datasetindicators" );
        emptyTable( "datasetoperands" );
        emptyTable( "datasetusergroupaccesses" );
        emptyTable( "dataset" );

        emptyTable( "dataapprovalaudit" );
        emptyTable( "dataapprovalworkflowlevels" );
        emptyTable( "dataapprovalworkflow" );
        emptyTable( "dataapprovallevel" );

        emptyTable( "predictororgunitlevels" );
        emptyTable( "predictor" );

        emptyTable( "datadimensionitem" );

        emptyTable( "programrulevariable" );
        emptyTable( "programruleaction" );
        emptyTable( "programrule" );

        emptyTable( "trackedentitydatavalue" );
        emptyTable( "trackedentitydatavalueaudit" );
        emptyTable( "programstageinstance" );
        emptyTable( "programinstance" );
        emptyTable( "programnotificationtemplate" );
        emptyTable( "programstage_dataelements" );
        emptyTable( "programstagedataelement" );
        emptyTable( "programstage" );
        emptyTable( "program_organisationunits" );
        emptyTable( "programusergroupaccesses" );
        emptyTable( "program_attributes" );
        emptyTable( "programindicator" );
        emptyTable( "program" );

        emptyTable( "trackedentityattributevalue" );
        emptyTable( "trackedentityattributevalueaudit" );
        emptyTable( "trackedentityattribute" );
        emptyTable( "trackedentityinstance" );
        emptyTable( "trackedentity" );

        emptyTable( "minmaxdataelement" );
        emptyTable( "expressiondataelement" );
        emptyTable( "expressionsampleelement" );
        emptyTable( "expressionoptioncombo" );
        emptyTable( "calculateddataelement" );

        emptyTable( "dataelementgroupsetmembers" );
        emptyTable( "dataelementgroupsetusergroupaccesses" );
        emptyTable( "dataelementgroupset" );

        emptyTable( "dataelementgroupmembers" );
        emptyTable( "dataelementgroupusergroupaccesses" );
        emptyTable( "dataelementgroup" );

        emptyTable( "dataelementaggregationlevels" );
        emptyTable( "dataelementoperand" );
        emptyTable( "dataelementusergroupaccesses" );
        emptyTable( "dataelement" );

        emptyTable( "categoryoptioncombos_categoryoptions" );
        emptyTable( "categorycombos_optioncombos" );
        emptyTable( "categorycombos_categories" );
        emptyTable( "categories_categoryoptions" );

        emptyTable( "userteisearchorgunits" );
        emptyTable( "categoryoption_organisationunits" );
        emptyTable( "organisationunit" );
        emptyTable( "orgunitlevel" );

        emptyTable( "version" );
        emptyTable( "deletedobject" );
        emptyTable( "mocksource" );
        emptyTable( "period" );

        emptyTable( "indicatorgroupsetmembers" );
        emptyTable( "indicatorgroupsetusergroupaccesses" );
        emptyTable( "indicatorgroupset" );

        emptyTable( "indicatorgroupmembers" );
        emptyTable( "indicatorgroupusergroupaccesses" );
        emptyTable( "indicatorgroup" );

        emptyTable( "indicator" );
        emptyTable( "indicatortype" );

        emptyTable( "categoryoptiongroupsetmembers" );
        emptyTable( "categoryoptiongroupsetusergroupaccesses" );
        emptyTable( "categoryoptiongroupset" );

        emptyTable( "categoryoptiongroupmembers" );
        emptyTable( "categoryoptiongroupusergroupaccesses" );
        emptyTable( "categoryoptiongroup" );

        emptyTable( "dataelementcategoryoptionusergroupaccesses" );

        emptyTable( "expression" );
        emptyTable( "categoryoptioncombo" );
        emptyTable( "categorycombo" );
        emptyTable( "dataelementcategory" );
        emptyTable( "dataelementcategoryoption" );

        emptyTable( "optionvalue" );
        emptyTable( "optionset" );

        emptyTable( "systemsetting" );

        emptyTable( "attribute" );

        emptyTable( "messageconversation_usermessages" );
        emptyTable( "usermessage" );
        emptyTable( "messageconversation_messages" );
        emptyTable( "messageconversation" );
        emptyTable( "message" );

        emptyTable( "usergroupusergroupaccesses" );
        emptyTable( "usergroupaccess" );
        emptyTable( "usergroupmembers" );
        emptyTable( "usergroup" );

        emptyTable( "previouspasswords" );
        emptyTable( "users" );
        emptyTable( "useraccess" );
        emptyTable( "usersetting" );
        emptyTable( "userinfo" );

        dropTable( "_orgunitstructure" );
        dropTable( "_datasetorganisationunitcategory" );
        dropTable( "_categoryoptioncomboname" );
        dropTable( "_dataelementgroupsetstructure" );
        dropTable( "_indicatorgroupsetstructure" );
        dropTable( "_organisationunitgroupsetstructure" );
        dropTable( "_categorystructure" );
        dropTable( "_dataelementstructure" );
        dropTable( "_dateperiodstructure" );
        dropTable( "_periodstructure" );
        dropTable( "_dataelementcategoryoptioncombo" );
        dropTable( "_dataapprovalminlevel" );

        emptyTable( "reservedvalue" );
        emptyTable( "sequentialnumbercounter" );

        log.debug( "Cleared database contents" );

        cacheManager.clearCache();

        log.debug( "Cleared Hibernate cache" );
    }

    @Override
    public void clearSession()
    {
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }

    @Override
    public void flushSession()
    {
        sessionFactory.getCurrentSession().flush();
    }

    @Override
    public void emptyTable( String table )
    {
        try
        {
            jdbcTemplate.update( "delete from " + table );
        }
        catch ( BadSqlGrammarException ex )
        {
            log.debug( "Table " + table + " does not exist" );
        }
    }

    @Override
    public boolean tableExists( String tableName )
    {
        final String sql =
            "select table_name from information_schema.tables " +
                "where table_name = '" + tableName + "' " +
                "and table_type = 'BASE TABLE'";

        List<Object> tables = jdbcTemplate.queryForList( sql, Object.class );

        return tables != null && tables.size() > 0;
    }


    @Override
    public List<List<Object>> getTableContent( String table )
    {
        List<List<Object>> tableContent = new ArrayList<>();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet( "select * from " + table );
        int cols = sqlRowSet.getMetaData().getColumnCount() + 1;

        List<Object> headers = new ArrayList<>();

        for ( int i = 1; i < cols; i++ )
        {
            headers.add( sqlRowSet.getMetaData().getColumnName( i ) );
        }

        tableContent.add( headers );

        while ( sqlRowSet.next() )
        {
            List<Object> row = new ArrayList<>();

            for ( int i = 1; i < cols; i++ )
            {
                row.add( sqlRowSet.getObject( i ) );

            }

            tableContent.add( row );
        }

        return tableContent;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void dropTable( String table )
    {
        try
        {
            jdbcTemplate.execute( "drop table " + table );
        }
        catch ( BadSqlGrammarException ex )
        {
            log.debug( "Table " + table + " does not exist" );
        }
    }
}
