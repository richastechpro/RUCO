import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Company } from '../interfaces/company';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  private url: string = `${environment.url}/${environment.company}`;
  private http: HttpClient = inject(HttpClient);

  get(limit:number,offset:number): Observable<any> {
    return this.http.get<any>(`${this.url}/${limit}/${offset}`);
  }

  getById(id: number): Observable<Company> {
    return this.http.get<Company>(`${this.url}/${id}`);
  }

  create(payload: Company): Observable<any> {
    return this.http.post(`${this.url}`, payload);
  }

  update(payload: Company): Observable<any> {
    return this.http.put(`${this.url}`, payload);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }
}
