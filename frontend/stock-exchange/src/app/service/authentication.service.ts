import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:8005/users';

  constructor(private http: HttpClient) {
  }

  login(emailId: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, {emailId: emailId, password: password});
  }

  signUp(emailId: string, password: string, name: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/signup`, {emailId: emailId, password: password, name: name});
  }
}
