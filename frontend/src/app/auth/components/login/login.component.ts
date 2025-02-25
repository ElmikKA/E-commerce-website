import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { MaterialModule } from '../../../shared/material.module';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [ReactiveFormsModule, MaterialModule, RouterModule],
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router,
    ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }
    
  onSubmit(): void {
    if (this.loginForm.valid) {
      const credentials = this.loginForm.value;
  
      this.authService.login(credentials).subscribe({
        next: (response) => {
          if (response && response.token) {
            this.authService.setToken(response.token);
            this.authService.setUserFromToken();
            
            this.toastr.success('Login successful!', 'Success');
  
            const userRole = this.authService.getRole();
            
            if (userRole === 'SELLER') {
              this.router.navigate(['/dashboard']).then((success) => {
                if (success) {
                  console.log('Navigated to /dashboard')
                } else {
                  console.error('Failed to navigate.')
                }
              });
            } else {
              this.router.navigate(['/products']);
            }
          } else {
            console.error('Login response does not contain a token:', response);
            this.toastr.error('Login failed. No token received.', 'Error');
          }
        },
        error: (err) => {
          this.toastr.error('Invalid credentials. Please try again.', 'Login Failed');
        }
      });
    } else {
      this.toastr.warning('Please fill out all required fields.', 'Validation Error');
    }
  }
  

}
