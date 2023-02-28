import {Component, OnInit} from '@angular/core';
import {AuthService} from "../auth.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  fullName: string | undefined;
  username: string | undefined;
  email: string | undefined;
  password: string | undefined;
  role: string = 'USER';

  constructor(public activeModal: NgbActiveModal, private authService: AuthService) {}

  ngOnInit(): void {}

  register() {
    const body = {
      fullName: this.fullName,
      username: this.username,
      email: this.email,
      password: this.password,
      role: this.role
    };

    this.authService.register(body).subscribe(
      (data) => {
        console.log('Registration successful! RegisterComponent: ', data);
        this.activeModal.close();
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
