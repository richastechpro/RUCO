import { Component, Inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { StateService } from '../../../../services/state.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-state-add',
  templateUrl: './state-add.component.html',
  styleUrl: './state-add.component.scss',
  imports: [FormsModule, ReactiveFormsModule],
})
export class StateAddComponent {
  form: FormGroup;
  constructor(
    private fb: FormBuilder,
    private stateService: StateService,
    private dialogRef: MatDialogRef<StateAddComponent>,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    this.form = this.fb.group({
      state: [null, Validators.required],
    });
  }

  submit() {
    const payload = this.form.value;
    const state = payload.state;
    delete payload.state;
    payload.stateName = state;
    this.stateService.create(payload).subscribe(
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
