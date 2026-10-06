import { Component, inject, Inject } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-rate-update-dialog',
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './rate-update-dialog.component.html',
  styleUrl: './rate-update-dialog.component.scss',
})
export class RateUpdateDialogComponent {
  form: FormGroup;
  dialogRef = inject(MatDialogRef<RateUpdateDialogComponent>);

  constructor(
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    const rate = this.data.rate;
    this.form = this.fb.group({
      rate: [rate !== null ? rate.price : null, Validators.required],
    });
  }

  submit() {
    this.dialogRef.close(this.form.value);
  }

  close() {
    this.dialogRef.close();
  }
}
