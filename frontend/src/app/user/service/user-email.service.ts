import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

@Injectable()
export class UserEmailService {

    localStorageKey = 'MyLittleUserEmailKey';

    constructor(private router: Router) {
    }

    setEmail(email: string): void {
        localStorage.setItem(this.localStorageKey, email);
    }

    getEmail(): string {
        const stored = localStorage.getItem(this.localStorageKey);

        if (!stored) {
            console.log('UserEmailService: cannot find user email');
            this.router.navigate(['login']);
        }
        return stored;
    }

}
