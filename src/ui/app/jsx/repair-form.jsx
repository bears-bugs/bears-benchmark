import React from "react";
import { WithContext as ReactTags } from 'react-tag-input';
import $ from "jquery";


const repairForm = React.createClass({

  propTypes: {
    addRepairSubject: React.PropTypes.object.isRequired,
    addRepairResult: React.PropTypes.object.isRequired,
    clusterNames: React.PropTypes.object.isRequired
  },

  getInitialState: function() {
    const isDev = window.top.location.pathname.includes('webpack-dev-server');
    const URL_PREFIX = isDev ? 'http://127.0.0.1:8080' : '';
    
    return {
      addRepairResultMsg: null, clusterNames: [], submitEnabled: false,
      clusterName: this.props.currentCluster!="all"?this.props.currentCluster:this.props.clusterNames[0], keyspace: "", tables: "", owner: null, segments: null,
      parallism: null, intensity: null, cause: null, incrementalRepair: null, formCollapsed: true, nodes: "", datacenters: "", blacklistedTables: "",
      nodeList: [], datacenterList: [], clusterStatus: {}, urlPrefix: URL_PREFIX, nodeSuggestions: [], datacenterSuggestions: [], tableSuggestions: [], 
      clusterTables: {}, blacklistSuggestions: [], tableList: [], blacklistList: [], keyspaceList: [], keyspaceSuggestions: [],
      blacklistReadOnly: false, tablelistReadOnly: false, advancedFormCollapsed: true
    };
  },

  componentWillMount: function() {
    this._repairResultSubscription = this.props.addRepairResult.subscribeOnNext(obs =>
      obs.subscribe(
        r => this.setState({addRepairResultMsg: null}),
        r => this.setState({addRepairResultMsg: r.responseText})
      )
    );

    this._clusterNamesSubscription = this.props.clusterNames.subscribeOnNext(obs =>
      obs.subscribeOnNext(names => {
        let previousNames = this.state.clusterNames;
        this.setState({clusterNames: names});
        if(names.length == 1) this.setState({clusterName: names[0]});
        if(previousNames.length == 0) {
          this._getClusterStatus();
        }
      })
    );

  },

  componentWillUnmount: function() {
    this._repairResultSubscription.dispose();
    this._clusterNamesSubscription.dispose();
  },


  _getClusterStatus: function() {
    let clusterName = this.state.clusterName;
    $.ajax({
          url: this.state.urlPrefix + '/cluster/' + encodeURIComponent(clusterName),
          method: 'GET',
          component: this,
          complete: function(data) {
            this.component.setState({clusterStatus: $.parseJSON(data.responseText)});
            this.component._getSuggestions();
          }
      });
    $.ajax({
      url: this.state.urlPrefix + '/cluster/' + encodeURIComponent(clusterName) + '/tables',
      method: 'GET',
      component: this,
      complete: function(data) {
        this.component.setState({clusterTables: $.parseJSON(data.responseText)});
        this.component._getKeyspaceSuggestions();
      }
    });
  },

  _getSuggestions: function() {
    var nodes = this.state.clusterStatus.nodes_status.endpointStates[0].endpointNames;
    nodes.sort();
    this.state.nodeSuggestions = nodes;
    
    var datacenters = Object.keys(this.state.clusterStatus.nodes_status.endpointStates[0].endpoints);
    datacenters.sort;
    this.setState({datacenterSuggestions: datacenters});
  },

  _getKeyspaceSuggestions: function() {
    this.setState({keyspaceSuggestions: Object.keys(this.state.clusterTables)});
  },

  _getTableSuggestions: function(ks) {
    this.setState({tableSuggestions: this.state.clusterTables[ks]});
  },

  _onAdd: function(e) {
    const repair = {
      clusterName: this.state.clusterName, keyspace: this.state.keyspace,
      owner: this.state.owner
    };
    if(this.state.tables) repair.tables = this.state.tables;
    if(this.state.segments) repair.segmentCount = this.state.segments;
    if(this.state.parallism) repair.repairParallelism = this.state.parallism;
    if(this.state.intensity) repair.intensity = this.state.intensity;
    if(this.state.cause) repair.cause = this.state.cause;
    if(this.state.incrementalRepair) repair.incrementalRepair = this.state.incrementalRepair;
    if(this.state.nodes) repair.nodes = this.state.nodes;
    if(this.state.datacenters) repair.datacenters = this.state.datacenters;
    if(this.state.blacklistedTables) repair.blacklistedTables = this.state.blacklistedTables;

    // Force incremental repair to FALSE if empty
    if(!this.state.incrementalRepair) repair.incrementalRepair = "false";

    this.props.addRepairSubject.onNext(repair);
  },

  _handleChange: function(e) {
    var v = e.target.value;
    var n = e.target.id.substring(3); // strip in_ prefix
    console.log(n + " = " + v)

    // update state
    const state = this.state;
    state[n] = v;
    this.replaceState(state);
    
    if (n == 'clusterName') {
      this._getClusterStatus();
    }
    
    // validate
    this._checkValidity();
  },

  _checkValidity: function() {
    console.log("Keyspaces : " + this.state.keyspaceList.length);
    const valid = this.state.keyspaceList.length > 0 && this.state.clusterName && this.state.owner 
                                      && ((this.state.datacenterList.length>0 && this.state.nodeList.length==0)
                                      || (this.state.datacenterList.length==0 && this.state.nodeList.length > 0) || (this.state.datacenterList.length==0  && this.state.nodeList==0) );
    this.setState({submitEnabled: valid});
  },

  _toggleFormDisplay: function() {
    if(this.state.formCollapsed == true) {
      this.setState({formCollapsed: false});
    }
    else {
      this.setState({formCollapsed: true});
    }
  },

  _toggleAdvancedSettingsDisplay: function() {
    if(this.state.advancedFormCollapsed == true) {
      this.setState({advancedFormCollapsed: false});
    }
    else {
      this.setState({advancedFormCollapsed: true});
    }
  },

  _handleAddition(node) {
    if (this.state.datacenterList.length == 0 && node.length > 1) {
      let nodes = this.state.nodeList;
      if ($.inArray(node, this.state.nodes.split(','))==-1) {
        nodes.push({
            id: nodes.length + 1,
            text: node
        });
        this.setState({nodeList: nodes, nodes: nodes.map(node => node.text).join(',')});
        this._checkValidity();
      }
    }
  },
  
  _handleDelete(i) {
        let nodes = this.state.nodeList;
        nodes.splice(i, 1);
        this.setState({nodeList: nodes, nodes: nodes.map(node => node.text).join(',')});
        this._checkValidity();
  },

  _handleDcAddition(dc) {
    if (this.state.nodeList.length == 0 && dc.length > 1) {
      let datacenters = this.state.datacenterList;
      if ($.inArray(dc, this.state.datacenters.split(','))==-1) {
        datacenters.push({
            id: datacenters.length + 1,
            text: dc
        });
        this.setState({datacenterList: datacenters, datacenters: datacenters.map(dc => dc.text).join(',')});
        this._checkValidity();
      }
    }
  },
  
  _handleDcDelete(i) {
        let datacenters = this.state.datacenterList;
        datacenters.splice(i, 1);
        this.setState({datacenterList: datacenters, datacenters: datacenters.map(dc => dc.text).join(',')});
        this._checkValidity();
  },

  _handleNodeFilterSuggestions(textInputValue, possibleSuggestionsArray) {
    var lowerCaseQuery = textInputValue.toLowerCase();
    let nodes = this.state.nodes;
 
    return possibleSuggestionsArray.filter(function(suggestion)  {
        return suggestion.toLowerCase().includes(lowerCaseQuery) && $.inArray(suggestion, nodes.split(','))==-1;
    })
  },

  _handleDcFilterSuggestions(textInputValue, possibleSuggestionsArray) {
    var lowerCaseQuery = textInputValue.toLowerCase();
    let datacenters = this.state.datacenters;
 
    return possibleSuggestionsArray.filter(function(suggestion)  {
        return suggestion.toLowerCase().includes(lowerCaseQuery) && $.inArray(suggestion, datacenters.split(','))==-1;
    })
  },

    // Blacklist tag list functions
    _handleBlacklistAddition(table) {
      if (this.state.tableList.length == 0 && table.length > 1) {
        let blacklist = this.state.blacklistList;
        if ($.inArray(table, this.state.blacklistedTables.split(','))==-1) {
          blacklist.push({
              id: this._create_UUID(),
              text: table
          });
          this.setState({blacklistList: blacklist, blacklistedTables: blacklist.map(table => table.text).join(',')});
          this._checkValidity();
          this.setState({tablelistReadOnly: true});
        }
      }
    },
  
    _handleBlacklistDelete(i) {
        let blacklist = this.state.blacklistList;
        blacklist.splice(i, 1);
        this.setState({blacklistList: blacklist, blacklistedTables: blacklist.map(table => table.text).join(',')});
        this._checkValidity();
        this.setState({tablelistReadOnly: (blacklist.length>0)});
    },
  
    _handleBlacklistFilterSuggestions(textInputValue, possibleSuggestionsArray) {
      var lowerCaseQuery = textInputValue.toLowerCase();
      let blacklist = this.state.blacklistedTables;
      let tables = this.state.tables;
   
      return possibleSuggestionsArray.filter(function(suggestion)  {
          return suggestion.toLowerCase().includes(lowerCaseQuery) && $.inArray(suggestion, blacklist.split(','))==-1
                 && $.inArray(suggestion, tables.split(','))==-1;
      })
    },
  
    // Tables tag list functions
    _handleTableAddition(table) {
      if (this.state.blacklistList.length == 0 && table.length > 1) { 
        let tables = this.state.tableList;
        if ($.inArray(table, this.state.tables.split(','))==-1) {
          tables.push({
              id: this._create_UUID(),
              text: table
          });
          this.setState({tableList: tables, tables: tables.map(table => table.text).join(',')});
          this._checkValidity();
          this.setState({blacklistReadOnly: true});
        }
      }
    },
  
    _handleTableDelete(i) {
        let tables = this.state.tableList;
        tables.splice(i, 1);
        this.setState({tableList: tables, tables: tables.map(table => table.text).join(',')});
        this._checkValidity();
        this.setState({blacklistReadOnly: (tables.length > 0)});
    },
  
    // Keyspace tag list functions
    _handleKeyspaceAddition(ks) {
      let keyspaces = this.state.keyspaceList;
      if (keyspaces.length==0) {
        if ($.inArray(ks, this.state.keyspace.split(','))==-1) {
          keyspaces.push({
              id: this._create_UUID(),
              text: ks
          });
          this.setState({keyspaceList: keyspaces, keyspace: ks, keyspaces: keyspaces.map(ks => ks.text).join(',')});
          this._checkValidity();
          this._getTableSuggestions(ks);
        }
      }
    },
  
    _handleKeyspaceDelete(i) {
        let keyspaces = this.state.keyspaceList;
        keyspaces.splice(i, 1);
        this.setState({keyspaceList: keyspaces, keyspace: "", keyspaces: keyspaces.map(ks => ks.text).join(',')});
        this._checkValidity();
        this._getTableSuggestions("");
    },

    _handleKeyspaceFilterSuggestions(textInputValue, possibleSuggestionsArray) {
      var lowerCaseQuery = textInputValue.toLowerCase();
      let keyspaces = this.state.keyspaceList;
   
      return possibleSuggestionsArray.filter(function(suggestion)  {
          return suggestion.toLowerCase().includes(lowerCaseQuery) && keyspaces.length==0;
      })
    },

    _create_UUID(){
      var dt = new Date().getTime();
      var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
          var r = (dt + Math.random()*16)%16 | 0;
          dt = Math.floor(dt/16);
          return (c=='x' ? r :(r&0x3|0x8)).toString(16);
      });
      return uuid;
    },

  render: function() {

    let addMsg = null;
    if(this.state.addRepairResultMsg) {
      addMsg = <div className="alert alert-danger" role="alert">{this.state.addRepairResultMsg}</div>
    }

    const clusterItems = this.state.clusterNames.sort().map(name =>
      <option key={name} value={name}>{name}</option>
    );

    let advancedMenuDownStyle = {
      display: "inline-block" 
    }

    let advancedMenuUpStyle = {
      display: "none" 
    }

    if(this.state.advancedFormCollapsed == false) {
      advancedMenuDownStyle = {
        display: "none"
      }
      advancedMenuUpStyle = {
        display: "inline-block"
      }
    }

    const keyspaceInputStyle = this.state.keyspaceList.length > 0 ? 'form-control-hidden':'form-control';

    const advancedSettingsHeader = <div className="panel-title" >
    <a href="#advanced-form" data-toggle="collapse" onClick={this._toggleAdvancedSettingsDisplay}>Advanced settings</a>
    &nbsp; <span className="glyphicon glyphicon-menu-down" aria-hidden="true" style={advancedMenuDownStyle}></span>
           <span className="glyphicon glyphicon-menu-up" aria-hidden="true" style={advancedMenuUpStyle}></span></div>


    const form = <div className="row">
        <div className="col-lg-12">

          <form className="form-horizontal form-condensed">

            <div className="form-group">
              <label htmlFor="in_clusterName" className="col-sm-3 control-label">Cluster*</label>
              <div className="col-sm-9 col-md-7 col-lg-5">
                <select className="form-control" id="in_clusterName"
                  onChange={this._handleChange} value={this.state.clusterName}>
                  {clusterItems}
                </select>
              </div>
            </div>

            <div className="form-group">
            <label htmlFor="in_keyspace" className="col-sm-3 control-label">Keyspace*</label>
            <div className="col-sm-9 col-md-7 col-lg-5">
              <ReactTags id={'in_keyspace'} tags={this.state.keyspaceList}
                suggestions={this.state.keyspaceSuggestions}
                labelField={'text'} handleAddition={this._handleKeyspaceAddition} 
                handleInputBlur={this._handleKeyspaceAddition} 
                handleDelete={this._handleKeyspaceDelete}
                placeholder={'Add a keyspace'}
                handleFilterSuggestions={this._handleKeyspaceFilterSuggestions}
                classNames={{
                    tagInputField: keyspaceInputStyle
                  }}/>
              </div>
            </div>
            
            <div className="form-group">
              <label htmlFor="in_owner" className="col-sm-3 control-label">Owner*</label>
              <div className="col-sm-9 col-md-7 col-lg-5">
                <input type="text" required className="form-control" value={this.state.owner}
                  onChange={this._handleChange} id="in_owner" placeholder="owner name for the repair run (any string)"/>
              </div>
            </div>
 

            <div className="form-group">
              <label htmlFor="in_cause" className="col-sm-3 control-label">Cause</label>
              <div className="col-sm-9 col-md-7 col-lg-5">
                <input type="text" className="form-control" value={this.state.cause}
                  onChange={this._handleChange} id="in_cause" placeholder="reason repair was started"/>
              </div>
            </div>

            <div className="form-group">
              <div className="col-sm-offset-1 col-sm-9">
                <div className="panel panel-info">
                  <div className="panel-heading">
                    {advancedSettingsHeader}
                  </div>
                  <div className="panel-body collapse" id="advanced-form">
                    <div className="form-group">
                    <label htmlFor="in_tables" className="col-sm-3 control-label">Tables</label>
                    <div className="col-sm-14 col-md-12 col-lg-9">
                      <ReactTags id={'in_tables'} tags={this.state.tableList}
                        suggestions={this.state.tableSuggestions}
                        labelField={'text'} handleAddition={this._handleTableAddition} 
                        handleInputBlur={this._handleTableAddition} 
                        handleDelete={this._handleTableDelete}
                        readOnly={this.state.tablelistReadOnly}
                        placeholder={'Add a table (optional)'}
                        handleFilterSuggestions={this._handleBlacklistFilterSuggestions}
                        classNames={{
                            tagInputField: 'form-control'
                          }}/>
                      </div>
                    </div>
                    <div className="form-group">
                    <label htmlFor="in_blacklist" className="col-sm-3 control-label">Blacklist</label>
                    <div className="col-sm-14 col-md-12 col-lg-9">
                      <ReactTags id={'in_blacklist'} tags={this.state.blacklistList}
                        suggestions={this.state.tableSuggestions}
                        labelField={'text'} handleAddition={this._handleBlacklistAddition} 
                        handleInputBlur={this._handleBlacklistAddition}
                        handleDelete={this._handleBlacklistDelete}
                        readOnly={this.state.blacklistReadOnly}
                        placeholder={'Add a table (optional)'}
                        handleFilterSuggestions={this._handleBlacklistFilterSuggestions}
                        classNames={{
                            tagInputField: 'form-control'
                          }}/>
                      </div>
                    </div>
                    <div className="form-group">
                      <label htmlFor="in_nodes" className="col-sm-3 control-label">Nodes</label>
                      <div className="col-sm-14 col-md-12 col-lg-9">
                      <ReactTags id={'in_nodes'} tags={this.state.nodeList}
                        suggestions={this.state.nodeSuggestions}
                        labelField={'text'} handleAddition={this._handleAddition} 
                        handleInputBlur={this._handleAddition} 
                        handleDelete={this._handleDelete}
                        placeholder={'Add a node (optional)'}
                        handleFilterSuggestions={this._handleNodeFilterSuggestions}
                        classNames={{
                            tagInputField: 'form-control'
                          }}/>
                      </div>
                    </div>
                    <div className="form-group">
                      <label htmlFor="in_datacenters" className="col-sm-3 control-label">Datacenters</label>
                      <div className="col-sm-14 col-md-12 col-lg-9">
                      <ReactTags id={'in_datacenters'} tags={this.state.datacenterList}
                      suggestions={this.state.datacenterSuggestions}
                      labelField={'text'} handleAddition={this._handleDcAddition} 
                      handleInputBlur={this._handleDcAddition} 
                      handleDelete={this._handleDcDelete}
                      placeholder={'Add a datacenter (optional)'}
                      handleFilterSuggestions={this._handleDcFilterSuggestions}
                      classNames={{
                          tagInputField: 'form-control'
                        }}/>
                      </div>
                    </div>
                    <div className="form-group">
                      <label htmlFor="in_segments" className="col-sm-3 control-label">Segments per node</label>
                      <div className="col-sm-14 col-md-12 col-lg-9">
                        <input type="number" className="form-control" value={this.state.segments}
                          onChange={this._handleChange} id="in_segments" placeholder="amount of segments per node to create for the repair run"/>
                      </div>
                    </div>
                    <div className="form-group">
                      <label htmlFor="in_parallism" className="col-sm-3 control-label">Parallism</label>
                      <div className="col-sm-14 col-md-12 col-lg-9">
                        <select className="form-control" id="in_parallism"
                          onChange={this._handleChange} value={this.state.parallism}>
                          <option value=""></option>
                          <option value="SEQUENTIAL">Sequential</option>
                          <option value="PARALLEL">Parallel</option>
                          <option value="DATACENTER_AWARE">DC-Aware</option>
                        </select>
                      </div>
                    </div>
                    <div className="form-group">
                      <label htmlFor="in_intensity" className="col-sm-3 control-label">Repair intensity</label>
                      <div className="col-sm-14 col-md-12 col-lg-9">
                        <input type="number" className="form-control" value={this.state.intensity}
                          min="0" max="1"
                          onChange={this._handleChange} id="in_intensity" placeholder="repair intensity"/>
                      </div>
                    </div>
                    <div className="form-group">
                      <label htmlFor="in_incrementalRepair" className="col-sm-3 control-label">Incremental</label>
                      <div className="col-sm-14 col-md-12 col-lg-9">
                        <select className="form-control" id="in_incrementalRepair"
                          onChange={this._handleChange} value={this.state.incrementalRepair}>
                          <option value="false">false</option>
                          <option value="true">true</option>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="form-group">
              <div className="col-sm-offset-3 col-sm-9">
                <button type="button" className="btn btn-warning" disabled={!this.state.submitEnabled}
                  onClick={this._onAdd}>Repair</button>
              </div>
            </div>            
          </form>

      </div>
    </div>

    

    let menuDownStyle = {
      display: "inline-block" 
    }

    let menuUpStyle = {
      display: "none" 
    }

    if(this.state.formCollapsed == false) {
      menuDownStyle = {
        display: "none"
      }
      menuUpStyle = {
        display: "inline-block"
      }
    }

    const formHeader = <div className="panel-title" ><a href="#repair-form" data-toggle="collapse" onClick={this._toggleFormDisplay}>Start a new repair</a>&nbsp; <span className="glyphicon glyphicon-menu-down" aria-hidden="true" style={menuDownStyle}></span><span className="glyphicon glyphicon-menu-up" aria-hidden="true" style={menuUpStyle}></span></div>





    return (<div className="panel panel-warning">
              <div className="panel-heading">
                {formHeader}
              </div>
              <div className="panel-body collapse" id="repair-form">
                {addMsg}
                {form}
              </div>
            </div>);
  }
});

export default repairForm;
