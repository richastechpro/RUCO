import { Component, OnInit, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { BranchService } from '../../../services/branch.service';
import { Branch } from '../../../interfaces/branch';
import { environment } from '../../../environments/environment.development';
import { AsyncPipe, NgFor } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { firstValueFrom, map, Observable, startWith } from 'rxjs';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrl: './user-edit.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    AsyncPipe,
    NgFor,
  ],
})
export class UserEditComponent implements OnInit {
  form: FormGroup;
  branches: any[] = [];
  filteredBranches!: Observable<any>;
  userTypes = ['SUPERADMIN', 'HR', 'PICKUPBOY', 'MANAGER', 'BRANCH_EXECUTIVE'];
  id: number;
  showBranch = signal(false);
  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private branchService: BranchService,
    private route: ActivatedRoute
  ) {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.form = this.fb.group({
      id: [this.id, Validators.required],
      loginId: [null, Validators.required],
      username: [null, Validators.required],
      email: [null, Validators.required],
      mobileNo: [null, Validators.required],
      userType: [null, Validators.required],
      branch: [null, Validators.required],
    });

    this.form.get('userType')!.valueChanges.subscribe((userType: string) => {
      if (userType && this.userTypes.indexOf(userType) > 1) {
        this.showBranch.set(true);
        return;
      }
      this.showBranch.set(false);
      this.form.get('branch')!.setValue(null);
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    const branch$ = firstValueFrom(this.branchService.getValid());
    const user$ = firstValueFrom(this.userService.getById(this.id));

    branch$
      .then((res) => {
        this.branches = res;
        this.filteredBranches = this.form.get('branch')!.valueChanges.pipe(
          startWith(''),
          map((value) =>
            typeof value === 'string'
              ? this._filterBranch(value)
              : this.branches
          )
        );
        return user$;
      })
      .then((res) => {
        const branch = this.branches.find((b) => b.id === res.branchId);
        this.form.patchValue({
          loginId: res.loginId,
          username: res.username,
          email: res.email,
          mobileNo: res.mobileNo,
          userType: res.userType,
          branch: branch,
        });
      })
      .catch((err) => {
        console.log(err);
      });
  }

  private _filterBranch(value: string): any[] {
    value = value.toLowerCase();
    return this.branches.filter((state) =>
      state.branchName.toLowerCase().includes(value)
    );
  }

  displayFn(value: any) {
    return value ? value.branchName : '';
  }

  onlyNumbers(event: KeyboardEvent) {
    const key = event.key;
    if (!/^[0-9]$/.test(key)) {
      event.preventDefault();
    }
  }

  submit() {
    const payload = this.form.value;
    if (this.userTypes.indexOf(payload.userType) > 1) {
      const branch = payload.branch;
      payload.branchId = branch.id;
    }
    this.userService.update(payload).subscribe(
      (res) => {
        this.router.navigate([
          environment.servletPath,
          environment.user,
          environment.list,
        ]);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  cancel() {
    this.router.navigate([
      environment.servletPath,
      environment.user,
      environment.list,
    ]);
  }
}
