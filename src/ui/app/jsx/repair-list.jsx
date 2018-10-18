import React from "react";
import moment from "moment";
import {RowDeleteMixin, RowAbortMixin, StatusUpdateMixin, DeleteStatusMessageMixin, CFsListRender} from "jsx/mixin";
import ProgressBar from 'react-bootstrap/lib/ProgressBar';
import Button from 'react-bootstrap/lib/Button';
import Modal from 'react-bootstrap/lib/Modal';
import segmentList from 'jsx/segment-list'

const TableRow = React.createClass({
  mixins: [RowDeleteMixin, StatusUpdateMixin, RowAbortMixin],

  _viewSegments: function(id) {
    console.log("Segments for run " + id );
    this.props.showSegments(id);
  },

  segmentsButton: function(id) {
    return <Button bsSize="xs" bsStyle="info" onClick={() => this._viewSegments(id)}>View segments</Button>
  },

  render: function() {
    let progressStyle = {
      marginTop: "0.25em",
      marginBottom: "0.25em"
    }
    let startTime = null;
    if(this.props.row.start_time) {
      startTime = moment(this.props.row.start_time).format("LLL");
    }
    const rowID = `#details_${this.props.row.id}`;
    const progressID = `#progress_${this.props.row.id}`;
    const segsRepaired = this.props.row.segments_repaired;
    const segsTotal = this.props.row.total_segments;
    const segsPerc = (100/segsTotal)*segsRepaired;
    const state = this.props.row.state;
    let etaOrDuration = moment(this.props.row.estimated_time_of_arrival).from(moment(this.props.row.current_time));
    if (!(state == 'RUNNING' || state == 'PAUSED')) {
      etaOrDuration = this.props.row.duration;
    } else if (segsPerc < 5) {
      etaOrDuration = 'TBD';
    }

    let progressStyleColor = "success";
    if (state == 'PAUSED') {
      progressStyleColor = "info";
    }
    else if (state != 'DONE' && state != 'RUNNING'){
      progressStyleColor = "danger";
    }


    
    const btnStartStop = this.props.row.state == 'ABORTED' ? null : this.statusUpdateButton();
    const btnAbort = state == 'RUNNING' || state == 'PAUSED' ? this.abortButton() : this.deleteButton();
    const btnSegment = this.segmentsButton(this.props.row.id);
    const active = state == 'RUNNING';
    let repairProgress = <ProgressBar now={Math.round((segsRepaired*100)/segsTotal)} active={active} bsStyle={progressStyleColor} 
                   label={segsRepaired + '/' + segsTotal}
                   key={progressID}/>

    return (
    <tr>
        <td data-toggle="collapse" data-target={rowID}>{startTime}</td>
        <td data-toggle="collapse" data-target={rowID}>{etaOrDuration}</td>
        <td data-toggle="collapse" data-target={rowID}>{this.props.row.state}</td>
        <td data-toggle="collapse" data-target={rowID}>{this.props.row.cluster_name}</td>
        <td data-toggle="collapse" data-target={rowID}>{this.props.row.keyspace_name}</td>
        <td data-toggle="collapse" data-target={rowID}><CFsListRender list={this.props.row.column_families} /></td>
        <td data-toggle="collapse" data-target={rowID}>
          <div className="progress">
          {repairProgress}
          </div>
        </td>
        <td>
          {btnSegment}
          {btnStartStop}
          {btnAbort}
        </td>
    </tr>
    );
  }

});

