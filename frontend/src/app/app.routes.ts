import { Routes } from '@angular/router';
import { BanksComponent } from './pages/banks/banks.component';
import { TransactionsComponent } from './pages/transactions/transactions.component';
import { ExpensesComponent } from './pages/expenses/expenses.component';
import { UserComponent } from './pages/user/user.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: BanksComponent, canActivate: [authGuard] },
  { path: 'banks', component: BanksComponent, canActivate: [authGuard] },
  {
    path: 'transactions',
    component: TransactionsComponent,
    canActivate: [authGuard],
  },
  { path: 'expenses', component: ExpensesComponent, canActivate: [authGuard] },
  { path: 'user', component: UserComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' },
];
