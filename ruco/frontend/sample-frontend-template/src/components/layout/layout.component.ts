import {
  AfterViewInit,
  Component,
  computed,
  effect,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {
  NavigationEnd,
  Router,
  RouterModule,
  RouterOutlet,
  UrlSegment,
} from '@angular/router';
import { environment } from '../../environments/environment.development';
import { CommonModule } from '@angular/common';
import { MatMenuModule } from '@angular/material/menu';
import { filter } from 'rxjs';
import { UrlChangeInterceptorService } from '../../services/url-change.service';
import { PaginationDataService } from '../../services/pagination-data.service';
import { MatBadgeModule } from '@angular/material/badge';
import { getSidemenu } from '../../constants/SideMenu';
import { AuthService } from '../../services/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { UpdatePwdDialogComponent } from '../../dialogs/update-pwd-dialog/update-pwd-dialog.component';
@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
  imports: [
    MatButtonModule,
    MatIconModule,
    RouterModule,
    CommonModule,
    MatMenuModule,
    MatBadgeModule,
    // RouterOutlet
  ],
})
export class LayoutComponent implements AfterViewInit {
  sideMenuOpened = signal(true);
  // sideMenuOpened = signal(false);
  sideMenusWidth = computed(() => (this.sideMenuOpened() ? 300 : 80));
  items: any[] = [];
  username!: string;
  role!: string;
  navigateRoute = signal<null | any>(null);
  private dialog = inject(MatDialog);

  constructor(
    private router: Router,
    private urlService: UrlChangeInterceptorService,
    private paginationDataService: PaginationDataService,
    private authService: AuthService
  ) {
    const user = localStorage.getItem('user');
    this.navigateRoute = this.urlService.tarUrls;
    if (user) {
      const appUser = JSON.parse(user);
      this.username = appUser.username;
      this.role = appUser.role;
      this.items = getSidemenu(appUser.role);
    }

    effect(() => {
      if (this.navigateRoute() !== null) {
        const urls = this.navigateRoute();
        if (urls) {
          let items = [];
          const length = urls.length;
          for (let i = 0; i < this.items.length; i++) {
            const url = urls[0];
            const item = this.items[i];
            if (item && url === item.route) {
              items.push(item);
              if (length === 1) {
                break;
              } else {
                const subItems = item.subItems;
                for (let j = 0; j < subItems.length; j++) {
                  const subUrl = urls[1];
                  const subItem = subItems[j];
                  if (subUrl === subItem.route) {
                    items.push(subItem);
                    if (length === 2) {
                      break;
                    } else {
                      const subItemsItems = subItem.subItemsItems;
                      for (let k = 0; k < subItemsItems.length; k++) {
                        const subSubUrl = urls[2];
                        const subItemItem = subItemsItems[k];
                        if (subSubUrl === subItemItem.route) {
                          items.push(subItemItem);
                          break;
                        }
                      }
                    }
                  }
                }
              }
            }
          }
          this.navigate(this.navigateRoute(), items);
        }
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.role === 'HR') {
      this.navigate(['user', 'list'], []);
    }
  }

  navigate(route: string[], data: any[]) {
    if (route !== null) {
      this.resetRoute();
      for (let i = 0; i < this.items.length; i++) {
        const item = this.items[i];
        const length = data.length;

        // single route
        if (length === 1 && item.route === data[0].route) {
          item.selected.set(true);
          break;
        }

        // second route
        else if (length === 2) {
          if (item.route === data[0].route) {
            item.selected.set(true);
            item.showSubItem.set(true);
            const subItems: any[] = item.subItems;
            for (let j = 0; j < subItems.length; j++) {
              const subItem = subItems[j];
              if (subItem.route === data[1].route) {
                subItem.selected.set(true);
                break;
              }
            }
            break;
          }
        }

        // thrird route
        else if (length === 3) {
          if (item.route === data[0].route) {
            item.selected.set(true);
            item.showSubItem.set(true);
            const subItems: any[] = item.subItems;
            for (let j = 0; j < subItems.length; j++) {
              const subItem = subItems[j];
              if (subItem.route === data[1].route) {
                subItem.selected.set(true);
                subItem.showSubItemsItem.set(true);
                const subItemItems: any[] = subItem.subItemsItems;
                for (let k = 0; k < subItemItems.length; k++) {
                  const subItemsItem = subItemItems[k];
                  if (subItemsItem.route === data[2].route) {
                    subItemsItem.selected.set(true);
                    break;
                  }
                }
                break;
              }
            }
            break;
          }
        }
      }
      let routes: string[] = [];
      routes.push(environment.servletPath);
      for (let i = 0; i < route.length; i++) {
        const newRoute: string = route[i];
        const newRoutes: string[] = newRoute.split('/');
        for (let j = 0; j < newRoutes.length; j++) {
          routes.push(newRoutes[j]);
        }
      }
      this.router.navigate(routes);
    }
  }

  resetRoute() {
    this.items.forEach((item) => {
      item.selected.set(false);
      item.showSubItem.set(false);
      const subItems: any[] = item.subItems;
      if (subItems.length > 0) {
        subItems.forEach((subItem) => {
          subItem.selected.set(false);
          subItem.showSubItemsItem.set(false);
          const subItemsItems: any[] = subItem.subItemsItems;
          if (subItemsItems.length > 0) {
            subItemsItems.forEach((subItemsItem) => {
              subItemsItem.selected.set(false);
            });
          }
        });
      }
    });
  }

  logout() {
    localStorage.clear();
    this.paginationDataService.clearAll();
    this.router.navigate(['']);
  }

  updatePWD() {
    const dialog = this.dialog.open(UpdatePwdDialogComponent);
    dialog.afterClosed().subscribe((res) => {
      if (res) {
        this.authService.updatePWD({ pwd: res.pwd }).subscribe(
          (res) => {
            console.log(res);
          },
          (err) => {
            console.log(err);
          }
        );
      }
    });
  }
}
