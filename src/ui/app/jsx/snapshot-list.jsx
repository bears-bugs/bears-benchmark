import React from "react";
import moment from "moment";
import {CFsListRender, getUrlPrefix, humanFileSize, toast} from "jsx/mixin";
import NodeStatus from "jsx/node-status";
import ProgressBar from 'react-bootstrap/lib/ProgressBar';
import Button from 'react-bootstrap/lib/Button';
import Modal from 'react-bootstrap/lib/Modal';
import snapshotScreen from "./snapshot-screen";
import Popover from 'react-bootstrap/lib/Popover';
import OverlayTrigger from 'react-bootstrap/lib/OverlayTrigger';
var NotificationSystem = require('react-notification-system');

const TableRow = React.createClass({
  
  propTypes: {
    snapshot: React.PropTypes.object.isRequired,
    snapshotName: React.PropTypes.string.isRequired,
    snapshotTrueSize: React.PropTypes.number.isRequired,
    snapshotSizeOnDisk: React.PropTypes.number.isRequired,
    listSnapshots: React.PropTypes.func.isRequired,
    notificationSystem: React.PropTypes.object.isRequired,
    currentCluster: React.PropTypes.string.isRequired
  },

  getInitialState() {
    return {communicating: false};
  },

  clearOnAllNodes: function() {
    this.setState({communicating: true});
    toast(this.props.notificationSystem, "Clearing snapshot " + this.props.snapshotName + " on cluster " + this.props.currentCluster, "warning", this.props.snapshotName);
    $.ajax({
      url: getUrlPrefix(window.top.location.pathname) + '/snapshot/cluster/' + encodeURIComponent(this.props.currentCluster) + "/" + this.props.snapshotName,
      method: 'DELETE',
      component: this,
      dataType: 'text',
      success: function(data) {
        toast(this.component.props.notificationSystem, "Successfully cleared snapshot " + this.component.props.snapshotName + " on cluster " + this.component.props.currentCluster, "success", this.component.props.snapshotName);
      },
      complete: function(data) {
        this.component.props.listSnapshots(this.component.props.currentCluster);
        this.component.setState({communicating: false});
      },
      error: function(data) {
        toast(this.component.props.notificationSystem, "Failed clearing snapshot " + this.component.props.snapshotName + " on cluster " + this.component.props.currentCluster + "<br/>" + data.responseText, "error", this.component.props.snapshotName);
      }
  })
  },



  render: function() {

    let progressStyle = {
      display: "none" 
    }

    let yesStyle = {
      display: "inline-block" 
    }

    if (this.state.communicating==true) {
      progressStyle = {
        display: "inline-block"
      }
      yesStyle = {
        display: "none"
      } 
    }

    const deleteSnapshotClick = (
      <Popover id="takeSnapshot" title="Confirm?">
        <strong>Click yes to confirm </strong>
        <button type="button" className="btn btn-xs btn-danger" onClick={this.clearOnAllNodes} style={yesStyle}>Yes</button>
        <button type="button" className="btn btn-xs btn-danger" style={progressStyle} disabled>Deleting...</button>
      </Popover>
    );


    return (
    <tr>
        <td data-toggle="collapse" data-target={"#details_" + this.props.snapshotName}>{this.props.snapshotName}</td>
        <td data-toggle="collapse" data-target={"#details_" + this.props.snapshotName}>{Object.keys(this.props.snapshot).length}</td>
        <td data-toggle="collapse" data-target={"#details_" + this.props.snapshotName}>{this.props.snapshot[Object.keys(this.props.snapshot)[0]][0].owner}</td>
        <td data-toggle="collapse" data-target={"#details_" + this.props.snapshotName}>{humanFileSize(this.props.snapshotSizeOnDisk, 1024)}</td>
        <td data-toggle="collapse" data-target={"#details_" + this.props.snapshotName}>{humanFileSize(this.props.snapshotTrueSize, 1024)}</td>
        <td>
        <OverlayTrigger trigger="focus" placement="bottom" overlay={deleteSnapshotClick}><button type="button" className="btn btn-danger">Delete</button></OverlayTrigger>
        </td>
    </tr>
    );
  }

});

