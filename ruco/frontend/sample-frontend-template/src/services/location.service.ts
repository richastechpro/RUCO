import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LocationService {
  private url: string = `${environment.url}/${environment.location}`;
  private http: HttpClient = inject(HttpClient);

  getLocations(): Observable<any> {
    return this.http.get<any>(`${this.url}`);
  }
}
