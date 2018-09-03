import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {UserEntityService} from '../../index';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class UserResolverService implements Resolve<any> {
    constructor(private userService: UserEntityService) { }

    resolve(route: ActivatedRouteSnapshot, rstate: RouterStateSnapshot): Observable<any> {
        // Get 50 users
        // TODO: figure out how paging is going to work... and how many to get initially
        return this.userService.findAllUserUsingGET(undefined, '50', undefined);
    }
}
