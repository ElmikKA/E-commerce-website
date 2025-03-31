import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  products = [
    { id: 1, name: 'Product A', price: 100 },
    { id: 2, name: 'Product B', price: 200 },
    { id: 3, name: 'Product C', price: 300 },
  ];
  constructor() { }

  // Simulations for now
  getProducts(): Observable<any[]> {
    return of(this.products);
  }

  addProduct(product: { id: number; name: string; price: number}): void {
    this.products.push(product);
  }

  deleteProduct(id: number): void {
    this.products = this.products.filter((product) => product.id !== id);
  }

  editProduct(updatedProduct: { id: number; name: string; price: number }): void {
    this.products = this.products.map((product) =>
      product.id === updatedProduct.id ? updatedProduct : product
    );
  }

}
