import { AfterContentChecked, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss',
})
export class UserComponent implements AfterContentChecked {
  selectedTheme: string = '';

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
}
