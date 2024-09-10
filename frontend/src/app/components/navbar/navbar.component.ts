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
      console.log(this.authService.getProfile());
      this.isLoggedIn = isLoggedIn;
      if (this.isLoggedIn) {
        const user = this.authService.getProfile();
        if (user) {
          this.name = user['given_name'];
          this.email = user['email'];
          this.picture = user['picture'];
        }
      } else {
        this.name = '';
        this.email = '';
        this.picture = '';
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
