import { Bank } from './bank.model';
import { Expense } from './expense.model';

export interface Transaction {
  id?: number;
  date: string;
  amount: number | null;
  bank?: Bank;
  expense?: Expense;
}
