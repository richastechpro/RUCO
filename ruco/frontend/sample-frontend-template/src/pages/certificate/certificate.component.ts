import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { CollectionService } from '../../services/collection.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-certificate',
  imports: [],
  templateUrl: './certificate.component.html',
  styleUrl: './certificate.component.scss',
})
export class CertificateComponent implements OnInit {
  certificateDetails = {
    fbo_name: 'Example Company',
    certificate_no: '12345',
    collected_on: '2024-04-01',
    invoice_no: 'INV-9876',
    collected_qty: '500',
  };
  id: number;

  constructor(
    private collectionService: CollectionService,
    private route: ActivatedRoute
  ) {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.collectionService.getCertificateDetails(this.id).subscribe(
      (data) => {
        this.certificateDetails = data;
      },
      (err) => {
        console.error('Error fetching certificate details:', err);
      }
    );
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {}

  goBack(): void {
    window.history.back();
  }

  printContent(el: string): void {
    const printContent = document.getElementById(el);
    const printWindow = window.open('', '', 'width=800,height=600');

    if (printContent && printWindow) {
      printWindow.document.write(
        `<html><head><title>Print</title></head><body>${printContent.innerHTML}</body></html>`
      );
      printWindow.document.close();
      printWindow.focus();
      setTimeout(() => {
        printWindow.print();
        printWindow.close();
      }, 250);
    }
  }
}
