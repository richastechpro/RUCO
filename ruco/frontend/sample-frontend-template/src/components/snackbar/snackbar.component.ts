import { Component, Inject, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {
  MAT_SNACK_BAR_DATA,
  MatSnackBarAction,
  MatSnackBarActions,
  MatSnackBarLabel,
  MatSnackBarRef,
} from '@angular/material/snack-bar';
import { Error, Success, Warning } from '../../constants/AppData';

@Component({
  selector: 'app-snackbar',
  templateUrl: './snackbar.component.html',
  styleUrl: './snackbar.component.scss',
  imports: [
    MatButtonModule,
    MatSnackBarLabel,
    MatSnackBarActions,
    MatSnackBarAction,
    MatIconModule,
  ],
})
export class SnackbarComponent {
  snackBarRef = inject(MatSnackBarRef);
  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: any) {
    if(data.type===Success){
      data.backgroundColor = '#04dd8a'
      data.color = '#fff'
    }
    else if(data.type===Warning){
      data.backgroundColor = '#ffb142'
      data.color = '#fff'
    }
    else if(data.type===Error){
      data.backgroundColor = '#ff5252'
      data.color = '#fff'
    }
  }
}
