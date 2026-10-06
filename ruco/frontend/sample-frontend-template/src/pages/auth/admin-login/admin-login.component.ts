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
import { AuthService } from '../../../services/auth.service';
import { SnackbarService } from '../../../services/snackbar.service';
import { Error, Success } from '../../../constants/AppData';

@Component({
  selector: 'app-admin-login',
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.scss',
  imports: [FormsModule, ReactiveFormsModule],
})
export class AdminLoginComponent {
  form: FormGroup;
  param: any;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackbarService: SnackbarService
  ) {
    localStorage.clear();
    this.form = this.fb.group({
      email: ['superadmin', Validators.required],
      password: ['1', Validators.required],
    });
  }

  submit() {
    const payload = {
      email: this.form.value.email + 'admin',
      password: this.form.value.password,
    };

    this.authService.login(payload).subscribe(
      (res) => {
        localStorage.setItem('user', JSON.stringify(res));
        this.router.navigate([environment.servletPath, environment.dashboard]);
      },
      (err) => {
        console.log(err);
        this.snackbarService.openSnackBar({
          title: 'Error',
          msg: err.error,
          type: Error,
        });
      }
    );
  }
}