const TableRowDetails = React.createClass({
  getInitialState() {
      return { intensity: this.props.row.intensity };
  },

  _handleIntensityChange: function(e) {
      const value = e.target.value;
      const state = this.state;
      state["intensity"] = value;
      this.replaceState(state);

      this.props.updateIntensitySubject.onNext({id: this.props.row.id, intensity: value});
  },

  render: function() {

    const rowID = `details_${this.props.row.id}`;
    const createdAt = moment(this.props.row.creation_time).format("LLL");
    let startTime = null;
    if(this.props.row.start_time) {
      startTime = moment(this.props.row.start_time).format("LLL");
    }
    let endTime = null;
    if(this.props.row.end_time 
      && this.props.row.state != 'RUNNING' 
      && this.props.row.state != 'PAUSED'
      && this.props.row.state != 'NOT_STARTED') {
      if (this.props.row.state == 'ABORTED') {
        endTime = moment(this.props.row.pause_time).format("LLL");
      } else {
        endTime = moment(this.props.row.end_time).format("LLL");
      }
    }
    let pauseTime = null;
    if(this.props.row.pause_time
      && this.props.row.state != 'RUNNING' 
      && this.props.row.state != 'NOT_STARTED') {
      pauseTime = moment(this.props.row.pause_time).format("LLL");
    }
    
    let duration = this.props.row.duration;


    const incremental = this.props.row.incremental_repair == true ? "true" : "false";

    let intensity = this.props.row.intensity;
    if (this.props.row.state === 'PAUSED' && !!this.props.updateIntensitySubject) {
      intensity =
        <input
          type="number"
          className="form-control"
          value={this.state.intensity}
          min="0" max="1"
          onChange={this._handleIntensityChange}
          id={`${rowID}_in_intensity`}
          placeholder="repair intensity" />;
    }

    return (
      <tr id={rowID} className="collapse out">
        <td colSpan="7">
          <table className="table table-condensed">
            <tbody>
                <tr>
                    <td>ID</td>
                    <td>{this.props.row.id}</td>
                </tr>
                <tr>
                    <td>Owner</td>
                    <td>{this.props.row.owner}</td>
                </tr>
                <tr>
                    <td>Cause</td>
                    <td>{this.props.row.cause}</td>
                </tr>
                <tr>
                    <td>Last event</td>
                    <td>{this.props.row.last_event}</td>
                </tr>
                <tr>
                    <td>Start time</td>
                    <td>{startTime}</td>
                </tr>
                <tr>
                    <td>End time</td>
                    <td>{endTime}</td>
                </tr>
                <tr>
                    <td>Pause time</td>
                    <td>{pauseTime}</td>
                </tr>
                <tr>
                    <td>Duration</td>
                    <td>{duration}</td>
                </tr>
                <tr>
                    <td>Segment count</td>
                    <td>{this.props.row.total_segments}</td>
                </tr>
                <tr>
                    <td>Segment repaired</td>
                    <td>{this.props.row.segments_repaired}</td>
                </tr>
                <tr>
                    <td>Intensity</td>
                    <td>{intensity}</td>
                </tr>
                <tr>
                    <td>Repair parallism</td>
                    <td>{this.props.row.repair_parallelism}</td>
                </tr>
                <tr>
                    <td>Incremental repair</td>
                    <td>{incremental}</td>
                </tr>
                <tr>
                    <td>Nodes</td>
                    <td><CFsListRender list={this.props.row.nodes} /></td>
                </tr>
                <tr>
                    <td>Datacenters</td>
                    <td><CFsListRender list={this.props.row.datacenters}/></td>
                </tr>
                <tr>
                    <td>Blacklist</td>
                    <td><CFsListRender list={this.props.row.blacklisted_tables} /></td>
                </tr>
                <tr>
                    <td>Creation time</td>
                    <td>{createdAt}</td>
                </tr>
            </tbody>
          </table>
        </td>
      </tr>
    );
  },

});

