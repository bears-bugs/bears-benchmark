import jQuery from "jquery";
import ReactDOM from "react-dom";
import React from "react";
import ServerStatus from "jsx/server-status";
import ClusterScreen from "jsx/cluster-screen";
import {
  statusObservableTimer,
  addClusterSubject, addClusterResult, deleteClusterSubject, deleteClusterResult,
  logoutSubject, logoutResult,
  loginSubject, loginResult, loginRequiredSubject, loginRequiredResult,
  clusterNames
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
    React.createElement(ClusterScreen, {clusterNames, addClusterSubject, addClusterResult, currentCluster, 
      logoutSubject: logoutSubject, logoutResult: logoutResult,
      loginSubject: loginSubject, loginResult: loginResult,
      loginRequiredSubject: loginRequiredSubject, loginRequiredResult: loginRequiredResult,
      deleteSubject: deleteClusterSubject,
      deleteResult: deleteClusterResult, statusObservableTimer}),
    document.getElementById('wrapper')
  );
});