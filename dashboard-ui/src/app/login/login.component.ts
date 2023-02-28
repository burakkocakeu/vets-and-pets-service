import {Component, OnInit} from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { RegisterComponent } from '../register/register.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username = '';
  password = '';
  errorMessage = '';
  registeredMessage = 'Registration is successful, you can login with your credentials!';
  showErrorMessage = false;
  showRegisteredMessage = false;


  constructor(private authService: AuthService, private router: Router, private modalService: NgbModal) { }

  ngOnInit(): void {
  }

  onSubmit() {
    this.authService.login(this.username, this.password).subscribe(
      (data) => {
        const token = data.token;
        console.log(token);

        // Store the JWT token in local storage
        localStorage.setItem('access_token', token);

        // Navigate to the dashboard component
        setTimeout(() => {
          this.router.navigate(['/dashboard']).then(() => {
            window.location.reload();
          });
        }, 500);
      },
      (error) => {
        console.log(error);
        if (error.status === 400) {
          this.errorMessage = 'Invalid username or password!';
        } else {
          this.errorMessage = 'An error occurred. Please try again later.';
        }
        this.showErrorMessage = true;
        setTimeout(() => {
          this.showErrorMessage = false;
        }, 4000);
      }
    );
  }

  onRegister(body: any) {
    this.authService.register(body).subscribe(
      (data) => {
        console.log(data);
        this.username = body.username;
        this.password = body.password;
        if (this.authService.getRole() !== 'ADMIN') {
          this.onSubmit();
        }
      },
      (error) => {
        console.log(error);
      }
    );
  }

  onLogout() {
    this.authService.logout().subscribe(
      (data) => { console.log(data); },
      (error) => { console.log(error); }
    );
    localStorage.removeItem('access_token');
  }

  showRegisterPopup() {
    const modalRef = this.modalService.open(RegisterComponent, { centered: true });
    modalRef.result.then((result) => {
      console.log('Registration successful! LoginComponent: ' + result);
      this.showRegisteredMessage = true;
      setTimeout(() => {
        this.showRegisteredMessage = false;
      }, 4000);
    });
  }
}
