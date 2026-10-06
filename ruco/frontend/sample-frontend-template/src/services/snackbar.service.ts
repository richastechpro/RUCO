import { inject, Injectable } from '@angular/core';
import {
  HorizontalPosition,
  SnackBarDuration,
  VerticalPosition,
} from '../constants/AppData';
import { SnackbarComponent } from '../components/snackbar/snackbar.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class SnackbarService {
  private _snackBar = inject(MatSnackBar);

  openSnackBar(data: { title: string; msg: string; type: string }) {
    this._snackBar.openFromComponent(SnackbarComponent, {
      duration: SnackBarDuration * 1000,
      horizontalPosition: HorizontalPosition,
      verticalPosition: VerticalPosition,
      data: data,
    });
  }
}
