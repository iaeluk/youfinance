import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Obtém o token do localStorage
  const token = localStorage.getItem('jwtToken');

  // Se o token não estiver presente, apenas passe a requisição sem alterações
  if (!token) {
    return next(req);
  }

  // Se o token estiver presente, clone a requisição e adicione o cabeçalho Authorization
  const clonedRequest = req.clone({
    headers: req.headers.set('Authorization', `Bearer ${token}`),
  });

  // Passe a requisição clonada para o próximo manipulador
  return next(clonedRequest);
};
