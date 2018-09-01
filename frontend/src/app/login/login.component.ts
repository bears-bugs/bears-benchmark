import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    hide: boolean = true;

  constructor() { }

  ngOnInit() {
  }

    submit(email: string, password: string): void {
        alert("email: " + email + ", password: " + password);
    }

}
