import { Component, inject, OnInit } from '@angular/core';
import { Bank } from '../../models/bank.model';
import { BankService } from '../../services/bank.service';
import { FormsModule } from '@angular/forms';
import { Transaction } from '../../models/transaction.model';
import { Expense } from '../../models/expense.model';
import { ExpenseService } from '../../services/expense.service';
import { TransactionService } from '../../services/transaction.service';
import { CurrencyPipe, DatePipe, NgClass } from '@angular/common';
import { CurrencyMaskModule } from 'ng2-currency-mask';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule, CurrencyPipe, DatePipe, NgClass, CurrencyMaskModule],
  templateUrl: './transactions.component.html',
  styleUrl: './transactions.component.scss',
})
export class TransactionsComponent implements OnInit {
  bankService = inject(BankService);
  expenseService = inject(ExpenseService);
  transactionService = inject(TransactionService);
  banks: Bank[] = [];
  expenses: Expense[] = [];
  transactions: Transaction[] = [];
  isLoading = true;

  date: string = this.getCurrentDateFormatted();
  amount: number | null = null;
  bankId: number | null = null;
  expenseId: number | null = null;
  TransactionType: string = 'bank';

  invoice: number | null = null;
  currentYear = new Date();
  currentYearAndMonth = undefined;

  ngOnInit() {
    if (typeof window !== 'undefined') {
      localStorage.setItem('activeButton', 'transactions');
    }

    this.bankService.getBanks().subscribe((data) => {
      this.banks = data;
      this.sortBanks();
    });

    this.expenseService.getExpenses().subscribe((data) => {
      this.expenses = data;
      this.sortExpenses();
    });

    this.getTransactions();
  }

  sortBanks() {
    this.banks.sort((a, b) => a.name!.localeCompare(b.name!));
  }

  sortExpenses() {
    this.expenses.sort((a, b) => a.name!.localeCompare(b.name!));
  }
  sortTransactions() {
    this.transactions.sort((a, b) => a.date!.localeCompare(b.date!));
  }

  getCurrentDateFormatted(): string {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  getTransactions() {
    this.transactionService.getTransactions().subscribe((data) => {
      this.transactions = data;
      this.isLoading = false;
      this.sortTransactions();
      this.invoice = this.transactions.reduce(
        (acc, tran) => acc + tran.amount!,
        0
      );
    });
    this.currentYearAndMonth = undefined;
  }

  getTransactionsByDate() {
    if (this.currentYearAndMonth !== undefined) {
      this.transactionService.getTransactions().subscribe((data) => {
        this.transactions = data.filter((transaction) => {
          const transactionYearAndMonth = transaction.date.slice(0, 7);
          return transactionYearAndMonth === this.currentYearAndMonth;
        });

        this.isLoading = false;
        this.sortTransactions();

        this.invoice = this.transactions.reduce(
          (acc, tran) => acc + tran.amount!,
          0
        );
      });
    } else {
      this.getTransactions();
    }
  }

  createTransaction() {
    let transaction: Transaction;

    transaction = {
      date: this.date,
      amount: this.amount,
    };

    if (this.TransactionType === 'bank') {
      if (!this.bankId) {
        throw new Error('Bank ID is required for card transactions');
      }
      transaction = {
        ...transaction,
        bank: {
          id: this.bankId,
        },
      };
    } else if (this.TransactionType === 'expense') {
      if (!this.expenseId) {
        throw new Error('Expense ID is required for expense transactions');
      }
      transaction = {
        ...transaction,
        expense: {
          id: this.expenseId,
        },
      };
    } else {
      throw new Error('Invalid transaction type');
    }

    this.transactionService.createTransaction(transaction).subscribe({
      next: (response) => {
        console.log('Transaction created successfully:', response);
        this.getTransactions();
        this.amount = null;
        this.bankId = null;
        this.expenseId = null;
      },
      error: (error) => console.error('Error creating transaction:', error),
    });
    this.date = this.getCurrentDateFormatted();
  }

  deleteTransaction(id: number) {
    this.transactionService.deleteTransaction(id).subscribe({
      next: (response) => {
        console.log('Deleted successfully:', response);
        this.getTransactions();
      },
      error: (error) => console.error('Error deleting:', error),
    });
  }
}
