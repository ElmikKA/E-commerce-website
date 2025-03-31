import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.authService.setUserFromToken().pipe(
      switchMap(() => {
        const requiredRole = route.data['role'];

        if (!requiredRole) {
          return of(true);
        } 

        const userRole = this.authService.getRole();
        console.log('[RoleGuard] Checking role:', userRole, 'Required:', requiredRole);

        if (userRole === requiredRole) {
          return of(true);
        }

        this.router.navigate(['/unauthorized']);
        return of(false);
      }),
      catchError(() => {
        this.router.navigate(['/auth/login']);
        return of(false);
      })
    );
  }
}
