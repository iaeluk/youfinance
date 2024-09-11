import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../models/user.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/user`;

  private userSubject = new BehaviorSubject<any>(
    this.loadUserFromLocalStorage()
  );
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  private loadUserFromLocalStorage() {
    if (typeof window !== 'undefined') {
      const user = localStorage.getItem('user');
      return user ? JSON.parse(user) : null;
    }
  }

  setUser(user: any) {
    if (typeof window !== 'undefined') {
      localStorage.setItem('user', JSON.stringify(user));
      this.router.navigate(['banks']);
      this.userSubject.next(user);
    }
  }

  clearUser() {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('user');
      localStorage.removeItem('access_token');
      localStorage.removeItem('id_token');
      this.userSubject.next(null);
    }
  }

  getUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}`);
  }

  getStatus(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/status`);
  }
}
