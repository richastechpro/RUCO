import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Area } from '../interfaces/area';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class RatesService {
  private url: string = `${environment.url}/${environment.rates}`;
  private http: HttpClient = inject(HttpClient);

  get(payload:Search): Observable<any> {
    return this.http.post<any>(`${this.url}/get`,payload);
  }
}
