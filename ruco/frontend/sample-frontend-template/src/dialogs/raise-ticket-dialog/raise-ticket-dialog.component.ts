import { Component, inject, Inject, OnInit } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RateUpdateDialogComponent } from '../rate-update-dialog/rate-update-dialog.component';
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-raise-ticket-dialog',
  templateUrl: './raise-ticket-dialog.component.html',
  styleUrl: './raise-ticket-dialog.component.scss',
  imports: [FormsModule, ReactiveFormsModule],
})
export class RaiseTicketDialogComponent  {
  form: FormGroup;
  dialogRef = inject(MatDialogRef<RaiseTicketDialogComponent>);

  constructor(
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    console.log(data);
    this.form = this.fb.group({
      subject: [null, Validators.required],
      description: [null, Validators.required],
    });
  }

  submit() {
    this.dialogRef.close(this.form.value);
  }

  close() {
    this.dialogRef.close();
  }
}
