import { Component, Inject } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MaterialModule } from '../../../shared/material.module';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-add-product-dialog',
  imports: [MaterialModule, ReactiveFormsModule],
  templateUrl: './add-product-dialog.component.html',
  styleUrl: './add-product-dialog.component.scss'
})
export class AddProductDialogComponent {
  addProductForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddProductDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.addProductForm = this.fb.group({
      name: [data?.name || '', Validators.required],
      price: [data?.price || 0, [Validators.required, Validators.min(1)]],
    });
  }

  onSave(): void {
    if (this.addProductForm.valid) {
      this.dialogRef.close(this.addProductForm.value); // Pass data back to parent
    }
  }

  onCancel(): void {
    this.dialogRef.close(null); // Close without saving
  }
}
