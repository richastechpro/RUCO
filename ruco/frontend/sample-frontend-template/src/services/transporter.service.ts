import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transporter } from '../interfaces/transporter';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class TransporterService {
  private url: string = `${environment.url}/${environment.transporter}`;
  private http: HttpClient = inject(HttpClient);

  get(payload: Search): Observable<any> {
      return this.http.post<any>(`${this.url}/get`, payload);
    }

  getById(id: number): Observable<Transporter> {
    return this.http.get<Transporter>(`${this.url}/${id}`);
  }

  create(payload: Transporter): Observable<any> {
    return this.http.post(`${this.url}`, payload);
  }

  update(payload: Transporter): Observable<any> {
    return this.http.put(`${this.url}`, payload);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  getAll(collectionId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/valid/${collectionId}`);
  }
}
