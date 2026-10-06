import { Component, Inject, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import {
  MatAutocompleteModule,
} from '@angular/material/autocomplete';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { AreaService } from '../../../../services/area.service';
import { AreaAddComponent } from '../area-add/area-add.component';

@Component({
  selector: 'app-area-edit',
  templateUrl: './area-edit.component.html',
  styleUrl: './area-edit.component.scss',
  imports: [FormsModule, ReactiveFormsModule, MatAutocompleteModule],
})
export class AreaEditComponent {
  form: FormGroup;
  locations = signal<any[]>([]);
  states = signal<any[]>([]);
  cities = signal<any[]>([]);
  areas = signal<any[]>([]);
  filteredStates!: Observable<any>;
  filteredCities!: Observable<any>;
  filteredAreas!: Observable<any>;
  constructor(
    private fb: FormBuilder,
    private areaService: AreaService,
    private dialogRef: MatDialogRef<AreaAddComponent>,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    this.form = this.fb.group({
      state: [data.stateName, Validators.required],
      city: [data.cityName, Validators.required],
      areaId: [data.areaId, Validators.required],
      areaName: [data.areaName, Validators.required],
    });
  }

  submit() {
    const payload = this.form.value;
    delete payload.city;
    delete payload.area;
    this.areaService.update(payload).subscribe(
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
