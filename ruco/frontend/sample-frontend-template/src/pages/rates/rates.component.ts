import { CommonModule } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { PaginatorComponent } from '../../components/paginator/paginator.component';
import { RaiseTicketDialogComponent } from '../../dialogs/raise-ticket-dialog/raise-ticket-dialog.component';
import { environment } from '../../environments/environment.development';
import { Search } from '../../interfaces/search';
import { AlertService } from '../../services/alert.service';
import { TicketService } from '../../services/ticket.service';
import { RateRequestService } from '../../services/rate-request.service';
import { RatesService } from '../../services/rates.service';

@Component({
  selector: 'app-rates',
  templateUrl: './rates.component.html',
  styleUrl: './rates.component.scss',
  imports: [
      MatButtonModule,
      MatIconModule,
      MatDialogModule,
      CommonModule,
      FormsModule,
      PaginatorComponent,],
})
export class RatesComponent implements OnInit {
  items: any[] = [];
  rows: number[] = [5, 10, 15];
  limit = signal(this.rows[0]);
  offset = signal(0);
  size = signal(0);
  records = signal(0);
  searchTxt!: string;
  from = computed(() => this.offset() * this.limit() + 1);
  to = computed(() => this.offset() * this.limit() + this.limit());
  branches: any[] = [{ id: null, branchName: 'All' }];
  dates: any[] = ['All'];
  role = signal<string | null>(null);

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private service: RatesService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    const payload: Search = {
      limit: this.limit(),
      offset: this.offset(),
      searchTxt: this.searchTxt,
    };
    this.service.get(payload).subscribe(
      (res) => {
        this.items = res.content;
        this.size.set(+res.totalPages);
        this.records.set(+res.totalElements);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  onSelectRow(event: any) {
    this.reset();
    this.limit.set(+event.target.value);
    this.loadData();
  }

  reset() {
    this.items = [];
    this.limit.set(this.rows[0]);
    this.offset.set(0);
    this.size.set(0);
    this.records.set(0);
  }

  navigate(targetPage: any) {
    this.offset.set(targetPage - 1);
    this.loadData();
  }

  view(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.ticket,
      environment.edit,
      id,
    ]);
  }

  search() {
    this.reset();
    this.loadData();
  }
}

