import { CommonModule } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { PaginatorComponent } from '../../../components/paginator/paginator.component';
import { environment } from '../../../environments/environment.development';
import { UserService } from '../../../services/user.service';
import { CollectionAddComponent } from '../single-collection/collection-add/collection-add.component';
import { CollectionService } from '../../../services/collection.service';
import { BranchService } from '../../../services/branch.service';
import { CollectionEditComponent } from '../single-collection/collection-edit/collection-edit.component';
import { CreateAssignComponent } from '../dialog/create-assign/create-assign.component';
import { CreateCollectComponent } from '../dialog/create-collect/create-collect.component';
import { InventoryInComponent } from '../dialog/inventory-in/inventory-in.component';
import { InventoryOutComponent } from '../dialog/inventory-out/inventory-out.component';
import { Search } from '../../../interfaces/search';
import { AlertService } from '../../../services/alert.service';

@Component({
  selector: 'app-collection-list',
  templateUrl: './collection-list.component.html',
  styleUrl: './collection-list.component.scss',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    CommonModule,
    FormsModule,
    PaginatorComponent,
  ],
})
export class CollectionListComponent implements OnInit {
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
    private collectionService: CollectionService,
    private dialog: MatDialog,
    private branchService: BranchService,
    private alertService: AlertService
  ) {
    const months = [
      'Dec',
      'Nov',
      'Oct',
      'Sep',
      'Aug',
      'Jul',
      'Jun',
      'May',
      'Apr',
      'Mar',
      'Feb',
      'Jan',
    ];

    for (let year = 2030; year >= 2018; year--) {
      for (let monthIndex = 0; monthIndex < 12; monthIndex++) {
        this.dates.push(`${months[monthIndex]} ${year}`);
      }
    }

    const role = JSON.parse(localStorage.getItem('user') || '{}')?.role;
    if (role) {
      this.role.set(role);
    }
  }

  ngOnInit(): void {
    this.loadData();
    this.branchService.getValid().subscribe(
      (res) => {
        res.forEach((r: any) => this.branches.push(r));
      },
      (err) => {
        console.log(err);
      }
    );
  }

  loadData() {
    const payload: Search = {
      limit: this.limit(),
      offset: this.offset(),
      searchTxt: this.searchTxt,
    };
    this.collectionService.get(payload).subscribe(
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

  editData(id: number, type: string) {
    this.collectionService.getById(id).subscribe(
      (res) => {
        if (type === 'SINGLE') {
          const dialog = this.dialog.open(CollectionEditComponent, {
            data: { data: res, collectionId: id },
            autoFocus: false,
          });
          dialog.afterClosed().subscribe(
            (res) => {
              if (res === 'success') {
                this.loadData();
              }
            },
            (err) => {
              console.log(err);
            }
          );
        } else if (type === 'BULK') {
          this.router.navigate([
            environment.servletPath,
            environment.collection,
            environment.bulk,
            id,
          ]);
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }

  deleteData(id: number) {
    this.alertService
      .alert(
        'Warning!',
        'Do you want delete this Collection?',
        'question',
        'Delete'
      )
      .then((res) => {
        if (res.isConfirmed) {
          this.collectionService.delete(id).subscribe(
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

  addBulkCollection() {
    this.router.navigate([
      environment.servletPath,
      environment.collection,
      environment.bulk,
    ]);
  }

  addSingleCollection() {
    const dialog = this.dialog.open(CollectionAddComponent, {
      data: { type: 'single' },
      autoFocus: false,
    });
    dialog.afterClosed().subscribe(
      (res) => {
        if (res === 'success') {
          this.loadData();
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }

  assign(id: number) {
    const dialogRef = this.dialog.open(CreateAssignComponent, {
      data: { collectionId: id },
      autoFocus: false,
    });
    dialogRef.afterClosed().subscribe(
      (res) => {
        if (res === 'success') {
          this.loadData();
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }

  collect(id: number, type: string, requestId: number, qty: number) {
    if (type === 'BULK') {
      this.router.navigate([
        environment.servletPath,
        environment.collection,
        environment.bulk,
        'collect',
        id,
      ]);
      return;
    } else {
      const dialogRef = this.dialog.open(CreateCollectComponent, {
        data: { collectionId: id, requestId: requestId, type: 'SINGLE', qty },
        autoFocus: false,
      });
      dialogRef.afterClosed().subscribe(
        (res) => {
          if (res === 'success') {
            this.loadData();
          }
        },
        (err) => {
          console.log(err);
        }
      );
    }
  }

  viewCertificate(id: number) {
    this.router.navigate([environment.servletPath, 'certificate', id]);
  }

  createInventoryIn(
    id: number,
    branchName: string,
    branchId: number,
    warehouse: string,
    details: any[]
  ) {
    const dialog = this.dialog.open(InventoryInComponent, {
      data: {
        collectionId: id,
        branchName,
        branchId,
        warehouse,
        details,
      },
      autoFocus: false,
    });
    dialog.afterClosed().subscribe((res) => {
      this.loadData();
    });
  }

  createInventoryOut(id: number) {
    const dialog = this.dialog.open(InventoryOutComponent, {
      data: { collectionId: id },
      autoFocus: false,
    });
    dialog.afterClosed().subscribe((res) => {
      this.loadData();
    });
  }
}
