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
import { CollectionService } from '../../../../services/collection.service';
import { FboService } from '../../../../services/fbo.service';

@Component({
  selector: 'app-collection-edit',
  templateUrl: './collection-edit.component.html',
  styleUrl: './collection-edit.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    FormsModule,
    AsyncPipe,
    NgFor,
  ],
})
export class CollectionEditComponent implements OnInit {
  form: FormGroup;
  fboBranches: any[] = [];
  filteredfboBranches!: Observable<any>;
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CollectionEditComponent>,
    private fboService: FboService,
    private collectionService: CollectionService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.form = this.fb.group({
      id: [null, Validators.required],
      branch: [null, Validators.required],
      requestDate: [null, Validators.required],
      qtyAvailable: [null, Validators.required],
      descr: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    // this.fboService.getBranches().subscribe(
    //   (res) => {
    //     this.fboBranches = res;
    //     const formData = this.data.data;
    //     this.form.patchValue({
    //       id: formData.id,
    //       branch: formData.branch,
    //       requestDate: formData.requestDate,
    //       qtyAvailable: formData.qtyAvailable,
    //       descr: formData.descr,
    //     });
    //     this.filteredfboBranches = this.form.get('branch')!.valueChanges.pipe(
    //       startWith(this.data.data.branch || ''),
    //       map((value) =>
    //         typeof value === 'string' ? this._filter(value) : this.fboBranches
    //       )
    //     );
    //   },
    //   (err) => {
    //     console.log(err);
    //   }
    // );
  }

  private _filter(value: string): any[] {
    const branches = this.fboBranches.filter((branch: any) =>
      branch.fboBranchName.toLowerCase().includes(value.toLowerCase())
    );
    return branches;
  }

  submit() {
    const payload = this.form.value;
    this.collectionService
      .updateSingleCollection(payload, this.data.collectionId)
      .subscribe(
        (res) => {
          console.log(res);
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
    return value ? value.fboBranchName : '';
  }
}