const repairList = React.createClass({
  mixins: [DeleteStatusMessageMixin],

  propTypes: {
    repairs: React.PropTypes.object.isRequired,
    clusterNames: React.PropTypes.object.isRequired,
    deleteSubject: React.PropTypes.object.isRequired,
    deleteResult: React.PropTypes.object.isRequired,
    updateStatusSubject: React.PropTypes.object.isRequired,
    updateIntensitySubject: React.PropTypes.object.isRequired,
    currentCluster: React.PropTypes.string.isRequired,
    changeCurrentCluster: React.PropTypes.func.isRequired

  },

  getInitialState: function() {
    return {repairs: [], deleteResultMsg: null, clusterNames:[], 
      currentCluster:this.props.currentCluster, 
      runningCollapsed: false, doneCollapsed: false,
      modalShow: false, repairRunId: '',
      height: 0, width: 0,
      numberOfElementsToDisplay: 10};
  },

  componentWillMount: function() {
    this._clusterNamesSubscription = this.props.clusterNames.subscribeOnNext(obs =>
      obs.subscribeOnNext(names => this.setState({clusterNames: names}))
    );

    this._repairsSubscription = this.props.repairs.subscribeOnNext(obs =>
      obs.subscribeOnNext(repairs => {
        const sortedRepairs = Array.from(repairs);
        sortedRepairs.sort((a, b) => a.id - b.id);
        this.setState({repairs: sortedRepairs});
      })
    );

    window.addEventListener('resize', this.updateWindowDimensions);
    this.updateWindowDimensions();
  },

  componentWillUnmount: function() {
    this._repairsSubscription.dispose();
    this._clustersSubscription.dispose();
    window.removeEventListener('resize', this.updateWindowDimensions);
  },

  updateWindowDimensions: function() {
    this.setState({ width: window.innerWidth, height: window.innerHeight });
  },

  _handleChange: function(e) {
    var v = e.target.value;
    var n = e.target.id.substring(3); // strip in_ prefix

    // update state
    const state = this.state;
    state[n] = v;
    this.replaceState(state);

    // validate
    const valid = state.currentCluster;
    this.setState({submitEnabled: valid});
    this.props.changeCurrentCluster(this.state.currentCluster);
    console.log("changed cluster to " + this.state.currentCluster);
  },

  _toggleRunningDisplay: function() {
    if(this.state.runningCollapsed == true) {
      this.setState({runningCollapsed: false});
    }
    else {
      this.setState({runningCollapsed: true});
    }
  },

  _toggleDoneDisplay: function() {
    if(this.state.doneCollapsed == true) {
      this.setState({doneCollapsed: false});
    }
    else {
      this.setState({doneCollapsed: true});
    }
  },

  _displaySegments: function(repairRunId) {
    console.log("Displaying segments for run " + repairRunId)
    this.setState({ modalShow: true, repairRunId: repairRunId });
  },

  render: function() {

    let rowsRunning = <div className="clusterLoader"></div>
    let rowsDone = <div className="clusterLoader"></div>

    function compareEndTimeReverse(a,b) {
      if (a.end_time < b.end_time)
        return 1;
      if (a.end_time > b.end_time)
        return -1;
      return 0;
    }

    function compareStartTimeReverse(a,b) {
      if (a.start_time < b.start_time)
        return 1;
      if (a.start_time > b.start_time)
        return -1;
      return 0;
    }

    let modalClose = () => this.setState({ modalShow:false, repairRunId: ''});

    const segmentModal = <SegmentModal show={this.state.modalShow} onHide={modalClose} repairRunId={this.state.repairRunId} height={this.state.height} width={this.state.width}/>;
    rowsRunning = this.state.repairs.sort(compareStartTimeReverse)
      .filter(repair => this.state.currentCluster == "all" || this.state.currentCluster == repair.cluster_name)
      .filter(repair => (repair.state == "RUNNING" || repair.state == "PAUSED" || repair.state == "NOT_STARTED"))
      .map(repair =>
      <tbody key={repair.id+'-rows'}>
      <TableRow row={repair} key={repair.id+'-head'}
        deleteSubject={this.props.deleteSubject}
        updateStatusSubject={this.props.updateStatusSubject} showSegments={this._displaySegments}/>
      <TableRowDetails row={repair} key={repair.id+'-details'} updateIntensitySubject={this.props.updateIntensitySubject} />
      </tbody>
    );

    rowsDone = this.state.repairs.sort(compareEndTimeReverse)
                                 .filter(repair => this.state.currentCluster == "all" || this.state.currentCluster == repair.cluster_name)
                                 .filter(repair => (repair.state != "RUNNING" && repair.state != "PAUSED" && repair.state != "NOT_STARTED"))
                                 .slice(0, this.state.numberOfElementsToDisplay)
                                 .map(repair =>
      <tbody key={repair.id+'-rows'}>
      <TableRow row={repair} key={repair.id+'-head'}
        deleteSubject={this.props.deleteSubject}
        updateStatusSubject={this.props.updateStatusSubject} showSegments={this._displaySegments}/>
      <TableRowDetails row={repair} key={repair.id+'-details'}/>
      </tbody>
    );

    const clusterItems = this.state.clusterNames.sort().map(name =>
      <option key={name} value={name}>{name}</option>
    );

    const clusterFilter = <form className="form-horizontal form-condensed">
            <div className="form-group">
              <label htmlFor="in_currentCluster" className="col-sm-3 control-label">Filter cluster :</label>
              <div className="col-sm-7 col-md-5 col-lg-3">
                <select className="form-control" id="in_currentCluster"
                  onChange={this._handleChange} value={this.state.currentCluster}>
                  <option key="all" value="all">All</option>
                  {clusterItems}
                </select>
              </div>
              <label htmlFor="in_numberOfElementsToDisplay" className="col-sm-1 control-label">Display :</label>
              <div className="col-sm-5 col-md-3 col-lg-1">
                <select className="form-control" id="in_numberOfElementsToDisplay"
                  onChange={this._handleChange} value={this.state.numberOfElementsToDisplay}>
                  <option key="10" value="10">Last 10</option>
                  <option key="25" value="25">Last 25</option>
                  <option key="50" value="50">Last 50</option>
                  <option key="100" value="100">Last 100</option>
                </select>
              </div>
            </div>
    </form>

    let tableRunning = null;
    if(rowsRunning.length == 0) {
      tableRunning = <div className="alert alert-info" role="alert">No running repair runs found</div>
    } else {

      tableRunning = <div className="row">
          <div className="col-sm-12">
              <div className="table-responsive">
                  <table className="table table-bordered table-hover table-striped">
                      <thead>
                          <tr>
                              <th>Start</th>
                              <th>ETA</th>
                              <th>State</th>
                              <th>Cluster</th>
                              <th>Keyspace</th>
                              <th>CFs</th>
                              <th>Repaired</th>
                              <th></th>
                          </tr>
                      </thead>
                        {rowsRunning}
                  </table>
              </div>
          </div>
      </div>;
    }

    let tableDone = null;
    if(rowsDone.length == 0) {
      tableDone = <div className="alert alert-info" role="alert">No past repair runs found</div>
    } else {

      tableDone = <div className="row">
          <div className="col-sm-12">
              <div className="table-responsive">
                  <table className="table table-bordered table-hover table-striped">
                      <thead>
                          <tr>
                              <th>Start</th>
                              <th>Duration</th>
                              <th>State</th>
                              <th>Cluster</th>
                              <th>Keyspace</th>
                              <th>CFs</th>
                              <th>Repaired</th>
                              <th></th>
                          </tr>
                      </thead>
                        {rowsDone}
                  </table>
              </div>
          </div>
      </div>;
    }

    let menuRunningDownStyle = {
      display: "none" 
    }

    let menuRunningUpStyle = {
      display: "inline-block" 
    }

    if(this.state.runningCollapsed == true) {
      menuRunningDownStyle = {
        display: "inline-block"
      }
      menuRunningUpStyle = {
        display: "none"
      }
    }


    let menuDoneDownStyle = {
      display: "inline-block" 
    }

    let menuDoneUpStyle = {
      display: "none" 
    }

    if(this.state.doneCollapsed == true) {
      menuDoneDownStyle = {
        display: "none"
      }
      menuDoneUpStyle = {
        display: "inline-block"
      }
    }

    const runningHeader = <div className="panel-title"><a href="#repairs-running" data-toggle="collapse" onClick={this._toggleRunningDisplay}>Running</a>&nbsp; <span className="glyphicon glyphicon-menu-down" aria-hidden="true" style={menuRunningDownStyle}></span><span className="glyphicon glyphicon-menu-up" aria-hidden="true" style={menuRunningUpStyle}></span></div>
    const doneHeader = <div className="panel-title"><a href="#repairs-done" data-toggle="collapse" onClick={this._toggleDoneDisplay}>Done</a>&nbsp; <span className="glyphicon glyphicon-menu-down" aria-hidden="true" style={menuDoneDownStyle}></span><span className="glyphicon glyphicon-menu-up" aria-hidden="true" style={menuDoneUpStyle}></span></div>



    return (
            <div>
              {segmentModal}
              {clusterFilter}
              <div className="panel panel-primary">
                <div className="panel-heading">
                  {runningHeader}
                </div>
                <div className="panel-body collapse in" id="repairs-running">
                  {tableRunning}
                </div>
              </div>
              <div className="panel panel-success">
                <div className="panel-heading">
                  {doneHeader}
                </div>
                <div className="panel-body collapse" id="repairs-done">
                  {tableDone}
                </div>
              </div>
            </div>);
  }
});

const SegmentModal = React.createClass({
  getInitialState: function() {
    return {height: this.props.height};
  },
  render() {
    return (
      <Modal  {...this.props} bsSize="large" aria-labelledby="contained-modal-title-lg" dialogClassName="large-modal">
        <Modal.Header closeButton>
          <Modal.Title id="contained-modal-title-lg">Segments</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <iframe src={"segments.html?repairRunId="+this.props.repairRunId} width="100%" height={parseInt(this.props.height)-200} frameBorder="0"/>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={this.props.onHide}>Close</Button>
        </Modal.Footer>
      </Modal>
    );
  },
});


export default repairList;
