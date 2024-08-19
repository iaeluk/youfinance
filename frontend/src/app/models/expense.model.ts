import { Transaction } from './transaction.model';

export interface Expense {
  id?: number;
  name?: string;
  transactions?: Transaction[];
}
