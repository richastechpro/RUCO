import { AsyncPipe, NgFor } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, firstValueFrom, startWith, map } from 'rxjs';
import { CollectionService } from '../../../../services/collection.service';
import { FboBranchLoginService } from '../../../../services/fbo-branch-login.service';
import { FboService } from '../../../../services/fbo.service';
import { TransporterService } from '../../../../services/transporter.service';
import { CollectionAddComponent } from '../../single-collection/collection-add/collection-add.component';

@Component({
  selector: 'app-create-collect',
  templateUrl: './create-collect.component.html',
  styleUrl: './create-collect.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    FormsModule,
  ],
})
export class CreateCollectComponent implements OnInit {
  form: FormGroup;
  collectionId: number;
  requestId: number;
  qty: number;
  type!: string;
  files!: FileList;
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CreateCollectComponent>,
    private collectionService: CollectionService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.collectionId = data.collectionId;
    this.requestId = data.requestId;
    this.type = data.type;
    this.qty = data.qty;
    const date = new Date();
    const day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
    const curDate = `${date.getFullYear()}-${date.getMonth() + 1}-${day}`;
    this.form = this.fb.group({
      collectionId: [this.collectionId],
      requestId: [this.requestId],
      type: [this.type],
      assignedQty: [this.qty, Validators.required],
      collectedQty: [this.qty, Validators.required],
      collectedOn: [curDate, Validators.required],
      invoiceNo: [null, Validators.required],
      descr: [null, Validators.required],
    });
  }

  ngOnInit(): void {}

  submit() {
    const formData = new FormData();
    if (this.files && this.files.length) {
      for (let i = 0; i < this.files.length; i++) {
        formData.append('files', this.files[i]);
      }
    }
    const payload = this.form.value;
    if (this.requestId) {
      formData.append('requestId', this.requestId.toString());
    }
    if (this.collectionId) {
      formData.append(
        'collectionId',
        this.collectionId !== null ? this.collectionId.toString() : ''
      );
    }
    formData.append('type', this.type.toString());
    formData.append('collectedQty', payload.collectedQty.toString());
    formData.append('collectedOn', payload.collectedOn.toString());
    formData.append('invoiceNo', payload.invoiceNo.toString());
    formData.append('descr', payload.descr.toString());
    this.collectionService.upload(formData).subscribe(
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
    return value ? value.userName : '';
  }

  displayTransporterFn(value: any) {
    return value ? value.transporterName : '';
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    const files: FileList = input.files;
    this.files = files;
  }
}
