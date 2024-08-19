import { Component, inject, OnInit } from '@angular/core';
import { BankService } from '../../services/bank.service';
import { Bank } from '../../models/bank.model';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { CurrencyMaskModule } from 'ng2-currency-mask';

@Component({
  selector: 'app-banks',
  standalone: true,
  imports: [FormsModule, CurrencyPipe, CurrencyMaskModule],
  templateUrl: './banks.component.html',
  styleUrl: './banks.component.scss',
})
export class BanksComponent implements OnInit {
  bankService = inject(BankService);
  banks: Bank[] = [];
  isLoading = true;

  type = 'bank';
  bankName: string | undefined = undefined;
  creditLimit: number | undefined = undefined;

  ngOnInit() {
    if (typeof window !== 'undefined') {
      localStorage.setItem('activeButton', 'banks');
    }

    this.getBanks();
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
        console.log('Banco criado com sucesso:', response);
        this.getBanks();
        this.bankName = undefined;
        this.creditLimit = undefined;
      },
      error: (error) => console.error('Erro ao criar banco:', error),
    });
  }

  deleteBank(id: number) {
    this.bankService.deleteBank(id).subscribe({
      next: (response) => {
        console.log('Deletado com sucesso:', response);
        this.getBanks();
      },
      error: (error) => console.error('Erro ao deletar:', error),
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
