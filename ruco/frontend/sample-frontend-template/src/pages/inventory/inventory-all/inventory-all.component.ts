import { CommonModule } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { PaginatorComponent } from '../../../components/paginator/paginator.component';
import { environment } from '../../../environments/environment.development';
import { CollectionService } from '../../../services/collection.service';
import { InventoryOutComponent } from '../inventory-out/inventory-out.component';
import { InventoryService } from '../../../services/inventory.service';

@Component({
  selector: 'app-inventory-all',
  templateUrl: './inventory-all.component.html',
  styleUrl: './inventory-all.component.scss',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    CommonModule,
    FormsModule,
  ],
})
export class InventoryAllComponent implements OnInit {
  totalQty = 0;
  totalAmount = 0;
  items: any[] = [];
  searchTxt!: string;

  constructor(
    private router: Router,
    private inventoryService: InventoryService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.inventoryService
      .getInventoryAll({})
      .subscribe(
        (res) => {
          this.items = res.items;
          this.totalQty = res.totalQty;
          this.totalAmount = res.totalAmount;
        },
        (err) => {
          console.log(err);
        }
      );
  }

  search(payload: any) {}
}
