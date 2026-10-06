import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Branch } from '../interfaces/branch';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class BranchService {
  private url: string = `${environment.url}/${environment.branch}`;
  private http: HttpClient = inject(HttpClient);

  get(payload: Search): Observable<any> {
    return this.http.post<any>(`${this.url}/get`, payload);
  }

  getById(id: number): Observable<Branch> {
    return this.http.get<Branch>(`${this.url}/${id}`);
  }

  create(payload: Branch): Observable<any> {
    return this.http.post(`${this.url}`, payload);
  }

  update(payload: Branch): Observable<any> {
    return this.http.put(`${this.url}`, payload);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  getValid(): Observable<any> {
    return this.http.get<any>(`${this.url}/valid`);
  }

  getValidByStateId(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/valid/${id}`);
  }

  getQty(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/qty/${id}`);
  }
}
