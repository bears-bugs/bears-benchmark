import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {UserEntityService} from '../../index';
import {Observable} from 'rxjs/Observable';
import {UserEmailService} from './user-email.service';

@Injectable()
export class UserByEmailResolverService implements Resolve<any> {
    constructor(private userService: UserEntityService,
                private userEmailService: UserEmailService) { }

    resolve(route: ActivatedRouteSnapshot, rstate: RouterStateSnapshot): Observable<any> {
        const email = this.userEmailService.getEmail();
        return this.userService.findByEmailUserUsingGET(email);
    }
}
