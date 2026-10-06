import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Area } from '../interfaces/area';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private url: string = `${environment.url}/${environment.customer}`;
  private http: HttpClient = inject(HttpClient);

  getValid(): Observable<any> {
    return this.http.get<any>(`${this.url}/valid`);
  }

  get(payload: Search): Observable<any> {
    return this.http.post<any>(`${this.url}/get`, payload);
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  create(payload: any): Observable<any> {
    return this.http.post(`${this.url}`, payload);
  }

  update(payload: any): Observable<any> {
    return this.http.put(`${this.url}`, payload);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  getPickupBoys(collectionId: number): Observable<any> {
    return this.http.get(`${this.url}/pickupboys/${collectionId}`);
  }
}
