import { Transaction } from "./transaction.model";

export interface Bank {
  id?: number;
  name?: string;
  creditLimit?: number;
  transactions?: Transaction[];
  totalCreditLimit?: number;
}
