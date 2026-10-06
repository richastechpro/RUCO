import { CommonModule } from '@angular/common';
import { Component, computed, input, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { PaginatorComponent } from '../paginator/paginator.component';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment.development';
import { UserService } from '../../services/user.service';
import { ItemService } from '../../services/item.service';
import { ItemCategoryService } from '../../services/item-category.service';
import { GstService } from '../../services/gst.service';
import { CourierService } from '../../services/courier.service';
import {
  brandMaster,
  courierMaster,
  customerItemMaster,
  customerMaster,
  gstMaster,
  itemCategoryMaster,
  itemMaster,
  orderMaster,
  userMaster,
} from '../../constants/Module';
import { CreateItemCategoryMasterComponent } from '../../pages/master/aos-user/item-category-master/create-item-category-master/create-item-category-master.component';
import { EditItemCategoryMasterComponent } from '../../pages/master/aos-user/item-category-master/edit-item-category-master/edit-item-category-master.component';
import { CreateBrandMasterComponent } from '../../pages/master/aos-user/brand-master/create-brand-master/create-brand-master.component';
import { EditBrandMasterComponent } from '../../pages/master/aos-user/brand-master/edit-brand-master/edit-brand-master.component';
import { BrandService } from '../../services/brand.service';
import { ImageComponent } from '../image/image.component';
import { CustomerService } from '../../services/customer.service';
import { CustomerItemService } from '../../services/customer-item.service';
import { OrderService } from '../../services/order.service';
import { AlertService } from '../../services/alert.service';
import { Success, Warning } from '../../constants/AppData';
import { SnackbarService } from '../../services/snackbar.service';
import { PaginationDataService } from '../../services/pagination-data.service';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrl: './table.component.scss',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    CommonModule,
    FormsModule,
    PaginatorComponent,
  ],
})
export class TableComponent implements OnInit {
  items: any[] = [];
  rows: number[] = [10, 20, 30];
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
  headers = input.required<string[]>();
  addBtnTxt = input.required<string>();
  module = input.required<string[]>();
  curModule = computed(() => this.module()[this.module().length - 1]);
  selectedCustomerId = signal<null | number>(null);
  customers: any[] = [{ field: null, value: '-- select customer --' }];

  userMaster = signal(userMaster);
  itemMaster = signal(itemMaster);
  itemCategoryMaster = signal(itemCategoryMaster);
  gstMaster = signal(gstMaster);
  courierMaster = signal(courierMaster);
  brandMaster = signal(brandMaster);
  customerMaster = signal(customerMaster);
  customerItemMaster = signal(customerItemMaster);
  orderMaster = signal(orderMaster);

  constructor(
    private router: Router,
    private userService: UserService,
    private itemService: ItemService,
    private itemCategoryService: ItemCategoryService,
    private gstService: GstService,
    private courierService: CourierService,
    private brandService: BrandService,
    private customerService: CustomerService,
    private customerItemService: CustomerItemService,
    private orderService: OrderService,
    private dialog: MatDialog,
    private alertService: AlertService,
    private snackBarService: SnackbarService,
    private paginationDataService: PaginationDataService
  ) {}

  reset() {
    this.items = [];
    this.limit.set(this.rows[0]);
    this.offset.set(0);
    this.size.set(0);
    this.records.set(0);
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    if (
      this.paginationDataService.get('curModule') !== null &&
      this.paginationDataService.get('curModule') === this.curModule()
    ) {
      if (!this.paginationDataService.exist('limit'))
        this.paginationDataService.put({ key: 'limit', value: this.limit() });
      else this.limit.set(+this.paginationDataService.get('limit')!);
      if (!this.paginationDataService.exist('offset'))
        this.paginationDataService.put({ key: 'offset', value: this.offset() });
      else this.offset.set(+this.paginationDataService.get('offset')!);
    } else {
      this.paginationDataService.clearAll();
      this.paginationDataService.put({ key: 'limit', value: this.limit() });
      this.paginationDataService.put({ key: 'offset', value: this.offset() });
      this.paginationDataService.put({
        key: 'curModule',
        value: this.curModule(),
      });
    }
    if (this.curModule() === userMaster) {
      this.loadUser();
    } else if (this.curModule() === itemMaster) {
      this.loadItem();
    } else if (this.curModule() === itemCategoryMaster) {
      this.loadItemCategory();
    } else if (this.curModule() === gstMaster) {
      this.loadGst();
    } else if (this.curModule() === courierMaster) {
      this.loadCourier();
    } else if (this.curModule() === brandMaster) {
      this.loadBrand();
    } else if (this.curModule() === customerMaster) {
      this.loadCustomer();
    } else if (this.curModule() === customerItemMaster) {
      this.loadCustomerItem();
    } else if (this.curModule() === orderMaster) {
      this.loadCustomers();
    }
  }

