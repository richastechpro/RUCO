import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Area } from '../interfaces/area';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class FboService {
  private url: string = `${environment.url}/${environment.fbo}`;
  private http: HttpClient = inject(HttpClient);

  get(payload: Search): Observable<any> {
    return this.http.post<any>(`${this.url}/get`, payload);
  }

  getById(id: number): Observable<Area> {
    return this.http.get<Area>(`${this.url}/${id}`);
  }

  create(payload: Area): Observable<any> {
    return this.http.post(`${this.url}`, payload);
  }

  update(payload: any): Observable<any> {
    return this.http.put(`${this.url}`, payload);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  deleteBranch(fboId: number, fboBranchId: number): Observable<any> {
    return this.http.delete(`${this.url}/${fboId}/${fboBranchId}`);
  }

  getValid(): Observable<any> {
    return this.http.get<any>(`${this.url}/valid`);
  }

  getFboBranches(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/branch/${id}`);
  }

  getFboLogins(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/logins/${id}`);
  }

  getFboName(fboId: number): Observable<string> {
    return this.http.get<string>(`${this.url}/fbo-name/${fboId}`);
  }
}
