import { AsyncPipe, NgFor } from '@angular/common';
import { Component, Inject, OnInit, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import {
  MatAutocompleteModule,
  MatAutocompleteSelectedEvent,
} from '@angular/material/autocomplete';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, lastValueFrom, startWith, map, of } from 'rxjs';
import { AreaService } from '../../../../services/area.service';
import { LocationService } from '../../../../services/location.service';
import { AreaAddComponent } from '../../area/area-add/area-add.component';
import { CityService } from '../../../../services/city.service';

@Component({
  selector: 'app-city-add',
  templateUrl: './city-add.component.html',
  styleUrl: './city-add.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    AsyncPipe,
    NgFor,
  ],
})
export class CityAddComponent implements OnInit {
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
    private locationService: LocationService,
    private cityService: CityService,
    private dialogRef: MatDialogRef<CityAddComponent>,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    this.form = this.fb.group({
      state: [null, Validators.required],
      cityName: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    const locations$ = lastValueFrom(this.locationService.getLocations());
    locations$
      .then((res) => {
        this.locations.set(res);
        this.states.set(this.locations());

        this.filteredStates = this.form.get('state')!.valueChanges.pipe(
          startWith(''),
          map((value) =>
            typeof value === 'string' ? this._filterState(value) : this.states()
          )
        );
      })
      .catch((err) => {
        console.log(err);
      });
  }

  onSelectState(event: MatAutocompleteSelectedEvent) {
    this.cities.set([]);
    this.areas.set([]);
    this.form.patchValue({ city: null, area: null });
    const selected: any = event.option.value;
    const cities: any[] = selected.cities;
    this.cities.set(cities);
    this.filteredCities = of(cities);
  }

  private _filterState(value: string): any[] {
    value = value.toLowerCase();
    return this.states().filter((state) =>
      state.stateName.toLowerCase().includes(value)
    );
  }

  displayStateFn(value: any) {
    return value ? value.stateName : '';
  }

  submit() {
    const payload = this.form.value;
    const state = payload.state;
    delete payload.state;
    payload.stateId = state !== null ? state.stateId : null;
    this.cityService.create(payload).subscribe(
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