  loadCustomers() {
    this.customerService.getValidCustomers().subscribe(
      (res) => {
        res.forEach((r) => this.customers.push(r));
      },
      (err) => {
        console.log(err);
      }
    );
  }

  navigateToCreateData() {
    if (this.curModule() === userMaster) {
      this.navigateToCreateUser();
    } else if (this.curModule() === itemMaster) {
      this.navigateToCreateItem();
    } else if (this.curModule() === itemCategoryMaster) {
      this.openDialog(null, 'item-category');
    } else if (this.curModule() === gstMaster) {
      this.navigateToCreateGst();
    } else if (this.curModule() === courierMaster) {
      this.navigateToCreateCourier();
    } else if (this.curModule() === brandMaster) {
      this.openDialog(null, 'brand');
    } else if (this.curModule() === customerMaster) {
      this.navigateToCreateCustomer();
    } else if (this.curModule() === customerItemMaster) {
      this.navigateToCreateCustomerItem();
    }
  }

  editData(id: number) {
    if (this.curModule() === userMaster) {
      this.editUser(id);
    } else if (this.curModule() === itemMaster) {
      this.editItem(id);
    } else if (this.curModule() === itemCategoryMaster) {
      this.openDialog(id, 'item-category');
    } else if (this.curModule() === gstMaster) {
      this.editGst(id);
    } else if (this.curModule() === courierMaster) {
      this.editCourier(id);
    } else if (this.curModule() === brandMaster) {
      this.openDialog(id, 'brand');
    } else if (this.curModule() === customerMaster) {
      this.editCustomer(id);
    }
  }

  deleteData(id: number) {
    if (this.curModule() === userMaster) {
      this.deleteUser(id);
    } else if (this.curModule() === brandMaster) {
      this.deleteBrand(id);
    } else if (this.curModule() === itemMaster) {
      this.deleteItem(id);
    } else if (this.curModule() === itemCategoryMaster) {
      this.deleteItemCategory(id);
    } else if (this.curModule() === gstMaster) {
      this.deleteGst(id);
    } else if (this.curModule() === courierMaster) {
      this.deleteCourier(id);
    } else if (this.curModule() === customerMaster) {
      this.deleteCustomer(id);
    }
  }

  search() {
    if (this.searchTxt) {
      const payload = {
        searchTxt: this.searchTxt,
        limit: this.limit(),
        offset: this.offset(),
      };
      if (this.curModule() === userMaster) {
        this.searchUser(payload);
      } else if (this.curModule() === brandMaster) {
        this.searchBrand(payload);
      } else if (this.curModule() === itemMaster) {
        this.searchItem(payload);
      } else if (this.curModule() === itemCategoryMaster) {
        this.searchItemCategory(payload);
      } else if (this.curModule() === gstMaster) {
        this.searchGst(payload);
      } else if (this.curModule() === courierMaster) {
        this.searchCourier(payload);
      } else if (this.curModule() === customerMaster) {
        this.searchCustomer(payload);
      }
    }
  }

  onSelectRow(event: any) {
    this.reset();
    this.limit.set(+event.target.value);
    this.paginationDataService.put({ key: 'limit', value: this.limit() });
    this.loadData();
  }

  onSelectCustomer(event: any) {
    const value = event.target.value;
    this.selectedCustomerId.set(+value);
  }

  navigate(targetPage: any) {
    this.offset.set(targetPage - 1);
    this.paginationDataService.put({ key: 'offset', value: this.offset() });
    this.loadData();
  }

