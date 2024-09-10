import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../environments/environment';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const backendUrl = environment.apiUrl;

  if (req.url.startsWith(backendUrl)) {
    const idToken = localStorage.getItem('id_token');

    if (idToken) {
      const clonedRequest = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${idToken}`),
      });
      return next(clonedRequest);
    }
  }

  return next(req);
};
