import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {EventTemplateEntityService} from '../../index';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/mergeMap';

@Injectable()
export class QuestionResolverService implements Resolve<any> {
    constructor(private templateService: EventTemplateEntityService) { }

    resolve(route: ActivatedRouteSnapshot, rstate: RouterStateSnapshot): Observable<any> {
        // Get the first template first
        return this.templateService.findAllEventTemplateUsingGET(undefined, '1', undefined)
        // Then use the template URL to get the questions for the template
            .flatMap(res => {
                const templateLink = res._embedded.eventTemplates[0]._links.eventTemplate.href;
                const templateURLSplit = templateLink.split('/'),
                    templateID = templateURLSplit[templateURLSplit.length - 1];
                return this.templateService.eventTemplateQuestionsUsingGET1(templateID);
            });
    }
}
