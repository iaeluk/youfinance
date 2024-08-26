import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { inject } from '@angular/core';
import { map, catchError, of, tap } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const isTokenExpired = (expiry: number): boolean => {
    const now = Date.now();
    return now > expiry;
  };

  const getQueryParams = () => {
    const params = new URLSearchParams(window.location.search);
    return {
      token: params.get('token'),
      expiresAt: params.get('expiresAt'),
    };
  };

  const { token: urlToken, expiresAt: urlExpiresAt } = getQueryParams();
  if (urlToken && urlExpiresAt) {
    localStorage.setItem('jwtToken', urlToken);
    localStorage.setItem('tokenExpiry', urlExpiresAt);
  }

  const token = localStorage.getItem('jwtToken');
  const expiry = localStorage.getItem('tokenExpiry')
    ? parseInt(localStorage.getItem('tokenExpiry')!, 10)
    : 0;

  if (token && !isTokenExpired(expiry)) {
    return of(true);
  } else {
    return authService.getToken().pipe(
      tap((res) => {
        if (res && res.token && res.expiresAt) {
          localStorage.setItem('jwtToken', res.token);
          localStorage.setItem(
            'tokenExpiry',
            new Date(res.expiresAt).getTime().toString()
          );
        }
      }),
      map((newToken) => !!newToken),
      catchError(() => {
        router.navigate(['/login']);
        return of(false);
      })
    );
  }
};
