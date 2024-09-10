import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from './services/user.service';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const userService = inject(UserService);

  if (localStorage.getItem('id_token')) {
    return userService.getStatus().pipe(
      map((status) => {
        console.log('status', status);
        if (status) {
          return true;
        } else {
          router.navigate(['/login']);
          return false;
        }
      }),
      catchError(() => {
        router.navigate(['/login']);
        return of(false);
      })
    );
  } else {
    router.navigate(['/login']);
    return of(false);
  }
};
