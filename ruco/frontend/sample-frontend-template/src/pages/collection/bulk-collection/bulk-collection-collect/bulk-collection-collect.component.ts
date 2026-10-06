import { CommonModule, NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
  FormArray,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable, startWith, map } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { CollectionService } from '../../../../services/collection.service';
import { FboService } from '../../../../services/fbo.service';
import { MatDialog } from '@angular/material/dialog';
import { CreateCollectComponent } from '../../dialog/create-collect/create-collect.component';

@Component({
  selector: 'app-bulk-collection-collect',
  templateUrl: './bulk-collection-collect.component.html',
  styleUrl: './bulk-collection-collect.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    NgFor,
  ],
})
export class BulkCollectionCollectComponent implements OnInit {
  form: FormGroup;
  collectionName!: string;
  id: number;
  constructor(
    private fb: FormBuilder,
    private collectionService: CollectionService,
    private router: Router,
    private route: ActivatedRoute,
    private dialog: MatDialog
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
      this.collectionName = res.collectionName;
      res.items.forEach((request: any) => {
        let newItemGroup: FormGroup = this.fb.group({
          id: [request.id, Validators.required],
          branch: [request.fboBranchName, Validators.required],
          requestDate: [request.date, Validators.required],
          qtyAvailable: [request.qty, Validators.required],
          descr: [request.description, Validators.required],
          collectedStatus: [request.collectedStatus, Validators.required],
        });
        this.items.push(newItemGroup);
      });
    });
  }

  get items(): FormArray {
    return this.form.get('items') as FormArray;
  }

  collect(i: number) {
    const requestId = +this.items.at(i).get('id')!.value;
    const qty = +this.items.at(i).get('qtyAvailable')!.value;
    const dialogRef = this.dialog.open(CreateCollectComponent, {
      data: { requestId: requestId, type: 'BULK', qty },
      autoFocus: false,
    });
    dialogRef.afterClosed().subscribe(
      (res) => {
        if (res === 'success') {
          this.items.clear();
          this.loadData();
        }
      },
      (err) => {
        console.log(err);
      }
    );
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
