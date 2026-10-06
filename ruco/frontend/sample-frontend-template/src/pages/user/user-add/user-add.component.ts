import { Component, OnInit, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { BranchService } from '../../../services/branch.service';
import { Branch } from '../../../interfaces/branch';
import { environment } from '../../../environments/environment.development';
import { map, Observable, startWith } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { AsyncPipe, NgFor } from '@angular/common';
import { SnackbarService } from '../../../services/snackbar.service';
import { Error } from '../../../constants/AppData';
import { AlertService } from '../../../services/alert.service';

@Component({
  selector: 'app-user-add',
  templateUrl: './user-add.component.html',
  styleUrl: './user-add.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    AsyncPipe,
    NgFor,
  ],
})
export class UserAddComponent implements OnInit {
  form: FormGroup;
  branches: Branch[] = [];
  filteredBranches!: Observable<any>;
  userTypes = ['SUPERADMIN', 'HR', 'PICKUPBOY', 'MANAGER', 'BRANCH_EXECUTIVE'];
  showBranch = signal(false);

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private branchService: BranchService,
    private snackbarService: SnackbarService
  ) {
    this.form = this.fb.group({
      loginId: [null, Validators.required],
      password: [null, Validators.required],
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
    this.branchService.getValid().subscribe(
      (res) => {
        this.branches = res;
        this.filteredBranches = this.form.get('branch')!.valueChanges.pipe(
          startWith(''),
          map((value) =>
            typeof value === 'string'
              ? this._filterBranch(value)
              : this.branches
          )
        );
      },
      (err) => {
        console.log(err);
      }
    );
  }

  private _filterBranch(value: string): any[] {
    value = value.toLowerCase();
    return this.branches.filter((branch) =>
      branch.branchName.toLowerCase().includes(value)
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
    this.userService.create(payload).subscribe(
      (res) => {
        this.cancel();
      },
      (err) => {
        console.log(err.error);
        if (err.error?.message.startsWith('Duplicate entry')) {
          this.snackbarService.openSnackBar({
            title: 'Error',
            msg: 'LoginId already exists',
            type: Error,
          });
        }
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
