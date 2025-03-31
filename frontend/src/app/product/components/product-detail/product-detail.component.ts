import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MaterialModule } from '../../../shared/material.module';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls:[ './product-detail.component.scss'],
  standalone: true,
  imports: [MaterialModule],
  encapsulation: ViewEncapsulation.None,
})
export class ProductDetailComponent implements OnInit {

  constructor(

    @Inject(MAT_DIALOG_DATA) public product: any,
    public dialogRef: MatDialogRef<ProductDetailComponent>,
  ) {}

  ngOnInit(): void {
  }

  close(): void {
    this.dialogRef.close();
  }
}
