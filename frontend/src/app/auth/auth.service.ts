import { Injectable } from '@angular/core';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Oauth} from './oauth';
import {ConfigService} from "../config/config.service";
import {Config} from "../config/config";



@Injectable()
export class AuthService {

    config: Config;
    tokenURL: string;

    constructor(private http: HttpClient, private configService: ConfigService) {
        this.config = configService.getConfig();
        this.tokenURL = this.config.backendURL + ':' + this.config.backendPort + '/oauth/token';
    }

    // todo Move the following impl specific details into config
    localStorageKey = 'h2msCookie';
    client_id = 'h2ms';
    secret = 'secret';
    grant_type = 'password';


    login(email: string, password: string) {
        // expect request to return:
        const httpOptions = {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + btoa(this.client_id + ':' + this.secret),
                'Content-Type': 'application/x-www-form-urlencoded'
            })
        };
        const dataString = 'grant_type=' + this.grant_type + '&username=' + email + '&password=' + password;
        return this.http.post<Oauth>(this.tokenURL, dataString, httpOptions)
            .do(response => {
                if (response && response.access_token) {
                    if (this.isLoggedIn()) {
                        this.logout();
                    }
                    localStorage.setItem(this.localStorageKey, JSON.stringify(response));
                    return response;
                }
            });
    }

    logout(): void {
        if (localStorage.removeItem(this.localStorageKey)) {
            // todo: place logout request to backend
        }
    }

    getToken(): string {
        return JSON.parse(localStorage.getItem(this.localStorageKey)).access_token;
    }

    isLoggedIn(): boolean {
        return !!localStorage.getItem(this.localStorageKey);

    }

    // todo refreshToken()
}
