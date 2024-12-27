import {Component, NgZone, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthenticationService} from "../service/authentication.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  errorMessage: string | undefined;
  token: string | undefined;
  name: string | undefined;

  constructor(private fb: FormBuilder,
              private router: Router,
              private authService: AuthenticationService,
              private ngZone: NgZone) {
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    this.authService.login(this.loginForm.value.email, this.loginForm.value.password).subscribe({
      next: data => {
        this.name = data.name;
        this.token = data.token;
        const userData = {name: this.name, token: this.token};
        this.ngZone.run(() => {
          this.router.navigateByUrl('/stockmarket', {state: {userData: userData}});
        });
      },
      error: error => {
        this.errorMessage = error.error.errorMessage;
        this.loginForm.reset();
      }
    })
  }

  openRegistrationPage() {
    this.router.navigateByUrl('/signup');
  }
}
