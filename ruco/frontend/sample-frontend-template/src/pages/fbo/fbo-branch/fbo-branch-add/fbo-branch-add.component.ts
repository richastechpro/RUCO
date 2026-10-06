import { Component, OnInit, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import {
  firstValueFrom,
  lastValueFrom,
  map,
  Observable,
  of,
  startWith,
} from 'rxjs';
import { BranchService } from '../../../../services/branch.service';
import {
  MatAutocompleteModule,
  MatAutocompleteSelectedEvent,
} from '@angular/material/autocomplete';
import { AsyncPipe, NgFor } from '@angular/common';
import { LocationService } from '../../../../services/location.service';
import { FboBranchService } from '../../../../services/fbo-branch.service';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../../environments/environment.development';
import { FboService } from '../../../../services/fbo.service';

@Component({
  selector: 'app-fbo-branch-add',
  templateUrl: './fbo-branch-add.component.html',
  styleUrl: './fbo-branch-add.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    NgFor,
    AsyncPipe,
  ],
})
export class FboBranchAddComponent implements OnInit {
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
  id: number;
  fboName: string | null = null;
  constructor(
    private fb: FormBuilder,
    private locationService: LocationService,
    private fboBranchService: FboBranchService,
    private branchService: BranchService,
    private route: ActivatedRoute,
    private router: Router,
    private fboService: FboService
  ) {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.form = this.fb.group({
      fboBranchName: [null, Validators.required],
      fboBranchAddress: [null, Validators.required],
      representative: [null, Validators.required],
      state: [null, Validators.required],
      city: [null, Validators.required],
      area: [null],
      branch: [null, Validators.required],
      email: [null, Validators.required],
      mobileNo: [null, Validators.required],
      gstNo: [null, Validators.required],
      fssaiNo: [null, Validators.required],
      rate: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    const locations$ = lastValueFrom(this.locationService.getLocations());
    const fbo$ = firstValueFrom(this.fboService.getFboName(this.id));
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

        this.filteredBranches = this.form.get('branch')!.valueChanges.pipe(
          startWith(''),
          map((value) =>
            typeof value === 'string'
              ? this._filterBranch(value)
              : this.branches()
          )
        );
        return fbo$;
      })
      .then((res) => {
        this.fboName = res[0];
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
    const branch = payload.branch;
    payload.stateId = state !== null ? state.stateId : null;
    payload.cityId = city !== null ? city.cityId : null;
    payload.areaId = area !== null ? area.areaId : null;
    payload.branchId = branch !== null ? branch.id : null;
    this.fboBranchService.create(payload, this.id).subscribe(
      (res) => {
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
      environment.fbo,
      environment.edit,
      this.id,
    ]);
  }

  onSelectState(event: MatAutocompleteSelectedEvent) {
    this.cities.set([]);
    this.areas.set([]);
    this.branches.set([]);
    this.form.patchValue({ city: null, area: null, branch: null });
    const selected: any = event.option.value;
    const cities: any[] = selected.cities;
    this.cities.set(cities);
    this.filteredCities = of(cities);
    this.loadBranch(selected.stateId);
  }

  loadBranch(id: number) {
    this.branchService.getValidByStateId(id).subscribe(
      (res) => {
        this.branches.set(res);
        this.filteredBranches = of(this.branches());
      },
      (err) => {
        console.log(err);
      }
    );
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

  displayStateFn(value: any) {
    return value ? value.stateName : '';
  }

  displayCityFn(value: any) {
    return value ? value.cityName : '';
  }

  displayAreaFn(value: any) {
    return value ? value.areaName : '';
  }

  displayBranchFn(value: any) {
    return value ? value.branchName : '';
  }
}
