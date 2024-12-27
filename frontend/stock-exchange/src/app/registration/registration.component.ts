import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../service/authentication.service";

@Component({
  selector: 'registration',
  templateUrl: 'registration.component.html',
  styleUrls: ['registration.component.scss']
})
export class RegistrationComponent implements OnInit {
  registrationForm!: FormGroup;
  userRegistered: boolean = false;
  errorMessage: string | undefined;

  constructor(private fb: FormBuilder,
              private router: Router,
              private authService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.registrationForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      name: ['', Validators.required]
    });
    this.userRegistered = false;
  }

  onSubmit(form: any) {
    this.authService.signUp(form.value.email, form.value.password, form.value.name).subscribe({
      next: data => {
        this.userRegistered = true;
        this.registrationForm.reset();
      },
      error: error => {
        this.errorMessage = error.error.errorMessage;
        this.registrationForm.reset();
        this.userRegistered = false;
      }
    })
  }

  openLoginPage() {
    this.router.navigateByUrl("");
  }
}
