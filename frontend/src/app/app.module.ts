import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './app-routing.module';
import {
    MatButtonModule,
    MatCardModule, MatCheckboxModule, MatDialogModule, MatDividerModule, MatFormFieldModule, MatIconModule,
    MatInputModule
} from "@angular/material";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { PrivacyComponent } from './privacy/privacy.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    PrivacyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatFormFieldModule,
      MatInputModule,
      BrowserAnimationsModule,
      MatCardModule,
      MatCheckboxModule,
      MatDividerModule,
      MatButtonModule,
      MatDialogModule,
      MatIconModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
