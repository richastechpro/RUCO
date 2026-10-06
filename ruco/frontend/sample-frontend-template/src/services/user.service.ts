import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private url: string = `${environment.url}/${environment.user}`;
  private http: HttpClient = inject(HttpClient);

  get(payload: Search): Observable<any> {
      return this.http.post<any>(`${this.url}/get`, payload);
    }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  create(payload: User): Observable<any> {
    return this.http.post(`${this.url}`, payload);
  }

  update(payload: User): Observable<any> {
    return this.http.put(`${this.url}`, payload);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  getPickupBoys(collectionId: number): Observable<any> {
    return this.http.get(`${this.url}/pickupboys/${collectionId}`);
  }
}
