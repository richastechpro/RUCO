import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Area } from '../interfaces/area';
import { Search } from '../interfaces/search';

@Injectable({
  providedIn: 'root',
})
export class CollectionService {
  private url: string = `${environment.url}/${environment.collection}`;
  private http: HttpClient = inject(HttpClient);

  get(payload: Search): Observable<any> {
    return this.http.post<any>(`${this.url}/get`, payload);
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  getBulkById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/bulk/${id}`);
  }

  createSingleCollection(payload: any): Observable<any> {
    return this.http.post(`${this.url}/single`, payload);
  }

  updateSingleCollection(payload: any, collectionId: number): Observable<any> {
    return this.http.put(`${this.url}/single/${collectionId}`, payload);
  }

  createBulkCollection(payload: any): Observable<any> {
    return this.http.post(`${this.url}/bulk`, payload);
  }

  updateBulkCollection(payload: any, collectionId: number): Observable<any> {
    return this.http.put(`${this.url}/bulk/${collectionId}`, payload);
  }

  update(payload: Area): Observable<any> {
    return this.http.put(`${this.url}`, payload);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }

  deleteInventoryOut(id: number): Observable<any> {
    return this.http.delete(`${this.url}/inventory-out/${id}`);
  }

  deleteBranch(fboId: number, fboBranchId: number): Observable<any> {
    return this.http.delete(`${this.url}/${fboId}/${fboBranchId}`);
  }

  getValid(): Observable<any> {
    return this.http.get<any>(`${this.url}/valid`);
  }

  getBranches(): Observable<any> {
    return this.http.get<any>(`${this.url}/branch`);
  }

  deleteRequest(id: number): Observable<any> {
    return this.http.delete(`${this.url}/request/${id}`);
  }

  updateAssign(payload: any): Observable<any> {
    return this.http.put(`${this.url}/assign`, payload);
  }

  updateCollect(payload: any): Observable<any> {
    return this.http.put(`${this.url}/collect`, payload);
  }

  upload(payload: any): Observable<any> {
    return this.http.put(`${this.url}/upload`, payload);
  }

  getCertificateDetails(id: number): Observable<any> {
    return this.http.get(`${this.url}/certficate/${id}`);
  }

  getBulkCollectionName(): Observable<any> {
    return this.http.get(`${this.url}/bulk-collection-name`);
  }

  createInventoryIn(collectionId: number): Observable<any> {
    return this.http.post(`${this.url}/inventory-in/${collectionId}`, {});
  }

  createInventoryOut(payload: number): Observable<any> {
    return this.http.post(`${this.url}/inventory-out`, payload);
  }

  getInventotyIn(limit: number, offset: number): Observable<any> {
    return this.http.get<any>(`${this.url}/inventory-in/${limit}/${offset}`);
  }

  getInventotyOut(limit: number, offset: number): Observable<any> {
    return this.http.get<any>(`${this.url}/inventory-out/${limit}/${offset}`);
  }
}
