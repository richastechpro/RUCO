import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrl: './entry.component.scss',
  imports: [],
})
export class EntryComponent {
  form: FormGroup;
  param: any;
  constructor(
    private fb: FormBuilder,
    private router: Router,
  ) {
    this.form = this.fb.group({
      email: [null, Validators.required],
      password: [null, Validators.required],
    });
    localStorage.clear();
  }

  fboRegister() {
    this.router.navigate(['login/']);
  }

  fboLogin() {
    this.router.navigate(['login/fbo']);
  }

  adminLogin() {
    this.router.navigate(['login/admin']);
  }
}
