import {FormControl, Validators} from '@angular/forms';

export const REQUIRED_EMAIL = new FormControl('', [Validators.required, Validators.email]);
export const REQUIRED_PASSWORD = new FormControl('', [Validators.required]);

export function REQUIRED_EMAIL_ERROR_MESSAGE() {
    return REQUIRED_EMAIL.hasError('required') ? 'You must enter a value' :
        REQUIRED_EMAIL.hasError('email') ? 'Not a valid email' : '';
}

export function REQUIRED_PASSWORD_ERROR_MESSAGE() {
    return REQUIRED_PASSWORD.hasError('required') ? 'You must enter a value' : '';
}