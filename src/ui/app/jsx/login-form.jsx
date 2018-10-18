import React from "react";


const loginForm = React.createClass({

  propTypes: {
    loginSubject: React.PropTypes.object.isRequired,
    loginResult: React.PropTypes.object.isRequired,
    loginCallback: React.PropTypes.func
  },

  getInitialState: function () {
    return {loginResultMsg: null};
  },

  componentWillMount: function () {
  this._loginResultSubscription = this.props.loginResult.subscribeOnNext(obs =>
      obs.subscribe(
        response => {
          this.setState({loginResultMsg: null});
          window.location.href = "/webui/index.html"
        },
        response => {
            this.setState({loginResultMsg: response.responseText});
        }
      )
    );
  },

  componentWillUnmount: function () {
    this._loginResultSubscription.dispose();
  },

  _onLogin: function (e) {
    const login = {
      username: React.findDOMNode(this.refs.in_username).value,
      password: React.findDOMNode(this.refs.in_password).value
    };
    this.props.loginSubject.onNext(login);
  },

  render: function () {
    let loginError = null;
    if (this.state.loginResultMsg) {
      loginError = <div className="alert alert-danger" role="alert">{this.state.loginResultMsg}</div>
    }

    const form = <div className="row">
      <div className="col-lg-12">
        <div className="col-lg-4">&nbsp;</div>
        <div className="col-lg-4"><h2>Cassandra Reaper</h2></div>
        <div className="col-lg-4">&nbsp;</div>
      </div>
      <div className="col-lg-12">
        <div className="col-lg-4">&nbsp;</div>
        <div className="col-lg-4">
          <div className="form-inline">
            <div className="form-group">
              <label htmlFor="in_username">Username:</label>
              <input type="text" className="form-control" ref="in_username" id="in_username"
                    placeholder="Username"></input>
            </div>
            <div className="form-group">
              <label htmlFor="in_username">Password:</label>
              <input type="password" className="form-control" ref="in_password" id="in_password"
                    placeholder="Password"></input>
            </div>
            <button type="button" className="btn btn-success" onClick={this._onLogin}>Login</button>
          </div>
        </div>
        <div className="col-lg-4">&nbsp;</div>
      </div>
    </div>


    return (
    <div className="panel panel-default">
      <div className="panel-body">
        {loginError}
        {form}
      </div>
    </div>);
  }
});

export default loginForm;