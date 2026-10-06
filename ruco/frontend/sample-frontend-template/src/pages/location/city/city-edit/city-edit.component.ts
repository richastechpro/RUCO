import { Component, Inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CityService } from '../../../../services/city.service';

@Component({
  selector: 'app-city-edit',
  templateUrl: './city-edit.component.html',
  styleUrl: './city-edit.component.scss',
  imports: [FormsModule, ReactiveFormsModule],
})
export class CityEditComponent {
  form: FormGroup;
  constructor(
    private fb: FormBuilder,
    private cityService: CityService,
    private dialogRef: MatDialogRef<CityEditComponent>,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    this.form = this.fb.group({
      stateName: [data.stateName, Validators.required],
      cityId: [data.cityId, Validators.required],
      cityName: [data.cityName, Validators.required],
    });
  }

  submit() {
    const payload = this.form.value;
    delete payload.state;
    this.cityService.update(payload).subscribe(
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
