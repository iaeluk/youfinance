import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Transaction } from '../models/transaction.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  private apiUrl = `${environment.apiUrl}/tx`;

  constructor(private http: HttpClient) {}

  getTransactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/list`);
  }

  createTransaction(transaction: Transaction): Observable<any> {
    console.log(transaction);
    return this.http.post(`${this.apiUrl}`, transaction);
  }

  deleteTransaction(transactionId: number): Observable<any> {
    console.log(`Deleting transaction with ID: ${transactionId}`);
    return this.http.delete(`${this.apiUrl}/${transactionId}`);
  }

  getTransactionsByDate(month: number, year: number) {
    return this.http.get<Transaction[]>(`${this.apiUrl}/date/${month}-${year}`);
  }

  getTotalAmountByExpenseName(name: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/total/expense/${name}`);
  }
}
