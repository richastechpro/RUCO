import { Component, inject, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatRippleModule } from '@angular/material/core';
import { DashboardService } from '../../services/dashboard.service';
import { NgClass } from '@angular/common';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment.development';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  imports: [MatCardModule, MatRippleModule, NgClass],
})
export class DashboardComponent implements OnInit {
  items: any[] = [];
  service = inject(DashboardService);
  router = inject(Router);

  ngOnInit(): void {
    this.service.getData().subscribe(
      (res) => {
        this.items = res;
      },
      (err) => {
        console.log(err);
      }
    );
  }

  navigate(route: string) {
    const routes = route.split('/');
    this.router.navigate([environment.servletPath, ...routes]);
  }
}
