import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Area } from '../interfaces/area';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class RateRequestService {
  private url: string = `${environment.url}/${environment.rateRequest}`;
  private http: HttpClient = inject(HttpClient);

  get(payload:Search): Observable<any> {
    return this.http.post<any>(`${this.url}/get`,payload);
  }

  approve(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}/approve`);
  }

  reject(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}/reject`);
  }
}
