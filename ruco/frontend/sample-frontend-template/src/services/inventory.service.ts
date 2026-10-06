import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Area } from '../interfaces/area';

@Injectable({
  providedIn: 'root',
})
export class InventoryService {
  private url: string = `${environment.url}/${environment.inventory}`;
  private http: HttpClient = inject(HttpClient);

  getInventoryAll(payload: any): Observable<any> {
    return this.http.post<any>(`${this.url}`, payload);
  }
}
