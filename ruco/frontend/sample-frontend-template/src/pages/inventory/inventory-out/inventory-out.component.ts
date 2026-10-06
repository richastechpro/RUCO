import { CommonModule } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { PaginatorComponent } from '../../../components/paginator/paginator.component';
import { InventoryOutComponent as InventoryOutAddComponentv } from '../../../pages/collection/dialog/inventory-out/inventory-out.component';
import { environment } from '../../../environments/environment.development';
import { CollectionService } from '../../../services/collection.service';

@Component({
  selector: 'app-inventory-out',
  templateUrl: './inventory-out.component.html',
  styleUrl: './inventory-out.component.scss',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    CommonModule,
    FormsModule,
    PaginatorComponent,
  ],
})
export class InventoryOutComponent implements OnInit {
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
    private collectionService: CollectionService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.collectionService
      .getInventotyOut(this.limit(), this.offset())
      .subscribe(
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
      environment.inventory,
      'edit',
      id,
    ]);
  }

  deleteData(id: number) {
    this.collectionService.deleteInventoryOut(id).subscribe(
      (res) => {
        this.reset();
        this.loadData();
      },
      (err) => {
        console.log(err);
      }
    );
  }

  add() {
    const dialog = this.dialog.open(InventoryOutAddComponentv, {
      autoFocus: false,
    });
    dialog.afterClosed().subscribe((res) => {
      this.loadData();
    });
  }

  search(payload: any) {}
}
