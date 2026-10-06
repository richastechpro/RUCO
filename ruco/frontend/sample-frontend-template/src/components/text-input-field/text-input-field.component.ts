import { Component, input } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-text-input-field',
  imports: [FormsModule,ReactiveFormsModule],
  templateUrl: './text-input-field.component.html',
  styleUrl: './text-input-field.component.scss',
})
export class TextInputFieldComponent {
  formControl = input.required<FormControl>();
  label = input.required<string>();
  required = input.required<boolean>();
}
