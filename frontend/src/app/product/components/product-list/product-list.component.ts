import { Component, OnInit } from '@angular/core';
import { MaterialModule } from '../../../shared/material.module';
import { ProductService } from '../../services/product.service';
import { MatDialog } from '@angular/material/dialog';
import { ProductDetailComponent } from '../product-detail/product-detail.component';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
  standalone: true,
  imports: [MaterialModule],
})
export class ProductListComponent implements OnInit {
  products: any[] = [];
  filteredProducts: any[] = [];
  searchTerm: string = '';
  minPrice: number | null = null;
  maxPrice: number | null = null;
  pageSize = 5;
  currentPage = 0;

  constructor(private productService: ProductService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.fetchProducts();
  }

  fetchProducts(): void {
    this.productService.getProducts().subscribe((data) => {
      this.products = data;
      this.minPrice = Math.min(...this.products.map((p) => p.price));
      this.maxPrice = Math.max(...this.products.map((p) => p.price));
      this.applyFilters();
    });
  }



  applyFilters(): void {
    let filtered = this.products.filter((product) => {
      const matchesSearch = 
        !this.searchTerm ||
        product.name.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesMinPrice =
        this.minPrice === null || product.price >= this.minPrice;
      const matchesMaxPrice =
        this.maxPrice === null || product.price <= this.maxPrice;

      return matchesSearch && matchesMinPrice && matchesMaxPrice;
    });

    const start = this.currentPage * this.pageSize;
    const end = start + this.pageSize;
    this.filteredProducts = filtered.slice(start, end);
  }

  onPageChange(event: PageEvent): void {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.applyFilters();
  }

  viewProduct(product: any): void {
    this.dialog.open(ProductDetailComponent, {
      width: '400px',
      data: product,
    })
  }
}
