import { Component, OnInit, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment.development';
import { Branch } from '../../../interfaces/branch';
import { BranchService } from '../../../services/branch.service';
import { UserService } from '../../../services/user.service';
import { AreaService } from '../../../services/area.service';
import { CityService } from '../../../services/city.service';
import { StateService } from '../../../services/state.service';
import { lastValueFrom, map, Observable, of, startWith } from 'rxjs';
import { TransporterService } from '../../../services/transporter.service';
import { AsyncPipe, NgFor } from '@angular/common';
import {
  MatAutocompleteModule,
  MatAutocompleteSelectedEvent,
} from '@angular/material/autocomplete';
import { LocationService } from '../../../services/location.service';

@Component({
  selector: 'app-transporter-add',
  templateUrl: './transporter-add.component.html',
  styleUrl: './transporter-add.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    AsyncPipe,
    NgFor,
  ],
})
export class TransporterAddComponent implements OnInit {
  form: FormGroup;
  locations = signal<any[]>([]);
  states = signal<any[]>([]);
  cities = signal<any[]>([]);
  areas = signal<any[]>([]);
  branches = signal<any[]>([]);
  filteredStates!: Observable<any>;
  filteredCities!: Observable<any>;
  filteredAreas!: Observable<any>;
  filteredBranches!: Observable<any>;
  constructor(
    private fb: FormBuilder,
    private transporterService: TransporterService,
    private router: Router,
    private branchService: BranchService,
    private locationService: LocationService
  ) {
    this.form = this.fb.group({
      transporterName: [null, Validators.required],
      transporterAddress: [null, Validators.required],
      state: [null, Validators.required],
      city: [null, Validators.required],
      area: [null, Validators.required],
      contactPerson: [null, Validators.required],
      mobileNo: [null, Validators.required],
      vehicleNo: [null, Validators.required],
      licenseNo: [null, Validators.required],
      gstNo: [null, Validators.required],
      panNo: [null, Validators.required],
      contractDate: [null, Validators.required],
      ratePerKg: [null, Validators.required],
      branch: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    const locations$ = lastValueFrom(this.locationService.getLocations());
    const branch$ = lastValueFrom(this.branchService.getValid());
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

        this.filteredCities = this.form.get('city')!.valueChanges.pipe(
          startWith(''),
          map((value) =>
            typeof value === 'string' ? this._filterCity(value) : this.cities()
          )
        );

        this.filteredAreas = this.form.get('area')!.valueChanges.pipe(
          startWith(''),
          map((value) =>
            typeof value === 'string' ? this._filterArea(value) : this.areas()
          )
        );
        return branch$;
      })
      .then((res) => {
        this.branches.set(res);
        this.filteredBranches = this.form.get('branch')!.valueChanges.pipe(
          startWith(''),
          map((value) =>
            typeof value === 'string'
              ? this._filterBranch(value)
              : this.branches()
          )
        );
      })
      .catch((err) => {
        console.log(err);
      });
  }

  onlyNumbers(event: KeyboardEvent) {
    const key = event.key;
    if (!/^[0-9]$/.test(key)) {
      event.preventDefault();
    }
  }

  submit() {
    const payload = this.form.value;
    const state = payload.state;
    const city = payload.city;
    const area = payload.area;
    const branch = payload.branch;
    delete payload.state;
    delete payload.city;
    delete payload.area;
    delete payload.branch;
    payload.stateId = state !== null ? state.stateId : null;
    payload.cityId = city !== null ? city.cityId : null;
    payload.areaId = area !== null ? area.areaId : null;
    payload.branchId = branch != null ? branch.id : null;
    this.transporterService.create(payload).subscribe(
      (_) => {
        this.cancel();
      },
      (err) => {
        console.log(err);
      }
    );
  }

  cancel() {
    this.router.navigate([
      environment.servletPath,
      environment.transporter,
      environment.list,
    ]);
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

  onSelectCity(event: MatAutocompleteSelectedEvent) {
    this.areas.set([]);
    this.form.patchValue({ area: null });
    const selectedCity: any = event.option.value;
    const areas: any[] = selectedCity.areas;
    this.areas.set(areas);
    this.filteredAreas = of(areas);
  }

  private _filterState(value: string): any[] {
    value = value.toLowerCase();
    return this.states().filter((state) =>
      state.stateName.toLowerCase().includes(value)
    );
  }

  private _filterCity(value: string): any[] {
    value = value.toLowerCase();
    return this.cities().filter((city) =>
      city.cityName.toLowerCase().includes(value)
    );
  }

  private _filterArea(value: string): any[] {
    value = value.toLowerCase();
    return this.areas().filter((area) =>
      area.areaName.toLowerCase().includes(value)
    );
  }

  private _filterBranch(value: string): any[] {
    value = value.toLowerCase();
    return this.branches().filter((branch) =>
      branch.branchName.toLowerCase().includes(value)
    );
  }

  displayFn(value: any) {
    return value ? value.branchName : '';
  }

  displayStateFn(value: any) {
    return value ? value.stateName : '';
  }

  displayCityFn(value: any) {
    return value ? value.cityName : '';
  }

  displayAreaFn(value: any) {
    return value ? value.areaName : '';
  }
}
