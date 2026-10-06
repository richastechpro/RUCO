import { CanActivateFn } from '@angular/router';

export const parentAuthGuard: CanActivateFn = (route, state) => {
  return route.routeConfig?.path === 'user';
};
