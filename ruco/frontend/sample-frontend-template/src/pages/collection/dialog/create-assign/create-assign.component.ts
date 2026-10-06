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
import { Observable, startWith, map, first, firstValueFrom } from 'rxjs';
import { CollectionService } from '../../../../services/collection.service';
import { FboService } from '../../../../services/fbo.service';
import { CollectionAddComponent } from '../../single-collection/collection-add/collection-add.component';
import { FboBranchLoginService } from '../../../../services/fbo-branch-login.service';
import { TransporterService } from '../../../../services/transporter.service';
import { UserService } from '../../../../services/user.service';

@Component({
  selector: 'app-create-assign',
  templateUrl: './create-assign.component.html',
  styleUrl: './create-assign.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    FormsModule,
    AsyncPipe,
    NgFor,
  ],
})
export class CreateAssignComponent implements OnInit {
  form: FormGroup;
  pickupBoys: any[] = [];
  transporters: any[] = [];
  filteredPickupBoys1!: Observable<any>;
  filteredPickupBoys2!: Observable<any>;
  filteredTransporters!: Observable<any>;
  collectionId: number;
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CollectionAddComponent>,
    private collectionService: CollectionService,
    private transporterService: TransporterService,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.collectionId = data.collectionId;
    this.form = this.fb.group({
      pickupBoy1: [null, Validators.required],
      pickupBoy2: [null, Validators.required],
      transporter: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    const pickupboys$ = firstValueFrom(
      this.userService.getPickupBoys(this.collectionId)
    );
    const transporters$ = firstValueFrom(this.transporterService.getAll(this.collectionId));
    pickupboys$
      .then((res) => {
        this.pickupBoys = res;
        this.filteredPickupBoys1 = this.form
          .get('pickupBoy1')!
          .valueChanges.pipe(
            startWith(''),
            map((value) =>
              typeof value === 'string'
                ? this._filterPickupBoys(value)
                : this.pickupBoys
            )
          );
        this.filteredPickupBoys2 = this.form
          .get('pickupBoy2')!
          .valueChanges.pipe(
            startWith(''),
            map((value) =>
              typeof value === 'string'
                ? this._filterPickupBoys(value)
                : this.pickupBoys
            )
          );
        return transporters$;
      })
      .then((res) => {
        this.transporters = res;
        this.filteredTransporters = this.form
          .get('transporter')!
          .valueChanges.pipe(
            startWith(''),
            map((value) =>
              typeof value === 'string'
                ? this._filterTransporters(value)
                : this.transporters
            )
          );
      })
      .catch((err) => {
        console.error('Error fetching data:', err);
      });
  }

  private _filterPickupBoys(value: string): any[] {
    const users = this.pickupBoys.filter((user: any) =>
      user.username.toLowerCase().includes(value.toLowerCase())
    );
    return users;
  }

  private _filterTransporters(value: string): any[] {
    const branches = this.transporters.filter((transporter: any) =>
      transporter.transporterName.toLowerCase().includes(value.toLowerCase())
    );
    return branches;
  }

  submit() {
    const payload = this.form.value;
    payload.collectionId = this.collectionId;
    const user1 = payload.pickupBoy1;
    const user2 = payload.pickupBoy2;
    const transporter = payload.transporter;
    delete payload.user1;
    delete payload.user2;
    delete payload.transporter;
    payload.user1 = user1 !== null ? user1.id : null;
    payload.user2 = user2 !== null ? user2.id : null;
    payload.transporter = transporter !== null ? transporter.id : null;
    console.log(payload);
    this.collectionService.updateAssign(payload).subscribe(
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

  displayUserFn(value: any) {
    return value ? value.username : '';
  }

  displayTransporterFn(value: any) {
    return value ? value.transporterName : '';
  }
}
