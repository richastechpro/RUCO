import { Component, OnInit, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { FboBranchLoginService } from '../../../services/fbo-branch-login.service';
import { AsyncPipe, CommonModule, NgFor } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment.development';
import { FboBranchService } from '../../../services/fbo-branch.service';
import {
  first,
  firstValueFrom,
  lastValueFrom,
  map,
  Observable,
  of,
  startWith,
} from 'rxjs';
import {
  MatAutocompleteModule,
  MatAutocompleteSelectedEvent,
} from '@angular/material/autocomplete';
import { StateService } from '../../../services/state.service';
import { BranchService } from '../../../services/branch.service';
import { LocationService } from '../../../services/location.service';
import { FboService } from '../../../services/fbo.service';

@Component({
  selector: 'app-fbo-login-add',
  templateUrl: './fbo-login-add.component.html',
  styleUrl: './fbo-login-add.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatAutocompleteModule,
    NgFor,
    AsyncPipe,
  ],
})
export class FboLoginAddComponent implements OnInit {
  fboId: number;
  fbo: any = {};
  items: any[] = [];
  form: FormGroup;
  userTypes = ['FBO_SUPERADMIN', 'FBO_STATE', 'FBO_CITY', 'FBO_BRANCH'];
  locations = signal<any[]>([]);
  states = signal<any[]>([]);
  cities = signal<any[]>([]);
  fboBranches = signal<any[]>([]);
  filteredStates!: Observable<any>;
  filteredCities!: Observable<any>;
  filteredFboBranches!: Observable<any>;

  state = signal(false);
  city = signal(false);
  branch = signal(false);

  constructor(
    private fboBranchLoginService: FboBranchLoginService,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router,
    private locationService: LocationService,
    private fboBranchService: FboBranchService,
    private fboService: FboService
  ) {
    this.fboId = +this.route.snapshot.paramMap.get('id')!;
    this.form = this.fb.group({
      id: [null],
      email: [null, Validators.required],
      password: [null, Validators.required],
      username: [null, Validators.required],
      mobileNo: [null, Validators.required],
      type: [null, Validators.required],
      state: [null],
      city: [null],
      fboBranch: [null],
    });
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    const locations$ = firstValueFrom(this.locationService.getLocations());
    const fboBranches$ = firstValueFrom(
      this.fboBranchService.getValid(this.fboId)
    );
    const fboLogins$ = firstValueFrom(this.fboService.getFboLogins(this.fboId));

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
        return fboBranches$;
      })
      .then((res) => {
        this.fboBranches.set(res);
        this.filteredFboBranches = this.form
          .get('fboBranch')!
          .valueChanges.pipe(
            startWith(''),
            map((value) =>
              typeof value === 'string'
                ? this._filterFboBranch(value)
                : this.cities()
            )
          );
        return fboLogins$;
      })
      .then((res) => {
        this.items = res;
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  }

  onSelectState(event: MatAutocompleteSelectedEvent) {
    this.cities.set([]);
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

  private _filterCity(value: string): any[] {
    value = value.toLowerCase();
    return this.cities().filter((city) =>
      city.cityName.toLowerCase().includes(value)
    );
  }

  private _filterFboBranch(value: string): any[] {
    value = value.toLowerCase();
    return this.fboBranches().filter((fboBranch) =>
      fboBranch.fboBranchName.toLowerCase().includes(value)
    );
  }

  displayStateFn(value: any) {
    return value ? value.stateName : '';
  }

  displayCityFn(value: any) {
    return value ? value.cityName : '';
  }

  displayFboBranchFn(value: any) {
    return value ? value.fboBranchName : '';
  }

  editData(id: number) {
    this.fboBranchLoginService.getById(id).subscribe(
      (res: any) => {
        this.form.patchValue({
          id: res.id,
          email: res.email,
          password: res.password,
          type: res.type,
          username: res.username,
          mobileNo: res.mobileNo,
          state: res.state,
          city: res.city,
          fboBranch: res.fboBranch,
        });
      },
      (err) => {
        console.log(err);
      }
    );
  }

  submit() {
    const payload = this.form.value;
    const state = payload.state;
    const city = payload.city;
    const fboBranch = payload.fboBranch;
    delete payload.state;
    delete payload.city;
    delete payload.fboBranch;
    payload.stateId = state !== null ? state.stateId : null;
    payload.cityId = city !== null ? city.cityId : null;
    payload.fboBranchId = fboBranch !== null ? fboBranch.id : null;
    console.log(payload);
    this.fboBranchLoginService.create(payload, this.fboId).subscribe(
      () => {
        this.form.reset();
        this.state.set(false);
        this.city.set(false);
        this.branch.set(false);
        this.loadData();
      },
      (err) => {
        console.log(err);
        this.cancel();
      }
    );
  }

  cancel() {
    this.router.navigate([
      environment.servletPath,
      environment.fbo,
      environment.list,
    ]);
  }

  removeData(id: number) {
    this.fboBranchLoginService.delete(id).subscribe(
      () => {
        this.loadData();
      },
      (err) => {
        console.log(err);
        this.cancel();
      }
    );
  }

  changeType(event: Event) {
    const target = event.target as HTMLInputElement | null;
    if (target && target.value === 'FBO_SUPERADMIN') {
      this.state.set(false);
      this.city.set(false);
      this.branch.set(false);
    } else if (target && target.value === 'FBO_STATE') {
      this.state.set(true);
      this.city.set(false);
      this.branch.set(false);
    } else if (target && target.value === 'FBO_CITY') {
      this.state.set(true);
      this.city.set(true);
      this.branch.set(false);
    } else if (target && target.value === 'FBO_BRANCH') {
      this.state.set(false);
      this.city.set(false);
      this.branch.set(true);
    }
  }

  onlyNumbers(event: KeyboardEvent) {
    const key = event.key;
    if (!/^[0-9]$/.test(key)) {
      event.preventDefault();
    }
  }

  downloadFormat() {
    this.fboBranchLoginService.download(this.fboId).subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'fb-branch-upload-format.xlsx';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const formData = new FormData();
      formData.append('file', file);

      this.fboBranchLoginService.upload(formData, this.fboId).subscribe(
        (response) => {
          this.loadData();
        },
        (error) => {
          console.error(error);
        }
      );
    }
  }
}
