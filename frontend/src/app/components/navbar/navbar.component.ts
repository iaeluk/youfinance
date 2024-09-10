import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  imports: [RouterModule],
})
export class NavbarComponent implements OnInit {
  router = inject(Router);

  name: string = '';
  email: string = '';
  picture: string = 'man.png';
  isLoggedIn = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.isLoggedIn$.subscribe((isLoggedIn) => {
      console.log('isLoggedIn: ', isLoggedIn);
      this.isLoggedIn = isLoggedIn;
      const user = localStorage.getItem('user');
      if (user) {
        const parsedUser = JSON.parse(user);
        this.name = parsedUser.name || '';
        this.email = parsedUser.email || '';
        this.picture = parsedUser.picture || '';
      }
    });
  }

  logout() {
    this.authService.logout();
    localStorage.removeItem('access_token');
    localStorage.removeItem('id_token');
    this.router.navigate(['/login']);
  }
}
