import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LocalstorageService {
  user = signal<any | null>(null);

  setUser(user: any) {
    localStorage.setItem('user', JSON.stringify(user));
  }

  getUser() {
    const userData = localStorage.getItem('user');
    if (userData) {
      const user = JSON.parse(userData);
      this.user.set(user);
      return this.user();
    }
  }

  getPermissions() {
    if (this.user()) {
      return this.user().permissions;
    }
  }
}
