import { Component, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment.development';
import { MatDialog } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FboService } from '../../../services/fbo.service';
import { FboBranchService } from '../../../services/fbo-branch.service';
import { MatRippleModule } from '@angular/material/core';
import { AlertService } from '../../../services/alert.service';
import { RateUpdateDialogComponent } from '../../../dialogs/rate-update-dialog/rate-update-dialog.component';
import { Role } from '../../../constants/Roles';

@Component({
  selector: 'app-fbo-edit',
  templateUrl: './fbo-edit.component.html',
  styleUrl: './fbo-edit.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatRippleModule,
  ],
})
export class FboEditComponent {
  form: FormGroup;
  userTypes = ['Superadmin', 'Pickupboy', 'Supervisor'];
  accTypes = ['CURRENT_ACCOUNT', 'SAVINGS_ACCOUNT'];
  id: number;
  fboBranches: any[] = [];
  isManager = signal(false);
  constructor(
    private fb: FormBuilder,
    private fboService: FboService,
    private fboBranchService: FboBranchService,
    private router: Router,
    private route: ActivatedRoute,
    private alertService: AlertService,
    private dialog: MatDialog
  ) {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.form = this.fb.group({
      id: [this.id, Validators.required],
      fboName: [null, Validators.required],

      bankName: [null, Validators.required],
      accountType: [null, Validators.required],
      accountNo: [null, Validators.required],
      accountHolderName: [null, Validators.required],
      ifscCode: [null, Validators.required],
      mobileNo: [null, Validators.required],
    });

    const user = localStorage.getItem('user');
    if (user) {
      const appUser = JSON.parse(user);
      this.isManager.set(appUser.role === Role.MANAGER);
    }
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.fboService.getById(this.id).subscribe(
      (res: any) => {
        this.form.patchValue({
          fboName: res.fboName,
          bankName: res.bankName,
          accountType: res.accountType,
          accountNo: res.accountNo,
          accountHolderName: res.accountHolderName,
          ifscCode: res.ifscCode,
          mobileNo: res.mobileNo,
        });

        this.fboBranches = res.fboBranches;
      },
      (err) => {
        console.log(err);
      }
    );
  }

  onlyNumbers(event: KeyboardEvent) {
    const key = event.key;
    if (!/^[0-9]$/.test(key)) {
      event.preventDefault();
    }
  }

  submit() {
    const payload = this.form.value;
    payload.branches = this.fboBranches;
    this.fboService.update(payload).subscribe(
      (_) => {
        this.router.navigate([
          environment.servletPath,
          environment.fbo,
          environment.list,
        ]);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  cancel() {
    this.router.navigate([
      environment.servletPath,
      environment.fbo,
      environment.list,
    ]);
  }

  addBranch() {
    this.router.navigate([
      environment.servletPath,
      environment.fbo,
      environment.branch,
      environment.create,
      this.id,
    ]);
  }

  updateRate(fboBranchId: number, index: number) {
    const fboBranch: any = this.fboBranches[index];
    const dialog = this.dialog.open(RateUpdateDialogComponent, {
      data: { rate: fboBranch.rate },
    });
    dialog.afterClosed().subscribe(
      (res) => {
        if (res) {
          const rate = res.rate;
          this.fboBranchService
            .updateRate({
              fboBranchId,
              rate,
              fboBranchName: fboBranch.fboBranchName,
            })
            .subscribe(
              (res) => {
                console.log(res);
              },
              (err) => {
                console.log(err);
              }
            );
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }

  editData(fboBranchId: number) {
    this.router.navigate([
      environment.servletPath,
      environment.fbo,
      environment.branch,
      environment.edit,
      this.id,
      fboBranchId,
    ]);
  }

  deleteData(id: number) {
    this.alertService
      .alert(
        'Warning!',
        'Do you want delete this FBO Branch?',
        'question',
        'Delete'
      )
      .then((res) => {
        if (res.isConfirmed) {
          this.fboBranchService.delete(id).subscribe(
            (res) => {
              this.loadData();
            },
            (err) => {
              console.log(err);
            }
          );
        }
      })
      .catch((err) => {
        console.log(err);
      });
  }

  downloadFormat() {
    this.fboBranchService.download().subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'fb-branch-upload-format.xlsx';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const formData = new FormData();
      formData.append('file', file);

      this.fboBranchService.upload(formData, this.id).subscribe(
        (response) => {
          this.loadData();
        },
        (error) => {
          console.error(error);
        }
      );
    }
  }
}
