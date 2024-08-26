import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bank } from '../models/bank.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class BankService {
  private apiUrl = `${environment.apiUrl}/banks`;

  constructor(private http: HttpClient) {}

  getBanks(): Observable<Bank[]> {
    return this.http.get<Bank[]>(`${this.apiUrl}/list`);
  }

  createBank(bank: Bank): Observable<any> {
    console.log(bank);
    return this.http.post(`${this.apiUrl}`, bank, { withCredentials: true });
  }

  deleteBank(bankId: number): Observable<any> {
    console.log(`Deleting transaction with ID: ${bankId}`);
    return this.http.delete(`${this.apiUrl}/${bankId}`, {
      withCredentials: true,
    });
  }
}
