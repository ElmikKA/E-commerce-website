import { Component, OnDestroy, OnInit } from '@angular/core';
import { MaterialModule } from '../../material.module';
import { Router } from '@angular/router';
import { User } from '../../models/user.model';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-navbar',
  imports: [MaterialModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  standalone: true,
})
export class NavbarComponent implements OnInit, OnDestroy {
  isLoggedIn: boolean = false;
  currentUser: User | null = null;
  private authSubscription!: Subscription;

  constructor(private authService: AuthService, private router: Router) {
    this.isLoggedIn = this.authService.isLoggedIn();
  }

  ngOnInit(): void {
    this.authSubscription = this.authService.getAuthStatus().subscribe((isAuthenticated) => {
      this.isLoggedIn = isAuthenticated;
      if (isAuthenticated) {
        this.currentUser = this.authService.getUser();
      } else {
        this.currentUser = null;
      }
    });

    this.authService.setUserFromToken().subscribe();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }

  ngOnDestroy(): void {
      if (this.authSubscription) {
        this.authSubscription.unsubscribe();
      }
  }

}
