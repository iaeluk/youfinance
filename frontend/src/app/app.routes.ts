import { Routes } from '@angular/router';
import { BanksComponent } from './pages/banks/banks.component';
import { TransactionsComponent } from './pages/transactions/transactions.component';
import { ExpensesComponent } from './pages/expenses/expenses.component';
import { UserComponent } from './pages/user/user.component';

export const routes: Routes = [
  { path: '', component: BanksComponent },
  { path: 'banks', component: BanksComponent },
  { path: 'transactions', component: TransactionsComponent },
  { path: 'expenses', component: ExpensesComponent },
  { path: 'user', component: UserComponent },
  { path: '**', redirectTo: '' },
];
