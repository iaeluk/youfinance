import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { inject } from '@angular/core';
import { map, catchError, of, tap, switchMap } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Função para verificar a validade do token
  const isTokenExpired = (expiry: number): boolean => {
    const now = Date.now();
    return now > expiry;
  };

  // Verifica se há um token no localStorage
  const token = localStorage.getItem('jwtToken');
  const expiry = localStorage.getItem('tokenExpiry')
    ? parseInt(localStorage.getItem('tokenExpiry')!, 10)
    : 0;

  if (token && !isTokenExpired(expiry)) {
    // Se o token existir e não estiver expirado, permite o acesso
    return of(true);
  } else {
    // Se não houver token ou o token estiver expirado, faz a requisição para obtê-lo
    return authService.getToken().pipe(
      tap((res) => {
        if (res && res.token && res.expiresAt) {
          localStorage.setItem('jwtToken', res.token); // Armazena o novo token no localStorage
          localStorage.setItem(
            'tokenExpiry',
            new Date(res.expiresAt).getTime().toString()
          ); // Armazena a data de expiração
        }
      }),
      map((newToken) => !!newToken), // Se o token existir, retorna true, senão false
      catchError(() => {
        router.navigate(['/login']); // Redireciona em caso de erro
        return of(false);
      })
    );
  }
};
