import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {RegistrationComponent} from './registration.component';
import {By} from '@angular/platform-browser';

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegistrationComponent],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have registration form with name, email, and password fields', () => {
    const nameInput = fixture.debugElement.query(By.css('input[formControlName="name"]'));
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]'));
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]'));
    expect(nameInput).toBeTruthy();
    expect(emailInput).toBeTruthy();
    expect(passwordInput).toBeTruthy();
  });

  it('should display error message on registration failure', () => {
    component.registrationForm.setValue({name: 'Test User', email: 'test@example.com', password: 'password'});
    component.onSubmit(component.registrationForm);
    const req = httpMock.expectOne('http://localhost:8005/users/signup');
    req.flush({errorMessage: 'Registration failed'}, {status: 400, statusText: 'Bad Request'});

    fixture.detectChanges();
    const errorMessage = fixture.debugElement.query(By.css('.txt-error'));
    expect(errorMessage).toBeTruthy();
    if (errorMessage) {
      expect(errorMessage.nativeElement.textContent).toContain('Registration failed');
    }
  });

  it('should reset the form after registration failure', () => {
    component.registrationForm.setValue({name: 'Test User', email: 'test@example.com', password: 'password'});
    component.onSubmit(component.registrationForm);
    const req = httpMock.expectOne('http://localhost:8005/users/signup');
    req.flush({errorMessage: 'Registration failed'}, {status: 400, statusText: 'Bad Request'});

    expect(component.registrationForm.value).toEqual({name: null, email: null, password: null});
  });

  it('should display a success message on successful registration', () => {
    component.registrationForm.setValue({name: 'Test User', email: 'test@example.com', password: 'password'});
    component.onSubmit(component.registrationForm);
    const req = httpMock.expectOne('http://localhost:8005/users/signup');

    req.flush({name: 'Test User', token: 'jwt-token'});

    expect(component.userRegistered).toBeTruthy();
  });
});
