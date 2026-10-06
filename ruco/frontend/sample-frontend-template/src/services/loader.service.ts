import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LoaderService {
  isLoading = signal(false);
  constructor() {}

  public showLoader(value: boolean) {
    this.isLoading.set(value);
  }
}
