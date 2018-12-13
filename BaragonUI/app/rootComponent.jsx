import React, { PropTypes, Component } from 'react';
import classNames from 'classnames';
import { NotFoundNoRoot } from 'components/common/NotFound';

const rootComponent = (Wrapped, refresh = null, refreshInterval = true, pageMargin = true, initialize = null) => class extends Component {

  static propTypes = {
    notFound: PropTypes.bool,
    pathname: PropTypes.string
  }

  static contextTypes = {
    store: PropTypes.object
  }

  constructor(props) {
    super(props);
    _.bindAll(this, 'handleBlur', 'handleFocus');

    this.state = {
      loading: refresh !== null
    };
  }

  dispatchRefresh() {
    return (refresh !== null)
      ? this.context.store.dispatch(refresh(this.props))
      : Promise.resolve();
  }

  dispatchInitialize() {
    return (initialize !== null)
      ? this.context.store.dispatch(initialize(this.props))
      : Promise.resolve();
  }

  componentWillMount() {
    const promise = (initialize !== null)
      ? this.dispatchInitialize()
      : this.dispatchRefresh();

    if (promise) {
      promise.then(() => {
        if (!this.unmounted) {
          this.setState({
            loading: false
          });
        }
      }).catch((reason) => {
        // Boot React errors out of the promise so they can be picked up by Sentry
        setTimeout(() => {
          throw new Error(reason);
        });
      });
    } else {
      this.setState({
        loading: false
      });
    }

    if (refreshInterval) {
      this.startRefreshInterval();
      window.addEventListener('blur', this.handleBlur);
      window.addEventListener('focus', this.handleFocus);
    }
  }

  componentWillUnmount() {
    this.unmounted = true;
    if (refreshInterval) {
      this.stopRefreshInterval();
      window.removeEventListener('blur', this.handleBlur);
      window.removeEventListener('focus', this.handleFocus);
    }
  }

  handleBlur() {
    this.stopRefreshInterval();
  }

  handleFocus() {
    const promise = this.dispatchRefresh();
    if (promise) {
      promise.catch((reason) => setTimeout(() => { throw new Error(reason); }));
    }
    this.startRefreshInterval();
  }

  startRefreshInterval() {
    this.refreshInterval = setInterval(() => {
      const promise = this.dispatchRefresh();
      if (promise) {
        promise.catch((reason) => setTimeout(() => { throw new Error(reason); }));
      }
    }, config.globalRefreshInterval);
  }

  stopRefreshInterval() {
    clearInterval(this.refreshInterval);
  }

  render() {
    if (this.props.notFound) {
      return (
        <div className={classNames({'page container-fluid': pageMargin})}>
          <NotFoundNoRoot location={{pathname: this.props.pathname}} />
        </div>
      );
    }
    const loader = this.state.loading && <div className="page-loader fixed" />;
    const page = !this.state.loading && <Wrapped {...this.props} />;
    return (
      <div className={classNames({'page container-fluid': pageMargin})}>
        {loader}
        {page}
      </div>
    );
  }
};

export default rootComponent;
