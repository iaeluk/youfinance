import { Injectable } from '@angular/core';
import { Expense } from '../models/expense.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ExpenseService {
  private apiUrl = `${environment.apiUrl}/expenses`;

  constructor(private http: HttpClient) {}

  getExpenses(): Observable<Expense[]> {
    return this.http.get<Expense[]>(`${this.apiUrl}/list`);
  }

  createExpense(expense: Expense): Observable<any> {
    console.log(expense);
    return this.http.post(`${this.apiUrl}`, expense);
  }

  deleteExpense(expenseId: number): Observable<any> {
    console.log(`Deleting transaction with ID: ${expenseId}`);
    return this.http.delete(`${this.apiUrl}/${expenseId}`);
  }
}