  navigateToCreateUser() {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.user,
      environment.create,
    ]);
  }

  navigateToCreateItem() {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.item,
      environment.create,
    ]);
  }

  navigateToCreateGst() {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.gstMaster,
      environment.create,
    ]);
  }

  navigateToCreateCourier() {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.courierMaster,
      environment.create,
    ]);
  }

  navigateToCreateBrand() {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.brandMaster,
      environment.create,
    ]);
  }

  navigateToCreateCustomer() {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.customerMaster,
      environment.master,
      environment.create,
    ]);
  }

  navigateToCreateCustomerItem() {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.customerMaster,
      environment.item,
      environment.create,
    ]);
  }

  editUser(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.user,
      environment.edit,
      id,
    ]);
  }

  editItem(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.item,
      environment.edit,
      id,
    ]);
  }

  editGst(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.gstMaster,
      environment.edit,
      id,
    ]);
  }

  editCourier(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.aosUser,
      environment.courierMaster,
      environment.edit,
      id,
    ]);
  }

  editCustomer(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.customerMaster,
      environment.master,
      environment.edit,
      id,
    ]);
  }

  deleteUser(id: number) {
    this.alertService
      .alert('Warning!', 'Do you want delete this user?', 'question', 'Delete')
      .then((res) => {
        if (res.isConfirmed) {
          this.userService.deleteUser(id).subscribe(
            (res) => {
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

  deleteBrand(id: number) {
    this.alertService
      .alert('Warning!', 'Do you want delete this brand?', 'question', 'Delete')
      .then((res) => {
        if (res.isConfirmed) {
          this.brandService.deleteBrand(id).subscribe(
            (res) => {
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

  deleteItem(id: number) {
    this.alertService
      .alert('Warning!', 'Do you want delete this item?', 'question', 'Delete')
      .then((res) => {
        if (res.isConfirmed) {
          this.itemService.delete(id).subscribe(
            (res) => {
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

  deleteItemCategory(id: number) {
    this.alertService
      .alert(
        'Warning!',
        'Do you want delete this category?',
        'question',
        'Delete'
      )
      .then((res) => {
        if (res.isConfirmed) {
          this.itemCategoryService.deleteItemCategory(id).subscribe(
            (res) => {
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

  deleteGst(id: number) {
    this.alertService
      .alert('Warning!', 'Do you want delete this gst?', 'question', 'Delete')
      .then((res) => {
        if (res.isConfirmed) {
          this.gstService.deleteGst(id).subscribe(
            (res) => {
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

  deleteCourier(id: number) {
    this.alertService
      .alert(
        'Warning!',
        'Do you want delete this courier?',
        'question',
        'Delete'
      )
      .then((res) => {
        if (res.isConfirmed) {
          this.courierService.deleteCourier(id).subscribe(
            (res) => {
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

  deleteCustomer(id: number) {
    this.alertService
      .alert(
        'Warning!',
        'Do you want delete this customer?',
        'question',
        'Delete'
      )
      .then((res) => {
        if (res.isConfirmed) {
          this.customerService.deleteCustomer(id).subscribe(
            (res) => {
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

  searchUser(payload: any) {
    this.userService.search(payload).subscribe(
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

  searchBrand(payload: any) {
    this.brandService.search(payload).subscribe(
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

  searchItem(payload: any) {
    this.itemService.search(payload).subscribe(
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

  searchItemCategory(payload: any) {
    this.itemCategoryService.search(payload).subscribe(
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

  searchGst(payload: any) {
    this.gstService.search(payload).subscribe(
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

  searchCourier(payload: any) {
    this.courierService.search(payload).subscribe(
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

  searchCustomer(payload: any) {
    this.customerService.search(payload).subscribe(
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

  loadUser() {
    this.userService.getUsers(this.limit(), this.offset()).subscribe(
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

  loadItem() {
    this.itemService.getItems(this.limit(), this.offset()).subscribe(
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

  loadItemCategory() {
    this.itemCategoryService
      .getItemCategories(this.limit(), this.offset())
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

  loadGst() {
    this.gstService.getGsts(this.limit(), this.offset()).subscribe(
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

  loadCourier() {
    this.courierService.getCouriers(this.limit(), this.offset()).subscribe(
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

  loadBrand() {
    this.brandService.getBrands(this.limit(), this.offset()).subscribe(
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

  loadCustomer() {
    this.customerService.getCustomers(this.limit(), this.offset()).subscribe(
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

  loadCustomerItem() {
    this.customerItemService.getData().subscribe(
      (res) => {
        this.items = res;
      },
      (err) => {
        console.log(err);
      }
    );
  }

  loadOrders() {
    if (this.selectedCustomerId()) {
      this.orderService
        .getAllByCustomerId(
          this.selectedCustomerId()!,
          this.limit(),
          this.offset()
        )
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
  }

  loadOrdersById() {
    if (this.selectedCustomerId()) {
      this.orderService
        .getAllByCustomerId(
          this.selectedCustomerId()!,
          this.limit(),
          this.offset()
        )
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
  }

  openDialog(id: number | null, module: string): void {
    let component: any | null = null;
    if (id === null) {
      if (module === 'item-category') {
        component = CreateItemCategoryMasterComponent;
      } else if (module === 'brand') {
        component = CreateBrandMasterComponent;
      }
    } else {
      if (module === 'item-category') {
        component = EditItemCategoryMasterComponent;
      } else if (module === 'brand') {
        component = EditBrandMasterComponent;
      }
    }

    const dialog = this.dialog.open(component, {
      width: '500px',
      height: '250px',
      enterAnimationDuration: '0ms',
      exitAnimationDuration: '0ms',
      data: { id },
    });
    dialog.afterClosed().subscribe((res) => {
      if (res === Success) {
        this.reset();
        this.loadData();
      }
    });
  }

  openImage(fileId: number, name: string) {
    if (!fileId) {
      this.snackBarService.openSnackBar({
        msg: 'Please add image to view.',
        type: Warning,
        title: 'No Image',
      });
      return;
    } else {
      this.dialog.open(ImageComponent, {
        enterAnimationDuration: '0ms',
        exitAnimationDuration: '0ms',
        data: { fileId, name },
      });
    }
  }

  viewData(id: number) {
    this.router.navigate([
      environment.servletPath,
      environment.master,
      environment.customerMaster,
      environment.item,
      environment.view,
      id,
    ]);
  }

  viewOrder(orderNo: string) {
    this.router.navigate([
      environment.servletPath,
      environment.order,
      environment.view,
      orderNo,
    ]);
  }

  download(module: string) {
    console.log(module);
    switch (module) {
      case customerItemMaster: {
        this.customerItemService.download().subscribe((blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = 'upload-format.xlsx';
          a.click();
          window.URL.revokeObjectURL(url);
        });
        break;
      }
      case orderMaster: {
        if (this.selectedCustomerId()) {
          this.orderService
            .download(this.selectedCustomerId()!)
            .subscribe((blob) => {
              const url = window.URL.createObjectURL(blob);
              const a = document.createElement('a');
              a.href = url;
              a.download = 'orders-all.xlsx';
              a.click();
              window.URL.revokeObjectURL(url);
            });
        }
        break;
      }
      case itemMaster: {
        this.itemService.download().subscribe((blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = 'item-upload-format.xlsx';
          a.click();
          window.URL.revokeObjectURL(url);
        });
        break;
      }
    }
  }

  onFileSelected(event: Event, module: string) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const formData = new FormData();
      formData.append('file', file);

      if (module === 'customer-item') {
        this.customerItemService.upload(formData).subscribe(
          (response) => {
            this.loadData();
          },
          (error) => {
            console.error(error);
          }
        );
      } else if (module === 'brand') {
        this.brandService.upload(formData).subscribe(
          (response) => {
            this.loadData();
          },
          (error) => {
            console.error(error);
          }
        );
      } else if (module === 'item-category') {
        this.itemCategoryService.upload(formData).subscribe(
          (response) => {
            this.loadData();
          },
          (error) => {
            console.error(error);
          }
        );
      } else if (module === 'item') {
        this.itemService.upload(formData).subscribe(
          (response) => {
            this.loadData();
          },
          (error) => {
            console.error(error);
          }
        );
      }
    }
  }
}
