import { Injectable } from '@angular/core';
import { ApiService } from '../../shared/services/api.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MediaService {
  constructor(private api: ApiService) {
    this.api.setBaseUrl('http://localhost:9000'); // Media API
  }

  uploadMedia(formData: FormData): Observable<any> {
    return this.api.post('/media/upload', formData);
  }

  getMediaById(id: string): Observable<any> {
    return this.api.get(`/media/${id}`);
  }
}
