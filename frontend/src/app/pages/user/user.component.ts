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
  }

  setTheme() {
    localStorage.setItem('theme', this.selectedTheme);
  }

  logout() {
    this.authService.logout();
  }
}
