import { Component } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment.development';
import { Branch } from '../../../interfaces/branch';
import { BranchService } from '../../../services/branch.service';
import { UserService } from '../../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { FboService } from '../../../services/fbo.service';

@Component({
  selector: 'app-fbo-add',
  templateUrl: './fbo-add.component.html',
  styleUrl: './fbo-add.component.scss',
  imports: [FormsModule, ReactiveFormsModule],
})
export class FboAddComponent {
  form: FormGroup;
  branches: Branch[] = [];
  accTypes = ['CURRENT_ACCOUNT', 'SAVINGS_ACCOUNT'];
  constructor(
    private fb: FormBuilder,
    private fboService: FboService,
    private router: Router
  ) {
    this.form = this.fb.group({
      fboName: [null],

      bankName: [null],
      accountType: [null],
      accountNo: [null],
      accountHolderName: [null],
      ifscCode: [null],
      mobileNo: [null],
    });
  }

  onlyNumbers(event: KeyboardEvent) {
    const key = event.key;
    if (!/^[0-9]$/.test(key)) {
      event.preventDefault();
    }
  }

  submit() {
    const payload = this.form.value;
    this.fboService.create(payload).subscribe(
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
      environment.fbo,
      environment.list,
    ]);
  }
}
