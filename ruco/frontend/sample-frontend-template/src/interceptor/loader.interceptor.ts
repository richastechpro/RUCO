import { HttpEventType, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, tap } from 'rxjs';
import { LoaderService } from '../services/loader.service';

export const loaderInterceptor: HttpInterceptorFn = (req, next) => {
  const loader: LoaderService = inject(LoaderService);
  return next(req).pipe(
    tap((event) => {
      if (event.type === HttpEventType.Sent) {
        loader.isLoading.set(true);
      } else if (event.type === HttpEventType.Response) {
        loader.isLoading.set(false);
      }
    }),
    catchError((error) => {
      console.log(error);
      loader.isLoading.set(false);
      throw error;
    })
  );
};
