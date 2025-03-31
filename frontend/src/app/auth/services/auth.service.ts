import { Injectable } from '@angular/core';
import { ApiService } from '../../shared/services/api.service';
import { BehaviorSubject, catchError, Observable, of, switchMap, tap } from 'rxjs';
import { User } from '../../shared/models/user.model';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private role: string | null = null;
  private currentUser: User | null = null;
  private isAuthenticatedSubject!: BehaviorSubject<boolean>;

  constructor(private api: ApiService) {
    this.api.setBaseUrl('http://localhost:8080'); // Users API
    this.isAuthenticatedSubject = new BehaviorSubject<boolean>(this.isLoggedIn());
  }

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.api.post<{ token: string }>('/api/v1/auth/login', credentials).pipe(
      tap((response) => {
        this.setToken(response.token);
        this.setUserFromToken().subscribe();
      }),
      catchError((error) => {
        console.error('Login failed:', error);
        throw error;
      })
    );
  }

  register(user: Omit<User, 'id'>): Observable<{ token: string; user: User }> {
    return this.api.post<{ token: string; user: User }>('/api/v1/auth/register', user).pipe(
      tap((response) => {
        this.setToken(response.token);
        this.setUserFromToken().subscribe();
      }),
      catchError((error) => {
        console.error('Registration failed:', error);
        throw error;
      })
    )
  }

  logout(): void {
    this.api.setToken('');
    localStorage.clear();
    this.currentUser = null;
    this.role = null;
    this.isAuthenticatedSubject.next(false);
  }

  isLoggedIn(): boolean {
    const token = this.api.getToken();
    if (!token) return false;

    const decodedToken: any = jwt_decode(token);
    const currentTime = Date.now() / 1000;
    if (decodedToken.exp < currentTime) {
      this.logout();
      return false;
    }
    return true;

  }

  setToken(token: string): void {
    this.api.setToken(token);
    this.isAuthenticatedSubject.next(true);
  }

  setUserFromToken(): Observable<boolean> {
    const token = this.api.getToken();
    if (!token) return of(false);

    if(this.currentUser) {
      return of(true);
    }

    const decodedToken: any = jwt_decode(token);

    if (!decodedToken.userRole) {
      console.error('AuthService - Role is undefined in decoded token!');
      return of(false);
    }

    this.currentUser = {
      id: decodedToken.userId,
      name: undefined,
      email: decodedToken.sub,
      role: (decodedToken.userRole || 'CLIENT').toUpperCase(),
      avatar: undefined,
    };

    this.role = this.currentUser.role;

    return this.fetchUserDetails(this.currentUser.id).pipe(
      tap(() => this.isAuthenticatedSubject.next(true)),
      switchMap(() => of(true)),
      catchError((error) => {
        console.error('Failed to fetch user details:', error);
        return of(false);
      })
    );
  }
  
  

  fetchUserDetails(userId: string): Observable<User> {
    return this.api.get<User>(`/api/users/fetchById/${userId}`).pipe(
      tap((userDetails) => {
        if (userDetails) {
          this.currentUser = userDetails;
          this.role = userDetails.role;
        }
      }),
      catchError((error) => {
        console.error('Failed to fetch user details:', error);
        throw error;
      })
    );
  }

  getUser(): User | null {
    if (!this.currentUser) {
      console.warn('[AuthService] User is not set.')
      return null;
    }
    return this.currentUser;
  }

  getRole(): string | null {
    return this.role ? this.role.toUpperCase() : null;
  }

  hasRole(role: string): boolean {
    return this.getRole() === role;
  }

  getToken(): string | null {
    return this.api.getToken();
  }

  getAuthStatus(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  updateUser(user: User): void {
    this.currentUser = user;
    this.isAuthenticatedSubject.next(true);
  }
}
