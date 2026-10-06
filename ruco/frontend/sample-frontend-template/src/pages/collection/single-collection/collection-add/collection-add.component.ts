import { Component, Inject, OnInit } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FboService } from '../../../../services/fbo.service';
import { CollectionService } from '../../../../services/collection.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { map, Observable, startWith } from 'rxjs';
import { AsyncPipe, NgFor } from '@angular/common';
import { FboBranchService } from '../../../../services/fbo-branch.service';

@Component({
  selector: 'app-collection-add',
  templateUrl: './collection-add.component.html',
  styleUrl: './collection-add.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    FormsModule,
    AsyncPipe,
    NgFor,
  ],
})
export class CollectionAddComponent implements OnInit {
  form: FormGroup;
  fboBranches: any[] = [];
  filteredFboBranches!: Observable<any>;
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CollectionAddComponent>,
    private fboBranchService: FboBranchService,
    private collectionService: CollectionService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.form = this.fb.group({
      fboBranch: [null, Validators.required],
      requestDate: [null, Validators.required],
      qty: [null, Validators.required],
      description: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.fboBranchService.getAllValid().subscribe(
      (res) => {
        this.fboBranches = res;
        this.filteredFboBranches = this.form
          .get('fboBranch')!
          .valueChanges.pipe(
            startWith(null),
            map((value) =>
              typeof value === 'string' ? this._filter(value) : this.fboBranches
            )
          );
      },
      (err) => {
        console.log(err);
      }
    );
  }

  private _filter(value: string): any[] {
    const branches = this.fboBranches.filter((branch: any) =>
      branch.fboBranchName.toLowerCase().includes(value.toLowerCase())
    );
    return branches;
  }

  submit() {
    const payload = this.form.value;
    const fboBranch = payload.fboBranch;
    const fboBranchId = fboBranch != null ? fboBranch.id : null;
    payload.fboBranchId = fboBranchId;
    this.collectionService.createSingleCollection(payload).subscribe(
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

  displayFboBranchFn(value: any) {
    return value ? value.fboBranchName : '';
  }
}
