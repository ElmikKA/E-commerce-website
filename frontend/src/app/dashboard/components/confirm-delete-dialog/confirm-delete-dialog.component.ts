import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MaterialModule } from '../../../shared/material.module';

@Component({
  selector: 'app-confirm-delete-dialog',
  template: `
    <h2 mat-dialog-title>Confirm Delete</h2>
    <mat-dialog-content>Are you sure you want to delete this product?</mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button color="warn" (click)="onConfirm()">Delete</button>
    </mat-dialog-actions>
  `,
  standalone: true,
  imports: [MaterialModule],
})
export class ConfirmDeleteDialogComponent {
  constructor(private dialogRef: MatDialogRef<ConfirmDeleteDialogComponent>) {}

  onConfirm(): void {
    this.dialogRef.close(true); // Confirm deletion
  }

  onCancel(): void {
    this.dialogRef.close(false); // Cancel deletion
  }
}
