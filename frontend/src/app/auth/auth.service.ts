import {Inject, Injectable, Optional} from '@angular/core';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Oauth} from './oauth';
import {BASE_PATH} from '../variables';
import {ConfigService} from '../config/config.service';
import {Config} from '../config/config';
import {Router} from '@angular/router';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class AuthService {

    private config: Config;
    private readonly tokenURL: string;
    private isRefreshingToken: boolean;
    private localStorageKey = 'h2msCookie';
    private localStorageKeyRefreshTimeout = 'h2msTimeoutCookie';
    private client_id = 'h2ms';
    // todo secure secret
    private secret = 'secret';

    constructor(private http: HttpClient,
                private configService: ConfigService,
                @Optional() @Inject(BASE_PATH) basePath: string,
                private router: Router) {
        this.config = configService.getConfig();
        this.tokenURL = basePath ? basePath : this.config.getBackendUrl() + '/oauth/token';
        this.isRefreshingToken = false;
    }

    login(email: string, password: string) {
        // expect request to return:
        const httpOptions = {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + btoa(this.client_id + ':' + this.secret),
                'Content-Type': 'application/x-www-form-urlencoded'
            })
        };
        const dataString = 'grant_type=password&username=' + email + '&password=' + password;
        return this.http.post<Oauth>(this.tokenURL, dataString, httpOptions)
            .do(response => {
                if (response && response.access_token) {
                    localStorage.setItem(this.localStorageKey, JSON.stringify(response));
                    const timeout = '' + (((response.expires_in) * 1000) + (new Date()).getTime());
                    localStorage.setItem(this.localStorageKeyRefreshTimeout, timeout);
                    return response;
                }
            });
    }

    logout(): void {
        if (localStorage.removeItem(this.localStorageKey)) {
            // todo: place logout request to backend
        }
        this.router.navigate(['login']);
    }

    getToken() {
        const currentTime = (new Date()).getTime();
        const timeoutTime = (+localStorage.getItem(this.localStorageKeyRefreshTimeout));
        if (timeoutTime < currentTime) {
            return this.refreshToken().map(response => response.access_token);
        }
        return Observable.of(JSON.parse(localStorage.getItem(this.localStorageKey)).access_token);
    }

    isLoggedIn(): boolean {
        return !!localStorage.getItem(this.localStorageKey);
    }

    refreshToken() {
        if (!this.isRefreshingToken) {
            this.isRefreshingToken = true;
            const httpOptions = {
                headers: new HttpHeaders({
                    'Authorization': 'Basic ' + btoa(this.client_id + ':' + this.secret),
                    'Content-Type': 'application/x-www-form-urlencoded'
                })
            };
            const dataString = 'grant_type=refresh_token&client_id=' + this.client_id + '&refresh_token='
                + JSON.parse(localStorage.getItem(this.localStorageKey)).refresh_token;
            return this.http.post<Oauth>(this.tokenURL, dataString, httpOptions)
                .do(response => {
                    if (response && response.access_token) {
                        localStorage.setItem(this.localStorageKey, JSON.stringify(response));
                        const timeout = '' + (((response.expires_in) * 1000) + (new Date()).getTime());
                        localStorage.setItem(this.localStorageKeyRefreshTimeout, timeout);
                        this.isRefreshingToken = false;
                    } else {
                        this.isRefreshingToken = false;
                        this.router.navigate(['login']);
                    }
                });
        }
    }

    getTokenURL() {
        return this.tokenURL;
    }
}
