import {
  HttpEvent,
  HttpEventType,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { LoaderService } from '../services/loader.service';
import { Observable, tap } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const loader: LoaderService = inject(LoaderService);
  loader.showLoader(true);
  const user = JSON.parse(localStorage.getItem('user')!);
  if (user) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${user.token}`,
      },
    });

    return next(authReq);
  }

  return next(req);
};