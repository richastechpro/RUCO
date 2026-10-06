import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private url: string = `${environment.url}/auth`;
  private http: HttpClient = inject(HttpClient);

  login(payload: any): Observable<any> {
    return this.http.post<any>(`${this.url}/login`, payload);
  }
  
  updatePWD(payload: any): Observable<any> {
    return this.http.put<any>(`${this.url}/pwd`, payload);
  }
}
