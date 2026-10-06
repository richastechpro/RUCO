import { Component, inject, Inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RateUpdateDialogComponent } from '../rate-update-dialog/rate-update-dialog.component';

@Component({
  selector: 'app-update-pwd-dialog',
  templateUrl: './update-pwd-dialog.component.html',
  styleUrl: './update-pwd-dialog.component.scss',
  imports: [FormsModule, ReactiveFormsModule],
})
export class UpdatePwdDialogComponent {
  form: FormGroup;
  dialogRef = inject(MatDialogRef<RateUpdateDialogComponent>);

  constructor(
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    this.form = this.fb.group({
      pwd: [ null, Validators.required],
    });
  }

  submit() {
    this.dialogRef.close(this.form.value);
  }

  close() {
    this.dialogRef.close();
  }
}
