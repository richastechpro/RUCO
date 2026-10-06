import { Component, OnInit } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Branch } from '../../../interfaces/branch';
import { BranchService } from '../../../services/branch.service';
import { CustomerService } from '../../../services/customer.service';

@Component({
  selector: 'app-customer-edit',
  templateUrl: './customer-edit.component.html',
  styleUrl: './customer-edit.component.scss',
  imports: [FormsModule, ReactiveFormsModule, MatAutocompleteModule],
})
export class CustomerEditComponent implements OnInit {
  form: FormGroup;
  branches: Branch[] = [];
  filteredBranches!: Observable<any>;
  userTypes = ['CUSTOMER', 'PLANT'];
  id: number;
  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router,
    private branchService: BranchService,
    private route: ActivatedRoute
  ) {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.form = this.fb.group({
      id: [this.id, Validators.required],
      customerName: [null, Validators.required],
      customerAddress: [null, Validators.required],
      customerType: [null, Validators.required],
      email: [null, Validators.required],
      mobileNo: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    // this.branchService.getValid().subscribe(
    //   (res) => {
    //     this.branches = res;
    //     this.filteredBranches = this.form.get('branch')!.valueChanges.pipe(
    //       startWith(''),
    //       map((value) =>
    //         typeof value === 'string'
    //           ? this._filterBranch(value)
    //           : this.branches
    //       )
    //     );
    //   },
    //   (err) => {
    //     console.log(err);
    //   }
    // );
    this.customerService.getById(this.id).subscribe(
      (res) => {
        this.form.patchValue({
          customerName: res.customerName,
          customerAddress: res.customerAddress,
          customerType: res.customerType,
          email: res.email,
          mobileNo: res.mobileNo,
        });
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
    this.customerService.update(payload).subscribe(
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
      environment.customer,
      environment.list,
    ]);
  }
}
