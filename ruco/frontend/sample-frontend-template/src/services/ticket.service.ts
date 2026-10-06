import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Area } from '../interfaces/area';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private url: string = `${environment.url}/${environment.ticket}`;
  private http: HttpClient = inject(HttpClient);

  raiseTicket(payload: any): Observable<any> {
    return this.http.post<any>(`${this.url}`, payload);
  }

  get(payload: Search): Observable<any> {
    return this.http.post<any>(`${this.url}/get`, payload);
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  sendMsg(payload: any): Observable<any> {
    return this.http.post(`${this.url}/send-msg`, payload);
  }

  closeTicket(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/close/${id}`);
  }
}
