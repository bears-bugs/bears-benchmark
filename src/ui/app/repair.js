import jQuery from "jquery";
import React from "react";
import ReactDOM from "react-dom";
import ServerStatus from "jsx/server-status";
import Sidebar from "jsx/sidebar";
import RepairForm from "jsx/repair-form";
import RepairList from "jsx/repair-list";
import repairScreen from "jsx/repair-screen";
import {
  statusObservableTimer,
  repairs,
  addRepairSubject, addRepairResult,
  deleteRepairSubject, deleteRepairResult, updateRepairStatusSubject,
  clusterNames, deleteSubject, deleteResult, updateStatusSubject,
  addClusterSubject, addClusterResult, deleteClusterSubject,
  deleteClusterResult, updateRepairIntensitySubject
} from "observable";

jQuery(document).ready(function($){

  $.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results != null) {
      return results[1] || 0;
    }
    else {
      return null;
    }
  }

  let currentCluster: string = $.urlParam('currentCluster');
  if(!currentCluster) {
    currentCluster = 'all';
  }

  ReactDOM.render(
    React.createElement(repairScreen, {clusterNames, addRepairSubject, addRepairResult, currentCluster, repairs, deleteSubject: deleteRepairSubject,
    deleteResult: deleteRepairResult,
    updateStatusSubject: updateRepairStatusSubject,
    updateIntensitySubject: updateRepairIntensitySubject,
    statusObservableTimer}),
    document.getElementById('wrapper')
  );


});
