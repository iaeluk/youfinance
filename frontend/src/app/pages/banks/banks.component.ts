import { Component, inject, OnInit } from '@angular/core';
import { BankService } from '../../services/bank.service';
import { Bank } from '../../models/bank.model';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { CurrencyMaskModule } from 'ng2-currency-mask';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-banks',
  standalone: true,
  imports: [FormsModule, CurrencyPipe, CurrencyMaskModule],
  templateUrl: './banks.component.html',
  styleUrl: './banks.component.scss',
})
export class BanksComponent implements OnInit {
  bankService = inject(BankService);
  userService = inject(UserService);
  router = inject(Router);
  banks: Bank[] = [];
  isLoading = true;

  type = 'bank';
  bankName: string | undefined = undefined;
  creditLimit: number | undefined = undefined;

  ngOnInit() {
    this.handleAuthCallback();

    this.userService.user$.subscribe((user) => {
      if (user) {
        this.getBanks();
      } else {
        this.router.navigate(['/login']);
      }
    });

    if (typeof window !== 'undefined') {
      localStorage.setItem('activeButton', 'banks');
    }
  }

  private handleAuthCallback() {
    if (typeof window !== 'undefined') {
      const fragment = window.location.hash;
      const params = new URLSearchParams(fragment.slice(1));

      const accessToken = params.get('access_token');
      const idToken = params.get('id_token');

      if (accessToken) {
        localStorage.setItem('access_token', accessToken);
      }

      if (idToken) {
        localStorage.setItem('id_token', idToken);
        this.userService.getUser().subscribe((user) => {
          this.userService.setUser(user);
        });
      }
    }
  }

  getBanks() {
    this.bankService.getBanks().subscribe((data) => {
      this.banks = data;
      this.sortBanks();
      this.isLoading = false;
      this.banks.map(
        (bank) =>
          (bank.totalCreditLimit =
            bank.creditLimit! +
            bank.transactions!.reduce(
              (acc, transaction) => acc + transaction.amount!,
              0
            ))
      );
    });
  }

  createBank() {
    let bank: Bank;

    bank = {
      name: this.bankName,
      creditLimit: this.creditLimit,
    };

    this.bankService.createBank(bank).subscribe({
      next: (response) => {
        console.log('Bank created successfully:', response);
        this.getBanks();
        this.bankName = undefined;
        this.creditLimit = undefined;
      },
      error: (error) => console.error('Error creating bank:', error),
    });
  }

  deleteBank(id: number) {
    this.bankService.deleteBank(id).subscribe({
      next: (response) => {
        console.log('Deleted successfully:', response);
        this.getBanks();
      },
      error: (error) => console.error('Error deleting:', error),
    });
    this.getBanks();
  }

  sortBanks() {
    this.banks.sort((a, b) => a.name!.localeCompare(b.name!));
  }

  totalCreditLimit(): number {
    return this.banks.reduce((acc, bank) => acc + bank.creditLimit!, 0);
  }
}
