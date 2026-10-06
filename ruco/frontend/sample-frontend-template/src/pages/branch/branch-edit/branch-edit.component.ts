import { Component, OnInit, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import {
  firstValueFrom,
  lastValueFrom,
  map,
  Observable,
  of,
  startWith,
} from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { AreaService } from '../../../services/area.service';
import { BranchService } from '../../../services/branch.service';
import { CityService } from '../../../services/city.service';
import { CompanyService } from '../../../services/company.service';
import { StateService } from '../../../services/state.service';
import { AsyncPipe, NgFor } from '@angular/common';
import {
  MatAutocompleteModule,
  MatAutocompleteSelectedEvent,
} from '@angular/material/autocomplete';
import { LocationService } from '../../../services/location.service';

@Component({
  selector: 'app-branch-edit',
  templateUrl: './branch-edit.component.html',
  styleUrl: './branch-edit.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    AsyncPipe,
    NgFor,
  ],
})
export class BranchEditComponent implements OnInit {
  form: FormGroup;
  id: number;
  locations = signal<any[]>([]);
  states = signal<any[]>([]);
  cities = signal<any[]>([]);
  areas = signal<any[]>([]);
  filteredStates!: Observable<any>;
  filteredCities!: Observable<any>;
  filteredAreas!: Observable<any>;

  constructor(
    private fb: FormBuilder,
    private branchService: BranchService,
    private router: Router,
    private route: ActivatedRoute,
    private locationService: LocationService
  ) {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.form = this.fb.group({
      id: [this.id, Validators.required],
      branchName: [null, Validators.required],
      branchAddress: [null, Validators.required],
      state: [null, Validators.required],
      city: [null, Validators.required],
      area: [null, Validators.required],
      panNo: [null, Validators.required],
      gstNo: [null, Validators.required],
      warehouseName: [null, Validators.required],
      warehouseAddress: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    const locations$ = lastValueFrom(this.locationService.getLocations());
    const branchPromise = firstValueFrom(this.branchService.getById(this.id));
    locations$
      .then((res) => {
        this.locations.set(res);
        this.states.set(this.locations());
        return branchPromise;
      })
      .then((res: any) => {
        this.form.patchValue({
          state: res.state,
          city: res.city,
          area: res.area,
          branchName: res.branchName,
          branchAddress: res.branchAddress,
          panNo: res.panNo,
          gstNo: res.gstNo,
          warehouseName: res.warehouseName,
          warehouseAddress: res.warehouseAddress,
        });
        const state = res.state;
        if (state !== null) {
          const stateId = state.stateId;
          const cities = this.locations().find(
            (l) => l.stateId === stateId
          ).cities;
          this.cities.set(cities);

          const city = res.city;
          if (city !== null) {
            const areas = this.cities().find(
              (c) => c.cityId === city.cityId
            ).areas;
            this.areas.set(areas);
          }
        }

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
      })
      .catch((err) => {
        console.log(err);
      });
  }

  submit() {
    const payload = this.form.value;
    const state = payload.state;
    const city = payload.city;
    const area = payload.area;
    delete payload.state;
    delete payload.city;
    delete payload.area;
    payload.stateId = state !== null ? state.stateId : null;
    payload.cityId = city !== null ? city.cityId : null;
    payload.areaId = area !== null ? area.areaId : null;
    this.branchService.update(payload).subscribe(
      (_) => this.cancel(),
      (err) => console.log(err)
    );
  }

  cancel() {
    this.router.navigate([
      environment.servletPath,
      environment.branch,
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
