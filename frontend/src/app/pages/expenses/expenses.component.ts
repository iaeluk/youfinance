import { Component, inject } from '@angular/core';
import { Expense } from '../../models/expense.model';
import { ExpenseService } from '../../services/expense.service';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { CurrencyPipe, NgClass } from '@angular/common';

@Component({
  selector: 'app-expenses',
  standalone: true,
  imports: [FormsModule, CurrencyPipe, NgClass],
  templateUrl: './expenses.component.html',
  styleUrl: './expenses.component.scss',
})
export class ExpensesComponent {
  expenseService = inject(ExpenseService);
  transactionService = inject(TransactionService);
  expenses: Expense[] = [];
  totals: { [key: string]: number | undefined } = {};

  expenseName: string | undefined;
  isLoading = true;

  getExpenses() {
    this.expenseService.getExpenses().subscribe((data) => {
      this.expenses = data;
      this.getTotals();
    });
  }

  createExpense() {
    let expense: Expense;

    expense = {
      name: this.expenseName,
    };

    this.expenseService.createExpense(expense).subscribe({
      next: (response) => {
        console.log('Despesa criada com sucesso:', response);
        this.getExpenses();
        this.expenseName = undefined;
      },
      error: (error) => console.error('Erro ao criar despesa:', error),
    });
  }

  deleteExpense(id: number) {
    this.expenseService.deleteExpense(id).subscribe({
      next: (response) => {
        console.log('Deletado com sucesso:', response);
        this.getExpenses();
      },
      error: (error) => console.error('Erro ao deletar:', error),
    });
    this.getExpenses();
  }

  ngOnInit() {
    if (typeof window !== 'undefined') {
      localStorage.setItem('activeButton', 'expenses');
    }

    this.getExpenses();
    this.sortExpenses();
  }

  private sortExpenses() {
    this.expenses.sort((a, b) => a.name!.localeCompare(b.name!));
  }

  getTotals(): void {
    this.expenses.map((expense) =>
      this.transactionService
        .getTotalAmountByExpenseName(expense.name!)
        .subscribe({
          next: (total) => (this.totals[expense.name!] = total),
        })
    );
    this.isLoading = false;
  }
}
