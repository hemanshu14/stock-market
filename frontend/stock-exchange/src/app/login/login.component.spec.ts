import {ComponentFixture, TestBed} from '@angular/core/testing';
import { Router } from '@angular/router';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { LoginComponent } from './login.component';
import { By } from '@angular/platform-browser';
import {StockmarketComponent} from "../stockmarket/stockmarket.component";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([
          { path: 'stockmarket', component: StockmarketComponent }
        ])
      ],
      providers: []
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a form with email and password fields', () => {
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]'));
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]'));
    expect(emailInput).toBeTruthy();
    expect(passwordInput).toBeTruthy();
  });

  it('should display error message on login failure', () => {
    component.loginForm.setValue({ email: 'test@example.com', password: 'password' });
    component.onSubmit();
    const req = httpMock.expectOne('http://localhost:8005/users/login');
    req.flush({ errorMessage: 'Invalid credentials' }, { status: 401, statusText: 'Unauthorized' });

    fixture.detectChanges();
    const errorMessage = fixture.debugElement.query(By.css('.txt-error'));
    expect(errorMessage).toBeTruthy();
    if (errorMessage) {
      expect(errorMessage.nativeElement.textContent).toContain('Invalid credentials');
    }
  });

  it('should navigate to /stockmarket on successful login', () => {
    jest.spyOn(router, 'navigateByUrl');
    component.loginForm.setValue({ email: 'test@example.com', password: 'password' });
    component.onSubmit();
    const req = httpMock.expectOne('http://localhost:8005/users/login');

    req.flush({ name: 'John Doe', token: 'jwt-token' });

    expect(router.navigateByUrl).toHaveBeenCalledWith('/stockmarket', { state: { userData: { name: 'John Doe', token: 'jwt-token' } } });
  });

  it('should reset the form after login failure', () => {
    component.loginForm.setValue({ email: 'test@example.com', password: 'password' });
    component.onSubmit();
    const req = httpMock.expectOne('http://localhost:8005/users/login');
    req.flush({ errorMessage: 'Invalid credentials' }, { status: 401, statusText: 'Unauthorized' });

    expect(component.loginForm.value).toEqual({ email: null, password: null });
  });
});
