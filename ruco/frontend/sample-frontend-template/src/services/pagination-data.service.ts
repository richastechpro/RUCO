import { keyframes } from '@angular/animations';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PaginationDataService {
  constructor() {}

  put(data: { key: string; value: number|string }) {
    localStorage.setItem(data.key, data.value + '');
  }

  clear(key: string) {
    localStorage.removeItem(key);
  }
  
  clearAll() {
    localStorage.removeItem('limit');
    localStorage.removeItem('offset');
    localStorage.removeItem('curModule');
  }

  exist(key: string): boolean {
    return localStorage.getItem(key) !== null;
  }

  get(key: string) {
    return localStorage.getItem(key);
  }
}
