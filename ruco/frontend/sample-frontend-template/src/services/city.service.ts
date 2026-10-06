import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { City } from '../interfaces/city';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class CityService {
  private url: string = `${environment.url}/${environment.city}`;
  private http: HttpClient = inject(HttpClient);

  get(payload: Search): Observable<any> {
    return this.http.post<any>(`${this.url}/get`, payload);
  }

  getById(id: number): Observable<City> {
    return this.http.get<City>(`${this.url}/${id}`);
  }

  create(payload: City): Observable<any> {
    return this.http.post(`${this.url}`, payload);
  }

  update(payload: City): Observable<any> {
    return this.http.put(`${this.url}`, payload);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  getValid(): Observable<any> {
    return this.http.get<any>(`${this.url}/valid`);
  }
}
