import { FormsModule } from '@angular/forms';
import { MatRadioModule } from '@angular/material/radio';
import { Component } from '@angular/core';
import { MatRippleModule } from '@angular/material/core';
import { ReportService } from '../../services/report.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrl: './report.component.scss',
  imports: [MatRadioModule, FormsModule, MatRippleModule],
})
export class ReportComponent {
  from!: string;
  to!: string;
  type!: string;
  types: string[] = ['Inventory Inward', 'Inventory Outward'];

  constructor(private reportService: ReportService) {}

  submit() {
    const payload = { from: this.from, to: this.to, type: this.type };
    this.reportService.get(payload).subscribe(
      (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${this.type}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  log() {
    this.reportService.log({}).subscribe(
      (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `audit_log.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
