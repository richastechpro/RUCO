import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private url: string = `${environment.url}/${environment.report}`;
  private http: HttpClient = inject(HttpClient);

  get(payload: any): Observable<any> {
    return this.http.post(`${this.url}`, payload, { responseType: 'blob' });
  }
  
  log(payload: any): Observable<any> {
    return this.http.post(`${environment.url}/${environment.audit}/log`, payload, { responseType: 'blob' });
  }
}
