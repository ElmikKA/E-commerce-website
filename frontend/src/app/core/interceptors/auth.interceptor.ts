import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '../../auth/services/auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    const cloned = req.clone({
      setHeaders: {
        Authorization: token ? `Bearer ${token}` : '',
      },
    });
    console.log('[AuthInterceptor] Headers:', cloned.headers);

    return next.handle(cloned);
  }
}
