import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpInterceptor
} from '@angular/common/http';
import { AuthService } from './auth.service';



@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(public auth: AuthService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler) {
        if (request.url !== this.auth.getTokenURL()
            && this.auth.isLoggedIn()) {
            return this.auth.getToken().flatMap((token) => {
                request = request.clone({
                    headers: request.headers.set('Authorization', `Bearer ${token}`)
                });
                return next.handle(request);
            });


        } else {
            return next.handle(request);
        }
    }
}
