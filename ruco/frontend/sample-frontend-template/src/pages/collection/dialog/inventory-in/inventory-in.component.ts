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
import { Observable, firstValueFrom, startWith, map } from 'rxjs';
import { CollectionService } from '../../../../services/collection.service';
import { TransporterService } from '../../../../services/transporter.service';
import { UserService } from '../../../../services/user.service';
import { CollectionAddComponent } from '../../single-collection/collection-add/collection-add.component';
import { BranchService } from '../../../../services/branch.service';
import { MatIcon, MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-inventory-in',
  templateUrl: './inventory-in.component.html',
  styleUrl: './inventory-in.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    FormsModule,
    MatIconModule,
  ],
})
export class InventoryInComponent implements OnInit {
  form: FormGroup;
  branches: any[] = [];
  filteredBranches!: Observable<any>;
  collectionId: number;
  details: any[] = [];
  inventoryInQty: number = 0;
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<InventoryInComponent>,
    private collectionService: CollectionService,
    private branchService: BranchService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.collectionId = data.collectionId;
    this.details = data.details || [];
    this.details.forEach((detail: any) => {
      this.inventoryInQty += !detail.inventoryIn ? detail.collectedQty || 0 : 0;
    });
    this.form = this.fb.group({
      collectionId: [this.data.collectionId, Validators.required],
      branchName: [this.data.branchName, Validators.required],
      branchId: [this.data.branchId, Validators.required],
      warehouse: [this.data.warehouse, Validators.required],
      inventoryInQty: [this.inventoryInQty, Validators.required],
    });
  }

  ngOnInit(): void {
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

  onSelectBranch(event: MatAutocompleteSelectedEvent) {
    this.form.patchValue({ warehouse: null });
    const selected: any = event.option.value;
    this.form.patchValue({ warehouse: selected.warehouse });
  }

  submit() {
    this.collectionService.createInventoryIn(this.collectionId).subscribe(
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

  displayBranchFn(value: any) {
    return value ? value.branchName : '';
  }

  onlyNumbers(event: KeyboardEvent) {
    const key = event.key;
    if (!/^[0-9]$/.test(key)) {
      event.preventDefault();
    }
  }
}
