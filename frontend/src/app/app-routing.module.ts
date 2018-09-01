import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent} from "./login/login.component";
import {PrivacyComponent} from "./privacy/privacy.component";


const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: '', redirectTo: 'login', pathMatch: 'full'},
    // TODO: Route about to the AboutComponent when it is created.
    { path: 'about', redirectTo: 'login', pathMatch: 'full'},
    { path: 'privacy', component: PrivacyComponent },
];

@NgModule({
    exports: [ RouterModule ],
    imports: [ RouterModule.forRoot(routes) ]
})
export class AppRoutingModule {}

