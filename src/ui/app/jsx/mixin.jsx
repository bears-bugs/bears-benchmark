import React from "react";

export const RowDeleteMixin = {

  deleteButton: function() {
    return <button type="button" className="btn btn-xs btn-danger" onClick={this._onDelete}>Delete</button>
  },

  _onDelete: function(e) {
    this.props.deleteSubject.onNext({id: this.props.row.id, owner: this.props.row.owner});
  }

};

export const RowAbortMixin = {

  abortButton: function() {
    return <button type="button" className="btn btn-xs btn-danger" onClick={this._onAbort}>Abort</button>
  },

  _onAbort: function(e) {
    this.props.updateStatusSubject.onNext({id: this.props.row.id, state: 'ABORTED'});
  }

};

export const StatusUpdateMixin = {

  statusUpdateButton: function() {

    let toggleStatusCue = null;
    if(this.props.row.state == 'ACTIVE' || this.props.row.state == 'RUNNING') {
      toggleStatusCue = 'Stop';
    } else if(this.props.row.state == 'PAUSED' || this.props.row.state == 'NOT_STARTED') {
      toggleStatusCue = 'Activate';
    }
    let btnSetStatus = null;
    if(toggleStatusCue) {
      return <button type="button" className="btn btn-xs btn-success" onClick={this._onToggleStatus}>{toggleStatusCue}</button>
    }
  },

  _onToggleStatus: function(e) {
    let toStatus = null;
    const state = this.props.row.state;
    if(state == 'ACTIVE' || state == 'RUNNING') {
      toStatus = 'PAUSED';
    } else if(state == 'PAUSED' || state == 'NOT_STARTED') {
      if(this.props.row.next_activation) {
        toStatus = 'ACTIVE'; // repair schedule
      } else {
        toStatus = 'RUNNING'; // repair run
      }
    }

    this.props.updateStatusSubject.onNext({id: this.props.row.id, state: toStatus});
  }

};

export const DeleteStatusMessageMixin = {

  componentWillMount: function() {
    this._deleteResultSubscription = this.props.deleteResult.subscribeOnNext(obs =>
      obs.subscribe(
        r => this.setState({deleteResultMsg: null}),
        r => this.setState({deleteResultMsg: r.responseText})
      )
    );
  },

  componentWillUnmount: function() {
    this._deleteResultSubscription.dispose();
  },

  deleteMessage: function() {
    if(this.state.deleteResultMsg) {
      return <div className="alert alert-danger" role="alert">{this.state.deleteResultMsg}</div>
    }
  }

};

export const CFsListRender = React.createClass({
    render: function() {
        return (
            <div className="ReactTags__tags">
              <div className="ReactTags__selected">
                {this.props.list.map(function(listValue){
                    return <span className="ReactTags__tag" key={listValue}>{listValue}</span>;
                })}
              </div>
            </div>
        )
    }
});

export const humanFileSize = function(bytes, si) {
  var thresh = si ? 1000 : 1024;
  if(Math.abs(bytes) < thresh) {
      return bytes + ' B';
  }
  var units = si
      ? ['kB','MB','GB','TB','PB','EB','ZB','YB']
      : ['KiB','MiB','GiB','TiB','PiB','EiB','ZiB','YiB'];
  var u = -1;
  do {
      bytes /= thresh;
      ++u;
  } while(Math.abs(bytes) >= thresh && u < units.length - 1);
  return bytes.toFixed(1)+' '+units[u];
}

export const getUrlPrefix = function(location) {
  const isDev = location.includes('webpack-dev-server');
  const URL_PREFIX = isDev ? 'http://127.0.0.1:8080' : '';
  return URL_PREFIX;
}

export const toast = function(notificationSystem, message, type, uid) {
  event.preventDefault();
  notificationSystem.addNotification({
    message: message,
    level: type,
    autoDismiss: 3
  });
}

export const toastPermanent = function(notificationSystem, message, type, uid) {
  event.preventDefault();
  notificationSystem.addNotification({
    message: message,
    level: type,
    autoDismiss: 0
  });
}
