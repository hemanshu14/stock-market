import {AuthenticationService} from "./authentication.service";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {TestBed} from "@angular/core/testing";

describe('AuthenticationService', () => {
  let service: AuthenticationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthenticationService]
    });
    service = TestBed.inject(AuthenticationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call login API with correct URL and payload', () => {
    const emailId = 'test@example.com';
    const password = 'password123';

    service.login(emailId, password).subscribe();

    const req = httpMock.expectOne(`${service['baseUrl']}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ emailId, password });
  });

  it('should call signUp API with correct URL and payload', () => {
    const emailId = 'test@example.com';
    const password = 'password123';
    const name = 'Test User';

    service.signUp(emailId, password, name).subscribe();

    const req = httpMock.expectOne(`${service['baseUrl']}/signup`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ emailId, password, name });
  });
});
