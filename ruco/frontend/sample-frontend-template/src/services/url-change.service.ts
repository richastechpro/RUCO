import { Injectable, signal } from '@angular/core';
import {
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router,
} from '@angular/router';
import { filter } from 'rxjs';
import { environment } from '../environments/environment.development';
import { PaginationDataService } from './pagination-data.service';

@Injectable({
  providedIn: 'root',
})
export class UrlChangeInterceptorService {
  tarUrls = signal<string[] | null>(null);
  constructor(private router: Router) {}

  interceptUrlChanges() {
    this.router.events
      .pipe(
        filter(
          (event) =>
            event instanceof NavigationStart ||
            event instanceof NavigationEnd ||
            event instanceof NavigationCancel ||
            event instanceof NavigationError
        )
      )
      .subscribe((event) => {
        if (event instanceof NavigationStart) {
          // console.log('Navigation started to:', event.url);
          // Perform actions when navigation starts (e.g., show a loader)
        }

        if (event instanceof NavigationEnd) {
          const url: string = event.url.replace('/app', '');
          const urls = url.split('/');
          let targetUrls: string[] = [];
          if (urls[urls.length - 1] === environment.list) {
            for (let i = 1; i < urls.length - 1; i++) {
              if (i === urls.length - 2) {
                targetUrls.push(urls[i] + '/' + environment.list);
              } else {
                targetUrls.push(urls[i]);
              }
            }
          } else {
            for (let i = 1; i < urls.length; i++) {
              targetUrls.push(urls[i]);
            }
          }

          // console.log('Navigation ended at:', targetUrls);
          this.tarUrls.set(targetUrls);
          // Perform actions when navigation ends (e.g., hide a loader)
        }

        if (event instanceof NavigationCancel) {
          console.log('Navigation canceled:', event.url);
          // Handle canceled navigation (e.g., due to a guard)
        }

        if (event instanceof NavigationError) {
          console.error('Navigation error:', event.error);
          // Handle navigation errors
        }
      });
  }
}
