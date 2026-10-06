import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Area } from '../interfaces/area';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private url: string = `${environment.url}/${environment.dashboard}`;
  private http: HttpClient = inject(HttpClient);

  getData(): Observable<any> {
    return this.http.get<any>(`${this.url}`);
  }
}
