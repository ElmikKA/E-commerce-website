import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { MaterialModule } from '../../../shared/material.module';
import { ProductService } from '../../services/product.service';
import { MatDialog } from '@angular/material/dialog';
import { AddProductDialogComponent } from '../add-product-dialog/add-product-dialog.component';
import { ConfirmDeleteDialogComponent } from '../confirm-delete-dialog/confirm-delete-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../auth/services/auth.service';
import { User } from '../../../shared/models/user.model';

@Component({
  selector: 'app-dashboard',
  imports: [MaterialModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  standalone: true,
})
export class DashboardComponent implements OnInit {
  displayedColumns: string[] = ['name', 'price', 'actions'];
  products: any[] = [];
  username: string = '';

  constructor(
    private productService: ProductService,
    private authService: AuthService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
      this.fetchProducts();
      this.getUsername();
  }

  fetchProducts(): void {
    this.productService.getProducts().subscribe((data) => {
      this.products = data;
    })
  }

  getUsername(): void {
    const user = this.authService.getUser();
    this.username = user?.name || user?.email || 'Guest';
  }

  onAddProduct(): void {
  const dialogRef = this.dialog.open(AddProductDialogComponent, {
    width: '400px',
  });

  dialogRef.afterClosed().subscribe((result) => {
    if (result) {
      this.productService.addProduct({
        id: this.products.length + 1,
        ...result,
      });
      this.fetchProducts();
      this.snackBar.open('Product added successfully!', 'Close', { duration: 3000 });
    }
  });
}

onEdit(product: any): void {
  const dialogRef = this.dialog.open(AddProductDialogComponent, {
    width: '400px',
    data: product,
  });

  dialogRef.afterClosed().subscribe((result) => {
    if (result) {
      const updatedProduct = { ...product, ...result };
      this.productService.editProduct(updatedProduct);
      this.fetchProducts();
      this.snackBar.open('Product updated successfully!', 'Close', { duration: 3000 });
    }
  });
}

onDelete(product: any): void {
  const dialogRef = this.dialog.open(ConfirmDeleteDialogComponent, {
    width: '300px',
  });

  dialogRef.afterClosed().subscribe((confirmed) => {
    if (confirmed) {
      this.productService.deleteProduct(product.id);
      this.fetchProducts();
      this.snackBar.open('Product deleted successfully!', 'Close', { duration: 3000 });
    }
  });
}


}
