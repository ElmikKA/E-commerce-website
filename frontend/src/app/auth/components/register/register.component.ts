import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { MaterialModule } from '../../../shared/material.module';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, MaterialModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService,
    ) {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      role: ['CLIENT', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const registerData = this.registerForm.value;

      this.authService.register(registerData).subscribe({
        next: (response) => {
          this.toastr.success('Registration successful!', 'Success');
          console.log('User registered successfully', response);

          this.authService.setToken(response.token);
          this.authService.setUserFromToken();

          if (registerData.role === 'SELLER') {
            this.router.navigate(['/dashboard']);
          } else {
            this.router.navigate(['/products']);
          }
        },
        error: (err) => {
          this.toastr.error('Registration failed. Please try again.', 'Error');
          console.error('Registration failed', err);
        }
      });
    } else {
      this.toastr.warning('Please fill out all required fields.', 'Validation Error');
      console.log('Form is invalid');
    }
  }

  get passwordMatch(): boolean{
    return (
      this.registerForm.get('password')?.value === this.registerForm.get('confirmPassword')?.value
    );
  }
}
