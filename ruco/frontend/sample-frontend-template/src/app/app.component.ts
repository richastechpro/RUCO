import { Component, HostListener, OnInit, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LoaderService } from '../services/loader.service';
import { UrlChangeInterceptorService } from '../services/url-change.service';
import { PaginationDataService } from '../services/pagination-data.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  imports: [RouterModule],
})
export class AppComponent implements OnInit {
  isLoading = signal(false);

  constructor(
    private loaderService: LoaderService,
    private urlChangeInterceptor: UrlChangeInterceptorService,
    private paginationDataService: PaginationDataService
  ) {
    this.isLoading = this.loaderService.isLoading;
  }

  ngOnInit() {
    this.urlChangeInterceptor.interceptUrlChanges();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeUnloadHandler(event: Event) {
    this.clearLocalStorage();
  }

  clearLocalStorage() {
    this.paginationDataService.clearAll();
  }
}
