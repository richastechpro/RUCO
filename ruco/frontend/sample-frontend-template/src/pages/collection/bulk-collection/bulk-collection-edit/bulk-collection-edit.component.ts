import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  FormArray,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, startWith, map } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { CollectionService } from '../../../../services/collection.service';
import { FboService } from '../../../../services/fbo.service';

@Component({
  selector: 'app-bulk-collection-edit',
  templateUrl: './bulk-collection-edit.component.html',
  styleUrl: './bulk-collection-edit.component.scss',
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
})
export class BulkCollectionEditComponent implements OnInit {
  form: FormGroup;
  branches: any[] = [];
  filteredBranches: Observable<any>[] = [];
  collectionName!: string;
  id: number;
  constructor(
    private fb: FormBuilder,
    private fboService: FboService,
    private collectionService: CollectionService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.form = this.fb.group({
      items: this.fb.array([]),
    });
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.collectionService.getBulkById(this.id).subscribe((res: any) => {
      this.collectionName = res.key;
      res.value.forEach((request: any) => {
        let newItemGroup: FormGroup = this.fb.group({
          id: [request.id, Validators.required],
          branch: [request.branch, Validators.required],
          requestDate: [request.requestDate, Validators.required],
          qtyAvailable: [request.qtyAvailable, Validators.required],
          descr: [request.descr, Validators.required],
        });
        this.items.push(newItemGroup);
        this.filteredBranches[this.items.length - 1] = this.items
          .at(this.items.length - 1)
          .get('branch')!
          .valueChanges.pipe(
            startWith(request.branch || ''),
            map((value) =>
              typeof value === 'string' ? this._filter(value) : this.branches
            )
          );
      });
    });

    // this.fboService.getBranches().subscribe(
    //   (res) => {
    //     this.branches = res;
    //   },
    //   (err) => {
    //     console.log(err);
    //   }
    // );
  }

  get items(): FormArray {
    return this.form.get('items') as FormArray;
  }

  addItem() {
    const index = this.items.length;
    let newItemGroup: FormGroup = this.fb.group({
      id: [null, Validators.required],
      branch: [null, Validators.required],
      requestDate: [null, Validators.required],
      qtyAvailable: [null, Validators.required],
      descr: [null, Validators.required],
    });
    this.items.push(newItemGroup);
    this.filteredBranches[index] = this.items
      .at(index)
      .get('branch')!
      .valueChanges.pipe(
        startWith(''),
        map((value) =>
          typeof value === 'string' ? this._filter(value) : this.branches
        )
      );
  }

  private _filter(value: string): any[] {
    return this.branches.filter((branch: any) =>
      branch.fboBranchName.toLowerCase().includes(value.toLowerCase())
    );
  }

  removeItem(index: number) {
    if (this.items.length > 1) {
      const data = this.items.at(index).value;
      if (data.id) {
        this.collectionService.deleteRequest(data.id).subscribe(
          (res) => {
            console.log(res);
          },
          (err) => {
            console.log(err);
          }
        );
      }
      this.items.removeAt(index);
    }
  }

  submit() {
    const payload = this.items.value;
    const data = {
      key: this.collectionName,
      value: payload,
    };
    this.collectionService.updateBulkCollection(data, this.id).subscribe(
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

  displayBranchFn(value: any) {
    return value ? value.fboBranchName : '';
  }
}
