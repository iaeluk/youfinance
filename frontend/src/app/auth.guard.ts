import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { inject } from '@angular/core';
import { map, catchError, of } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getUserInfo().pipe(
    map((user) => {
      if (user) {
        return true; // Usuário autenticado, permite acesso
      } else {
        router.navigate(['/login']); // Redireciona para login se não autenticado
        return false;
      }
    }),
    catchError(() => {
      router.navigate(['/login']); // Redireciona em caso de erro
      return of(false);
    })
  );
};