const TableRowDetails = React.createClass({
  getInitialState() {
      return {};
  },

  render: function() {
    const rowID = `details_${this.props.snapshotName}`;
    return (
      <tr id={rowID} className="collapse out">
        <td colSpan="7">
          <table className="table table-condensed">
            <tbody>
                <tr>
                    <td>
                      <div className="row">
                        <div className="col-lg-3">Cause</div>
                        <div className="col-lg-9">{this.props.snapshot[Object.keys(this.props.snapshot)[0]][0].cause}</div>
                      </div>
                    </td>
                </tr>
                <tr>
                    <td>
                      <div className="row">
                        <div className="col-lg-3">Nodes</div>
                        <div className="col-lg-9"><CFsListRender list={Object.keys(this.props.snapshot).sort()} /></div>
                      </div>
                    </td>
                </tr>
                <tr>
                    <td>
                      <div className="row">
                        <div className="col-lg-3">Creation time</div>
                        <div className="col-lg-9">{moment(this.props.snapshot[Object.keys(this.props.snapshot)[0]][0].creationDate).format("LLL")}</div>
                      </div>
                    </td>
                </tr>
            </tbody>
          </table>
        </td>
      </tr>
    );
  },

});

const snapshotList = React.createClass({
  _notificationSystem: null,

  propTypes: {
    clusterNames: React.PropTypes.object.isRequired,
    currentCluster: React.PropTypes.string.isRequired,
    changeCurrentCluster: React.PropTypes.func.isRequired,
    snapshots: React.PropTypes.object,  
    snapshotsSizeOnDisk: React.PropTypes.object, 
    snapshotsTrueSize: React.PropTypes.object,
    totalSnapshotSizeOnDisk: React.PropTypes.number, 
    totalSnapshotTrueSize: React.PropTypes.number,
    listSnapshots: React.PropTypes.func

  },

  getInitialState: function() {
    return {clusterNames:[], 
      currentCluster:this.props.currentCluster,
      communicating: false, refreshEnabled: !(this.props.currentCluster=='all')
    };
  },

  componentWillMount: function() {
    this._clusterNamesSubscription = this.props.clusterNames.subscribeOnNext(obs =>
      obs.subscribeOnNext(names => this.setState({clusterNames: names}))
    );
  },

  componentDidMount: function() {
    this._notificationSystem = this.refs.notificationSystem;
  },

  componentWillUnmount: function() {
    this._clustersSubscription.dispose();
  },


 


  render: function() {
    let rowsSnapshots = <div className="clusterLoader"></div>
 
    function compareCreationTimeReverse(a,b) {
      if (a.creation_time < b.creation_time)
        return 1;
      if (a.creation_time > b.creation_time)
        return -1;
      return 0;
    }



    rowsSnapshots = Object.keys(this.props.snapshots).sort()
      .map(snapshotName =>
      <tbody key={snapshotName+'-rows'}>
      <TableRow snapshot={this.props.snapshots[snapshotName]} snapshotName={snapshotName} key={snapshotName+'-head'} 
                snapshotSizeOnDisk={this.props.snapshotsSizeOnDisk[snapshotName]} snapshotTrueSize={this.props.snapshotsTrueSize[snapshotName]}
                listSnapshots={this.props.listSnapshots} notificationSystem={this._notificationSystem} currentCluster={this.props.currentCluster}/>
      <TableRowDetails snapshot={this.props.snapshots[snapshotName]} snapshotName={snapshotName} key={snapshotName+'-details'} />
      </tbody>
    );

    



    let tableSnapshots = null;
    if(rowsSnapshots.length == 0) {
      tableSnapshots = <div className="alert alert-info" role="alert">No snapshot found</div>
    } else {

      tableSnapshots = <div className="row">
          <div className="col-sm-12">
              <div className="table-responsive">
                  <table className="table table-bordered table-hover table-striped">
                      <thead>
                          <tr>
                            <th>Snapshot</th>
                            <th>Nodes</th>
                            <th>Owner</th>
                            <th>Size on disk</th>
                            <th>True size</th>
                            <th></th>
                          </tr>
                      </thead>
                        {rowsSnapshots}
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

    const snapshotHeader = <div className="panel-title"><a href="#snapshots" data-toggle="collapse" onClick={this._toggleRunningDisplay}>Snapshots</a>&nbsp; <span className="glyphicon glyphicon-menu-down" aria-hidden="true" style={menuRunningDownStyle}></span><span className="glyphicon glyphicon-menu-up" aria-hidden="true" style={menuRunningUpStyle}></span></div>


    return (
            <div>
              <NotificationSystem ref="notificationSystem" />
              <div className="panel panel-primary">
                <div className="panel-heading">
                  {snapshotHeader}
                </div>
                <div className="panel-body collapse in" id="snapshots">
                  {tableSnapshots}
                </div>
              </div>
            </div>);
  }
});




export default snapshotList;
