import { AfterContentChecked, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss',
})
export class UserComponent implements AfterContentChecked {
  selectedTheme: string = '';

  name: string = '';
  email: string = '*****@gmail.com';
  picture: string = 'man.png';

  authService = inject(AuthService);

  ngAfterContentChecked(): void {
    let html = document.querySelector('html');
    html?.setAttribute('data-theme', this.selectedTheme);
  }

  ngOnInit() {
    if (typeof window !== 'undefined') {
      localStorage.setItem('activeButton', 'user');
    }

    this.selectedTheme = localStorage.getItem('theme') || '';

    console.log('Profile: ', this.authService.getProfile());

    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      this.name = parsedUser.name || '';
      this.email = parsedUser.email || '';
      this.picture = parsedUser.picture || '';
    }
  }

  setTheme() {
    localStorage.setItem('theme', this.selectedTheme);
  }
}
