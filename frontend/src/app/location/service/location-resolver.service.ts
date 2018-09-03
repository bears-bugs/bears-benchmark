import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {LocationEntityService} from '../../index';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class LocationResolverService implements Resolve<any> {
    constructor(private locationService: LocationEntityService) { }

    resolve(route: ActivatedRouteSnapshot, rstate: RouterStateSnapshot): Observable<any> {
        // Get 50 locations
        // TODO: figure out how paging is going to work... and how many to get initially
        return this.locationService.findAllLocationUsingGET(undefined, '50', undefined);
    }
}
