import { CommonModule } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { PaginatorComponent } from '../../../../components/paginator/paginator.component';
import { environment } from '../../../../environments/environment.development';
import { StateService } from '../../../../services/state.service';
import { CityService } from '../../../../services/city.service';
import { Dialog } from '@angular/cdk/dialog';
import { CityAddComponent } from '../city-add/city-add.component';
import { CityEditComponent } from '../city-edit/city-edit.component';
import { Search } from '../../../../interfaces/search';
import { AlertService } from '../../../../services/alert.service';

@Component({
  selector: 'app-city-list',
  templateUrl: './city-list.component.html',
  styleUrl: './city-list.component.scss',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    CommonModule,
    FormsModule,
    PaginatorComponent,
  ],
})
export class CityListComponent implements OnInit {
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
    private cityService: CityService,
    private dilaog: MatDialog,
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
    this.cityService.get(payload).subscribe(
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

  deleteData(id: number) {
    this.alertService
      .alert('Warning!', 'Do you want delete this City?', 'question', 'Delete')
      .then((res) => {
        if (res.isConfirmed) {
          this.cityService.delete(id).subscribe(
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
    const dialog = this.dilaog.open(CityAddComponent, { autoFocus: false });
    dialog.afterClosed().subscribe((res) => {
      this.loadData();
    });
  }

  editData(data: any) {
    console.log(data);
    const dialog = this.dilaog.open(CityEditComponent, {
      data: data,
      autoFocus: false,
    });
    dialog.afterClosed().subscribe((res) => {
      this.loadData();
    });
  }

  search() {
    this.reset();
    this.loadData();
  }
}
