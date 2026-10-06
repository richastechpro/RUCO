import { CommonModule } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { PaginatorComponent } from '../../../components/paginator/paginator.component';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { environment } from '../../../environments/environment.development';
import { Search } from '../../../interfaces/search';
import { AlertService } from '../../../services/alert.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    CommonModule,
    FormsModule,
    PaginatorComponent,
  ],
})
export class UserListComponent implements OnInit {
  items: any[] = [];
  rows: number[] = [5, 10, 15];
  limit = signal(this.rows[0]);
  offset = signal(0);
  size = signal(0);
  records = signal(0);
  searchTxt!: string;
  from = computed(() => this.offset() * this.limit() + 1);
  to = computed(() => this.offset() * this.limit() + this.limit());

  constructor(
    private router: Router,
    private userService: UserService,
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
    this.userService.get(payload).subscribe(
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
      environment.user,
      'edit',
      id,
    ]);
  }

  deleteData(id: number) {
    this.alertService
      .alert('Warning!', 'Do you want delete this User?', 'question', 'Delete')
      .then((res) => {
        if (res.isConfirmed) {
          this.userService.delete(id).subscribe(
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
      environment.user,
      environment.create,
    ]);
  }

  search() {
    this.reset();
    this.loadData();
  }
}
