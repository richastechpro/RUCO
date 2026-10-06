import { Component, Inject, inject } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ImageComponent } from '../../components/image/image.component';
import { environment } from '../../environments/environment.development';

@Component({
  selector: 'app-customer-billing',
  templateUrl: './customer-billing.component.html',
  styleUrl: './customer-billing.component.scss',
  imports: [FormsModule, ReactiveFormsModule],
})
export class CustomerBillingComponent {
  form: FormGroup;
  dialogRef = inject(MatDialogRef<ImageComponent>);

  constructor(
    private fb: FormBuilder,
    private router: Router,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    if (data.mode === 'edit') {
      const billing = this.data.billing;
      this.form = this.fb.group({
        address: [billing.address, Validators.required],
        mobileNo: [billing.mobileNo, Validators.required],
        state: [billing.state, Validators.required],
        city: [billing.city, Validators.required],
        pincode: [billing.pincode, Validators.required],
      });
    } else {
      this.form = this.fb.group({
        address: [null, Validators.required],
        mobileNo: [null, Validators.required],
        state: [null, Validators.required],
        city: [null, Validators.required],
        pincode: [null, Validators.required],
      });
    }
  }

  submit() {
    this.dialogRef.close(this.form.value);
  }

  navigateToListCourierPage() {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.courierMaster,
      environment.list,
    ]);
  }

  validateNumberInput(event: KeyboardEvent): void {
    const allowedKeys = [
      'Backspace',
      'ArrowLeft',
      'ArrowRight',
      'Tab',
      'Delete',
    ];

    if (!/^\d$/.test(event.key) && !allowedKeys.includes(event.key)) {
      event.preventDefault();
    }
  }

  close() {
    this.dialogRef.close();
  }
}
