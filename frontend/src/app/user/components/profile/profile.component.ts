import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/services/auth.service';
import { ApiService } from '../../../shared/services/api.service';
import { User } from '../../../shared/models/user.model';
import { MaterialModule } from '../../../shared/material.module';

@Component({
  selector: 'app-profile',
  imports: [MaterialModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  currentUser: User | null = null;
  isLoading = true;
  isSaving = false;
  errorMsg: string | null = null;


  constructor(private authService: AuthService, private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }
  
  loadUserProfile(): void {
    this.isLoading = true;
    const userData = this.authService.getUser();
    if (userData) {
      this.currentUser = { ...userData };
      this.isLoading = false;
    } else {
      this.authService.setUserFromToken().subscribe({
        next: (isAuthenticated) => {
          if (isAuthenticated) {
            this.currentUser = this.authService.getUser();
          } else {
            this.errorMsg = 'Failed to load user data.';
          }
        },
        error: () => {
          this.errorMsg = 'Failed to load user data.';
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    }
  }
  

  saveProfile(): void {
    if (!this.currentUser) return;

    this.isSaving = true;
    this.errorMsg = null;

    this.apiService.put<User>('/api/users/update', this.currentUser).subscribe({
      next: (updatedUser) => {
        this.currentUser = updatedUser;
        this.authService.setUserFromToken().subscribe();
      },
      error: (error) => {
        this.errorMsg = 'Failed to update profile.';
        console.error(error);
      },
      complete: () => {
        this.isSaving = false;
      },
    });
  }
}
