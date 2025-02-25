import { Routes } from '@angular/router';
import { RoleGuard } from './auth/guards/auth.guard';
import { UnauthorizedComponent } from './shared/components/unauthorized/unauthorized.component';

export const routes: Routes = [
  { path: '', redirectTo: '/auth/login', pathMatch: 'full' },

  // Authentication (login/register)
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.routes').then((m) => m.authRoutes),
  },

  // User Profile

  {
    path: 'profile',
    loadChildren: () =>
      import('./user/profile.routes').then((m) => m.profileRoutes),
      canActivate: [RoleGuard],
  },

  // Seller routes
  {
    path: 'dashboard',
    loadChildren: () =>
      import('./dashboard/dashboard.routes').then((m) => m.dashboardRoutes),
    canActivate: [RoleGuard], 
    data: { role: 'SELLER' }, 
  },
  {
    path: 'media',
    loadChildren: () =>
      import('./media/media.routes').then((m) => m.mediaRoutes),
    canActivate: [RoleGuard],
    data: { role: 'SELLER' }, 
  },

  // Client routes
  {
    path: 'products',
    loadChildren: () =>
      import('./product/product.routes').then((m) => m.productRoutes),
    canActivate: [RoleGuard],
    data: { role: 'CLIENT' },
  },

  { path: 'unauthorized', component: UnauthorizedComponent },

  { path: '**', redirectTo: '/auth/login' },
];
