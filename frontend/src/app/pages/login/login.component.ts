import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  private router = inject(Router);
  private userService = inject(UserService);

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.handleAuthCallback();
  }

  onLogin() {
    this.authService.login();
  }

  private handleAuthCallback() {
    const fragment = window.location.hash;
    const params = new URLSearchParams(fragment.slice(1));

    const accessToken = params.get('access_token');
    const idToken = params.get('id_token');

    if (accessToken) {
      localStorage.setItem('access_token', accessToken);
      console.log('Access token:', accessToken);
    }

    if (idToken) {
      localStorage.setItem('id_token', idToken);
      this.router.navigate(['/banks']);
      console.log('ID token:', idToken);

      this.userService.getUser().subscribe((user) => {
        console.log('User', user);
        this.userService.setUser(user);
      });
    }
  }
}
