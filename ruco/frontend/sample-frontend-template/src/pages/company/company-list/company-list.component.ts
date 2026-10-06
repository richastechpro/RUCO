import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { PaginatorComponent } from '../../../components/paginator/paginator.component';
import { environment } from '../../../environments/environment.development';
import { UserService } from '../../../services/user.service';
import { CompanyService } from '../../../services/company.service';
import { Company } from '../../../interfaces/company';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material/dialog';
import { LocalstorageService } from '../../../services/localstorage.service';

@Component({
  selector: 'app-company-list',
  templateUrl: './company-list.component.html',
  styleUrl: './company-list.component.scss',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    CommonModule,
    FormsModule,
    PaginatorComponent,
  ],
})
export class CompanyListComponent implements OnInit {
  items: Company[] = [];
  rows: number[] = [5, 10, 15];
  limit = signal(this.rows[0]);
  offset = signal(0);
  size = signal(0);
  records = signal(0);
  searchTxt!: string;
  from = computed(() => this.offset() * this.limit() + 1);
  to = computed(() => this.offset() * this.limit() + this.limit());
  permissions = signal<string[]>([]);

  constructor(
    private router: Router,
    private companyService: CompanyService,
    private localStorageService: LocalstorageService
  ) {
    this.permissions.set(this.localStorageService.getPermissions());
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.companyService.get(this.limit(), this.offset()).subscribe(
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

  viewData(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.company,
      'edit',
      id,
    ]);
  }
  
  editData(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.company,
      'edit',
      id,
    ]);
  }

  add() {
    this.router.navigate([
      environment.servletPath,
      environment.user,
      environment.create,
    ]);
  }

  search(payload: any) {
    // this.userService.search(payload).subscribe(
    //   (res) => {
    //     this.items = res.content;
    //     this.size.set(+res.totalPages);
    //     this.records.set(+res.totalElements);
    //   },
    //   (err) => {
    //     console.log(err);
    //   }
    // );
  }

  hasPermission(permission: string): boolean {
    return this.permissions().find((p) => p === permission) !== undefined;
  }
}
