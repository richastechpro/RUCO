import { Routes } from '@angular/router';
import { environment } from '../environments/environment.development';
import { LayoutComponent } from '../components/layout/layout.component';
import { DashboardComponent } from '../pages/dashboard/dashboard.component';
import { EntryComponent } from '../pages/entry/entry.component';
import { AdminLoginComponent } from '../pages/auth/admin-login/admin-login.component';
import { CompanyListComponent } from '../pages/company/company-list/company-list.component';
import { CompanyEditComponent } from '../pages/company/company-edit/company-edit.component';
import { BranchListComponent } from '../pages/branch/branch-list/branch-list.component';
import { BranchEditComponent } from '../pages/branch/branch-edit/branch-edit.component';
import { BranchAddComponent } from '../pages/branch/branch-add/branch-add.component';
import { UserListComponent } from '../pages/user/user-list/user-list.component';
import { UserAddComponent } from '../pages/user/user-add/user-add.component';
import { UserEditComponent } from '../pages/user/user-edit/user-edit.component';
import { TransporterListComponent } from '../pages/transporter/transporter-list/transporter-list.component';
import { TransporterAddComponent } from '../pages/transporter/transporter-add/transporter-add.component';
import { TransporterEditComponent } from '../pages/transporter/transporter-edit/transporter-edit.component';
import { StateListComponent } from '../pages/location/state/state-list/state-list.component';
import { CityListComponent } from '../pages/location/city/city-list/city-list.component';
import { AreaListComponent } from '../pages/location/area/area-list/area-list.component';
import { FboListComponent } from '../pages/fbo/fbo-list/fbo-list.component';
import { FboAddComponent } from '../pages/fbo/fbo-add/fbo-add.component';
import { FboEditComponent } from '../pages/fbo/fbo-edit/fbo-edit.component';
import { CollectionListComponent } from '../pages/collection/collection-list/collection-list.component';
import { FboLoginAddComponent } from '../pages/fbo/fbo-login-add/fbo-login-add.component';
import { BulkCollectionEditComponent } from '../pages/collection/bulk-collection/bulk-collection-edit/bulk-collection-edit.component';
import { BulkCollectionCollectComponent } from '../pages/collection/bulk-collection/bulk-collection-collect/bulk-collection-collect.component';
import { CertificateComponent } from '../pages/certificate/certificate.component';
import { BulkCollectionAddComponent } from '../pages/collection/bulk-collection/bulk-collection-add/bulk-collection-add.component';
import { CustomerListComponent } from '../pages/customer/customer-list/customer-list.component';
import { CustomerAddComponent } from '../pages/customer/customer-add/customer-add.component';
import { CustomerEditComponent } from '../pages/customer/customer-edit/customer-edit.component';
import { ReportComponent } from '../pages/report/report.component';
import { InventoryOutComponent } from '../pages/inventory/inventory-out/inventory-out.component';
import { InventoryAllComponent } from '../pages/inventory/inventory-all/inventory-all.component';
import { FboBranchAddComponent } from '../pages/fbo/fbo-branch/fbo-branch-add/fbo-branch-add.component';
import { FboBranchEditComponent } from '../pages/fbo/fbo-branch/fbo-branch-edit/fbo-branch-edit.component';
import { InventoryInComponent } from '../pages/inventory/inventory-in/inventory-in.component';
import { FBOLoginComponent } from '../pages/auth/fbo-login/fbo-login.component';
import { ApprovalListComponent } from '../pages/approve/approval-list/approval-list.component';
import { ApprovalEditComponent } from '../pages/approve/approval-edit/approval-edit.component';
import { ApprovalViewComponent } from '../pages/approve/approval-view/approval-view.component';
import { TicketListComponent } from '../pages/ticket/ticket-list/ticket-list.component';
import { TicketEditComponent } from '../pages/ticket/ticket-edit/ticket-edit.component';
import { RatesComponent } from '../pages/rates/rates.component';

