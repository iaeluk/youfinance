import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  imports: [RouterModule, CommonModule],
})
export class NavbarComponent implements OnInit {
  router = inject(Router);

  name: string = '';
  email: string = '';
  picture: string = '';

  isLoggedIn = false;

  user: any = null;

  constructor(
    public userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.userService.user$.subscribe((user) => {
      this.user = user;
      console.log('User updated:', this.user);
      if (user) {
        this.isLoggedIn = true;
        this.name = user.name;
        this.email = user.email;
        this.picture = user.picture;
      }
    });
  }

  logout() {
    this.isLoggedIn = false;

    this.userService.clearUser();
    this.authService.logout();

    this.router.navigate(['/login']);
  }
}
