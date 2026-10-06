import { Component, Inject, inject } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment.development';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ImageComponent } from '../../components/image/image.component';

@Component({
  selector: 'app-customer-contact',
  templateUrl: './customer-contact.component.html',
  styleUrl: './customer-contact.component.scss',
  imports: [FormsModule, ReactiveFormsModule],
})
export class CustomerContactComponent {
  form: FormGroup;
  dialogRef = inject(MatDialogRef<ImageComponent>);

  constructor(
    private fb: FormBuilder,
    private router: Router,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    if (data.mode === 'edit') {
      const contact = this.data.contact
      this.form = this.fb.group({
        name: [contact.name, Validators.required],
        mobileNo: [contact.mobileNo, Validators.required],
        landLineNo: [contact.landLineNo, Validators.required],
        email: [contact.email, Validators.required],
        designation: [contact.designation, Validators.required],
        remark: [contact.remark, Validators.required],
      });
    } else {
      this.form = this.fb.group({
        name: [null, Validators.required],
        mobileNo: [null, Validators.required],
        landLineNo: [null, Validators.required],
        email: [null, Validators.required],
        designation: [null, Validators.required],
        remark: [null, Validators.required],
      });
    }
  }

  submit() {
    this.dialogRef.close(this.form.value);
  }

  navigateToListCourierPage() {
    this.router.navigate([
      environment.servletPath,
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
