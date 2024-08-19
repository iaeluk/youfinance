import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss',
})
export class FooterComponent {
  router = inject(Router);
  activeButton: string = '';

  ngOnInit(): void {
    this.getActiveButton();
  }

  getActiveButton() {
    if (typeof window !== 'undefined') {
      const storedButton = localStorage.getItem('activeButton');
      if (storedButton) {
        this.activeButton = storedButton;
      } else {
        this.activeButton = 'banks';
      }
    }
  }

  navigateTo(button: string) {
    this.router.navigate([button]);
    this.activeButton = button;
    localStorage.setItem('activeButton', button);
  }
}
