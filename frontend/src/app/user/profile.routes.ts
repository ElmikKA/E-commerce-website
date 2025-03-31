import { Routes } from '@angular/router';
import { ProfileComponent } from './components/profile/profile.component';
import { RoleGuard } from '../auth/guards/auth.guard';

export const profileRoutes: Routes = [
  { path: '', component: ProfileComponent, canActivate: [RoleGuard] },
];
