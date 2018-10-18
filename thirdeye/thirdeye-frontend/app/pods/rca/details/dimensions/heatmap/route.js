import { later } from '@ember/runloop';
import { inject as service } from '@ember/service';
import Route from '@ember/routing/route';
import { Actions } from 'thirdeye-frontend/actions/dimensions';

export default Route.extend({
  redux: service(),
  model(params, transition) {
    const redux = this.get('redux');
    const { metricId } = transition.params['rca.details'];
    const {
      analysisStart: initStart,
      analysisEnd: initEnd
    } = this.modelFor('rca.details');
    const {
      analysisStart,
      analysisEnd
    } = transition.queryParams;

    if (!metricId) { return; }
    const start = analysisStart || initStart;
    const end = analysisEnd || initEnd;

    redux.dispatch(Actions.fetchHeatMapData(Number(start), Number(end)));
    return {};
  },

  actions: {
    // Dispatches a redux action on query param change
    // to fetch heatmap data in the new date range
    queryParamsDidChange(changedParams, oldParams) {
      const redux = this.get('redux');
      let {
        analysisStart: start,
        analysisEnd: end
      } = changedParams;
      const params = Object.keys(changedParams || {});

      if (params.length && (start || end)) {

        start = start || oldParams.analysisStart;
        end = end || oldParams.analysisEnd;

        later(() => {
          redux.dispatch(Actions.fetchHeatMapData(
            Number(start),
            Number(end)
          ));
        });
      }
      this._super(...arguments);

      return true;
    }
  }
});
