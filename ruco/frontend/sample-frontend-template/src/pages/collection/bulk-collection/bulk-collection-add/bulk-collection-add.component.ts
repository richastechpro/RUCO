import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  FormArray,
  Validators,
  AbstractControl,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Observable, startWith, map } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { CollectionService } from '../../../../services/collection.service';
import { FboBranchService } from '../../../../services/fbo-branch.service';
import { BranchService } from '../../../../services/branch.service';
@Component({
  selector: 'app-bulk-collection-add',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
  ],
  templateUrl: './bulk-collection-add.component.html',
  styleUrl: './bulk-collection-add.component.scss',
})
export class BulkCollectionAddComponent implements OnInit {
  form: FormGroup;
  fboBranches: any[] = [];
  filteredFboBranches: Observable<any>[] = [];
  collectionName!: string;
  branches: any[] = [{ id: null, branchName: ' -- Select Branch -- ' }];
  isSuperAdmin = signal(false);
  selectedBranchId = signal<number | null>(null);

  constructor(
    private fb: FormBuilder,
    private collectionService: CollectionService,
    private fboBranchService: FboBranchService,
    private router: Router,
    private branchService: BranchService
  ) {
    const user = JSON.parse(localStorage.getItem('user')!);
    if (user) {
      this.isSuperAdmin.set(user.role === 'SUPERADMIN');
    }
    this.form = this.fb.group({
      items: this.fb.array([]),
    });
  }

  get items(): FormArray {
    return this.form.get('items') as FormArray;
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.collectionService.getBulkCollectionName().subscribe((res) => {
      this.collectionName = res[0];
    });

    this.branchService.getValid().subscribe(
      (res) => {
        res.forEach((r: any) => this.branches.push(r));
      },
      (err) => {
        console.log(err);
      }
    );
  }

  loadFboBranchByBranchId(branchId: number) {
    this.fboBranchService.getAllValidByBranchId(branchId).subscribe(
      (res) => {
        this.fboBranches = res;
        if (this.items.length > 0) {
          this.items.controls.forEach((control: AbstractControl) => {
            control.get('fboBranch')?.setValue(null);
          });
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }

  addItem() {
    if (this.selectedBranchId() === null) {
      alert('Please select Branch');
      return;
    }

    const index = this.items.length;
    let newItemGroup: FormGroup = this.fb.group({
      fboBranch: [null, Validators.required],
      requestDate: [null, Validators.required],
      qty: [null, Validators.required],
      description: [null, Validators.required],
    });
    this.items.push(newItemGroup);
    this.filteredFboBranches[index] = this.items
      .at(index)
      .get('fboBranch')!
      .valueChanges.pipe(
        startWith(''),
        map((value) =>
          typeof value === 'string' ? this._filter(value) : this.fboBranches
        )
      );
  }

  private _filter(value: string): any[] {
    return this.fboBranches.filter((branch: any) =>
      branch.fboBranchName.toLowerCase().includes(value.toLowerCase())
    );
  }

  removeItem(index: number) {
    if (this.items.length > 1) {
      this.items.removeAt(index);
    }
  }

  submit() {
    const payload: any[] = this.items.value;
    payload.forEach((p) => {
      const fboBranch = p.fboBranch;
      const fboBranchId = fboBranch != null ? fboBranch.id : null;
      p.fboBranchId = fboBranchId;
    });
    const data = {
      collectionName: this.collectionName,
      items: payload,
      branchId: this.selectedBranchId(),
    };
    this.collectionService.createBulkCollection(data).subscribe(
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
      environment.collection,
      environment.list,
    ]);
  }

  displayFboBranchFn(value: any) {
    return value ? value.fboBranchName : '';
  }

  onSelectRow(event: any) {
    const selectedBranchId = event.target.value;
    this.selectedBranchId.set(selectedBranchId);
    this.loadFboBranchByBranchId(selectedBranchId);
  }
}
