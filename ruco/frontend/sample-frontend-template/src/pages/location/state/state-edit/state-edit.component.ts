import { Component, Inject } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StateService } from '../../../../services/state.service';
import { StateAddComponent } from '../state-add/state-add.component';

@Component({
  selector: 'app-state-edit',
  templateUrl: './state-edit.component.html',
  styleUrl: './state-edit.component.scss',
  imports: [FormsModule, ReactiveFormsModule],
})
export class StateEditComponent {
  form: FormGroup;
  constructor(
    private fb: FormBuilder,
    private stateService: StateService,
    private dialogRef: MatDialogRef<StateAddComponent>,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    console.log(data);
    this.form = this.fb.group({
      stateId: [data.stateId, Validators.required],
      stateName: [data.stateName, Validators.required],
    });
  }

  submit() {
    const payload = this.form.value;
    this.stateService.update(payload).subscribe(
      (_) => {
        this.cancel();
      },
      (err) => {
        console.log(err);
      }
    );
  }

  cancel() {
    this.dialogRef.close();
  }
}
