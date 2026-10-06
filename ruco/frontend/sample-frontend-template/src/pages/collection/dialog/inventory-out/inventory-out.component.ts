import { AsyncPipe, NgFor } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
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
import { Observable, startWith, map, firstValueFrom } from 'rxjs';
import { BranchService } from '../../../../services/branch.service';
import { CollectionService } from '../../../../services/collection.service';
import { InventoryInComponent } from '../inventory-in/inventory-in.component';
import { CustomerService } from '../../../../services/customer.service';
import { TransporterService } from '../../../../services/transporter.service';

@Component({
  selector: 'app-inventory-out',
  templateUrl: './inventory-out.component.html',
  styleUrl: './inventory-out.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    FormsModule,
    AsyncPipe,
    NgFor,
  ],
})
export class InventoryOutComponent implements OnInit {
  form: FormGroup;
  customers: any[] = [];
  filteredCustomers!: Observable<any>;
  branches: any[] = [];
  filteredBranches!: Observable<any>;
  // transporters: any[] = [];
  // filteredTransporters!: Observable<any>;
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<InventoryInComponent>,
    private collectionService: CollectionService,
    private customerService: CustomerService,
    private branchService: BranchService,
    // private transporterService: TransporterService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.form = this.fb.group({
      branch: [null, Validators.required],
      warehouse: [null, Validators.required],
      customer: [null, Validators.required],
      availableQty: [null, Validators.required],
      qty: [null, Validators.required],
      comment: [null, Validators.required],
      vehicleNo: [null, Validators.required],
      transporter: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    const customer$ = firstValueFrom(this.customerService.getValid());
    const branches$ = firstValueFrom(this.branchService.getValid());
    // const transporters$ = firstValueFrom(this.transporterService.getAll());
    customer$
      .then((res) => {
        this.customers = res;
        this.filteredCustomers = this.form.get('customer')!.valueChanges.pipe(
          startWith(''),
          map((value) =>
            typeof value === 'string'
              ? this._filterCustomer(value)
              : this.customers
          )
        );
        // return transporters$;
        return branches$;
      })
      // .then((res) => {
      //   this.transporters = res;
      //   this.filteredTransporters = this.form
      //     .get('transporter')!
      //     .valueChanges.pipe(
      //       startWith(''),
      //       map((value) =>
      //         typeof value === 'string'
      //           ? this._filterTransporters(value)
      //           : this.transporters
      //       )
      //     );
      // })
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
      })
      .catch((err) => {
        console.error('Error fetching data:', err);
      });
  }

  private _filterCustomer(value: string): any[] {
    value = value.toLowerCase();
    return this.customers.filter((branch) =>
      branch.customerName.toLowerCase().includes(value)
    );
  }

  // private _filterTransporters(value: string): any[] {
  //   const branches = this.transporters.filter((transporter: any) =>
  //     transporter.transporterName.toLowerCase().includes(value.toLowerCase())
  //   );
  //   return branches;
  // }

  private _filterBranch(value: string): any[] {
    value = value.toLowerCase();
    return this.branches.filter((branch) =>
      branch.branchName.toLowerCase().includes(value)
    );
  }

  displayFn(value: any) {
    return value ? value.branchName : '';
  }

  submit() {
    const payload = this.form.value;
    const customer = payload.customer;
    const branch = payload.branch;
    delete payload.customer;
    delete payload.branch;
    payload.customerId = customer !== null ? customer.id : null;
    payload.branchId = branch !== null ? branch.id : null;
    this.collectionService.createInventoryOut(payload).subscribe(
      (res) => {
        this.dialogRef.close('success');
      },
      (err) => {
        console.log(err);
      }
    );
  }

  cancel() {
    this.dialogRef.close();
  }

  displayCustomerFn(value: any) {
    return value ? value.customerName : '';
  }

  displayTransporterFn(value: any) {
    return value ? value.transporterName : '';
  }

  onlyNumbers(event: KeyboardEvent) {
    const key = event.key;
    if (!/^[0-9]$/.test(key)) {
      event.preventDefault();
    }
  }

  onSelect(event: MatAutocompleteSelectedEvent) {
    const selected: any = event.option.value;
    this.branchService.getQty(selected.id).subscribe(
      (res) => {
        this.form.patchValue({ availableQty: res, qty: res });
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
