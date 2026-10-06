import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Area } from '../interfaces/area';

@Injectable({
  providedIn: 'root',
})
export class FboBranchLoginService {
  private url: string = `${environment.url}/${environment.fboBranchLogin}`;
  private http: HttpClient = inject(HttpClient);

  getById(id: number): Observable<Area> {
    return this.http.get<Area>(`${this.url}/${id}`);
  }

  create(payload: any, fboId: number): Observable<any> {
    return this.http.post(`${this.url}/${fboId}`, payload);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  getPickupBoys(collectionId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/pickupboys/${collectionId}`);
  }

  upload(payload: FormData, fboId: number): Observable<any> {
    return this.http.post(`${this.url}/upload/${fboId}`, payload);
  }

  download(fboId: number): Observable<any> {
    return this.http.get(`${this.url}/download-format/${fboId}`, {
      responseType: 'blob',
    });
  }
}
