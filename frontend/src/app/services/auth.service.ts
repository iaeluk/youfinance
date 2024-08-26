import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  getUserInfo(): Observable<any> {
    return this.http.get(`${this.apiUrl}/user`);
  }

  getToken(): Observable<any> {
    return this.http.get(`${this.apiUrl}/user/token`, {
      withCredentials: true,
    });
  }

  login() {
    window.location.href = `${this.apiUrl}/oauth2/authorization/google`;
  }

  logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('tokenExpiry');

    window.location.href = `${this.apiUrl}/logout`;
  }
}
