import { CommonModule } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { PaginatorComponent } from '../../../components/paginator/paginator.component';
import { environment } from '../../../environments/environment.development';
import { FboService } from '../../../services/fbo.service';
import { Search } from '../../../interfaces/search';
import { AlertService } from '../../../services/alert.service';

@Component({
  selector: 'app-fbo-list',
  templateUrl: './fbo-list.component.html',
  styleUrl: './fbo-list.component.scss',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    CommonModule,
    FormsModule,
    PaginatorComponent,
  ],
})
export class FboListComponent implements OnInit {
  items: any[] = [];
  rows: number[] = [5, 10, 15];
  limit = signal(this.rows[0]);
  offset = signal(0);
  // total no of pages available for selected limit, offset
  size = signal(0);
  // total no of records available in the db for selected module
  records = signal(0);
  searchTxt!: string;
  // to show in pagination --> Showing {from} to {to} of {records} entries
  from = computed(() => this.offset() * this.limit() + 1);
  to = computed(() => this.offset() * this.limit() + this.limit());

  constructor(
    private router: Router,
    private fboService: FboService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  getData() {
    this.items = JSON.parse(localStorage.getItem('fbos')!);
    if (this.items && this.items.length) {
      this.size.set(this.items.length / this.limit());
      this.records.set(this.items.length);
    }
  }

  loadData() {
    const payload: Search = {
      limit: this.limit(),
      offset: this.offset(),
      searchTxt: this.searchTxt,
    };
    this.fboService.get(payload).subscribe(
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

  editData(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.fbo,
      'edit',
      id,
    ]);
  }

  deleteData(id: number) {
    this.alertService
      .alert('Warning!', 'Do you want delete this FBO?', 'question', 'Delete')
      .then((res) => {
        if (res.isConfirmed) {
          this.fboService.delete(id).subscribe(
            (res) => {
              console.log(res);
              this.reset();
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

  add() {
    this.router.navigate([
      environment.servletPath,
      environment.fbo,
      environment.create,
    ]);
  }

  search() {
    this.reset();
    this.loadData();
  }

  createLogin(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.fbo,
      environment.create,
      'login',
      id,
    ]);
  }
}
