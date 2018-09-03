import {Component, OnInit} from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {Router} from '@angular/router';
import {ConfigService} from "../config/config.service";
import {Config} from "../config/config";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
    styleUrls: ['./login.component.css', '../card.css']
})
export class LoginComponent implements OnInit {

    hide = true;
    loginAttempts = 2;
    config: Config;

    constructor(private auth: AuthService, private router: Router, private configService: ConfigService) {
        this.config = configService.getConfig();
    }

    ngOnInit() {
        this.auth.logout();
    }

    submit(email: string, password: string): void {
        this.auth.login(email, password)
            .subscribe(
            response => {
                    this.router.navigate(['dashboard']);
            },
            error => {
                if (error.status === 401) {
                    alert('login failed');
                    if (this.loginAttempts-- === 0) {
                        this.router.navigate(['password-recovery']);
                    }
                }
            });
    }
}
