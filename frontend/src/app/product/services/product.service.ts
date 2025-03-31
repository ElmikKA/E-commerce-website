import { Injectable } from '@angular/core';
import { ApiService } from '../../shared/services/api.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  constructor(private api: ApiService) {
    this.api.setBaseUrl('http://localhost:8090'); // Products API
  }

  getProducts(): Observable<any> {
    return this.api.get('/products');
  }

  getProductById(id: number): Observable<any> {
    return this.api.get(`/products/${id}`);
  }

  addProduct(product: any): Observable<any> {
    return this.api.post('/products', product);
  }

  updateProduct(id: number, product: any): Observable<any> {
    return this.api.put(`/products/${id}`, product);
  }

  deleteProduct(id: number): Observable<any> {
    return this.api.delete(`/products/${id}`);
  }
}
