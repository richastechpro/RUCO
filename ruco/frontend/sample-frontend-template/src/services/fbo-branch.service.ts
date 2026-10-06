import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FboBranchService {
  private url: string = `${environment.url}/${environment.fboBranch}`;
  private http: HttpClient = inject(HttpClient);

  create(payload: any, fboId: number): Observable<any> {
    return this.http.post(`${this.url}/${fboId}`, payload);
  }

  update(payload: any, fboId: number): Observable<any> {
    return this.http.put(`${this.url}/${fboId}`, payload);
  }

  getValid(fboId: number): Observable<any> {
    return this.http.get(`${this.url}/valid/${fboId}`);
  }

  getAllValid(): Observable<any> {
    return this.http.get(`${this.url}/all-valid`);
  }

  getAllValidByBranchId(branchId: number): Observable<any> {
    return this.http.get(`${this.url}/all-valid/by-branch/${branchId}`);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  updateRate(payload: any): Observable<any> {
    return this.http.put(`${this.url}/update-rate`, payload);
  }

  upload(payload: FormData, fboId: number): Observable<any> {
    return this.http.post(`${this.url}/upload/${fboId}`, payload);
  }

  download(): Observable<any> {
    return this.http.get(`${this.url}/download-format`, {
      responseType: 'blob',
    });
  }
}