export const routes: Routes = [
  { path: '', component: EntryComponent },

  { path: 'login/admin', component: AdminLoginComponent },
  { path: 'login/fbo', component: FBOLoginComponent },

  {
    path: environment.servletPath,
    component: LayoutComponent,
    children: [
      {
        path: environment.dashboard,
        component: DashboardComponent,
      },

      {
        path: environment.company,
        children: [
          { path: environment.list, component: CompanyListComponent },
          { path: `${environment.edit}/:id`, component: CompanyEditComponent },
        ],
      },

      {
        path: environment.customer,
        children: [
          { path: environment.list, component: CustomerListComponent },
          { path: environment.create, component: CustomerAddComponent },
          { path: `${environment.edit}/:id`, component: CustomerEditComponent },
        ],
      },

      {
        path: environment.branch,
        children: [
          { path: environment.list, component: BranchListComponent },
          { path: environment.create, component: BranchAddComponent },
          { path: `${environment.edit}/:id`, component: BranchEditComponent },
        ],
      },

      {
        path: environment.user,
        children: [
          { path: environment.list, component: UserListComponent },
          { path: environment.create, component: UserAddComponent },
          { path: `${environment.edit}/:id`, component: UserEditComponent },
        ],
      },

      {
        path: environment.transporter,
        children: [
          { path: environment.list, component: TransporterListComponent },
          { path: environment.create, component: TransporterAddComponent },
          {
            path: `${environment.edit}/:id`,
            component: TransporterEditComponent,
          },
        ],
      },

      {
        path: environment.location,
        children: [
          {
            path: `${environment.state}/${environment.list}`,
            component: StateListComponent,
          },
          {
            path: `${environment.city}/${environment.list}`,
            component: CityListComponent,
          },
          {
            path: `${environment.area}/${environment.list}`,
            component: AreaListComponent,
          },
        ],
      },

      {
        path: environment.fbo,
        children: [
          {
            path: environment.list,
            component: FboListComponent,
          },
          {
            path: environment.create,
            component: FboAddComponent,
          },
          {
            path: `${environment.edit}/:id`,
            component: FboEditComponent,
          },
          {
            path: `${environment.create}/login/:id`,
            component: FboLoginAddComponent,
          },
          {
            path: `${environment.branch}/${environment.create}/:id`,
            component: FboBranchAddComponent,
          },
          {
            path: `${environment.branch}/${environment.edit}/:id/:fboBranchId`,
            component: FboBranchEditComponent,
          },
        ],
      },

      {
        path: environment.collection,
        children: [
          {
            path: environment.list,
            component: CollectionListComponent,
          },
          {
            path: environment.bulk,
            component: BulkCollectionAddComponent,
          },
          {
            path: `${environment.bulk}/:id`,
            component: BulkCollectionEditComponent,
          },
          {
            path: `${environment.bulk}/collect/:id`,
            component: BulkCollectionCollectComponent,
          },
        ],
      },

      {
        path: environment.inventory,
        children: [
          { path: 'in', component: InventoryInComponent },
          { path: 'out', component: InventoryOutComponent },
          { path: 'all', component: InventoryAllComponent },
        ],
      },

      {
        path: environment.report,
        component: ReportComponent,
      },

      {
        path: 'certificate/:id',
        component: CertificateComponent,
      },

      {
        path: environment.approval,
        children: [
          {
            path: environment.list,
            component: ApprovalListComponent,
          },
          {
            path: `${environment.edit}/:id`,
            component: ApprovalEditComponent,
          },
          {
            path: `${environment.view}/:id`,
            component: ApprovalViewComponent,
          },
        ],
      },

      {
        path: environment.ticket,
        children: [
          {
            path: environment.list,
            component: TicketListComponent,
          },
          {
            path: `${environment.edit}/:id`,
            component: TicketEditComponent,
          },
        ],
      },

      {
        path: environment.rates,
        component: RatesComponent,
      },
    ],
  },
];
