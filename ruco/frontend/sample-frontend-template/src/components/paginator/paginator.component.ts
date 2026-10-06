import { CommonModule } from '@angular/common';
import {
  Component,
  computed,
  EventEmitter,
  input,
  Input,
  OnChanges,
  OnInit,
  Output,
  signal,
  SimpleChanges,
} from '@angular/core';
import { Paginator } from '../../interfaces/paginator';
import { MatIconModule } from '@angular/material/icon';
import { PaginationDataService } from '../../services/pagination-data.service';
import { of } from 'rxjs';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.scss',
  imports: [CommonModule, MatIconModule],
})
export class PaginatorComponent implements OnInit, OnChanges {
  @Input('from') offset!: number;
  @Input('to') to!: number;
  @Input('records') records!: number;
  // total no of pages to be shown (list.size() / limit)
  // size sholud be greater than 0 (assumption)
  @Input('size') size!: number;
  @Output('page') page: EventEmitter<any> = new EventEmitter<any>();

  pages: Paginator[] = [];
  currentPage!: Paginator | undefined;

  constructor(private paginationDataService: PaginationDataService) {}

  ngOnInit(): void {
    this.initData();
    if (this.to > this.records) {
      this.to = this.records;
    }
    const offset: number = +this.paginationDataService.get('offset')!;
    if (offset) {
      this.pages.forEach((p) => {
        if (p.index === offset) {
          p.active = true;
          this.currentPage = p;
        } else {
          p.active = false;
        }
      });
    }
  }

  roundDownToNearestTen(num: number): number {
    return Math.floor(Math.floor(num / 10) * 10);
  }

  ngOnChanges(changes: SimpleChanges): void {
    // if size changes, ie) the user selected different row size(no of pages changed) then recalculate
    if (changes['size']) {
      this.initData();
    }

    if (changes['to']) {
      if (this.to > this.records) {
        this.to = this.records;
      }
    }
  }

  initData() {
    // empty the array
    this.pages.splice(0);
    // currentPage is not known yet
    this.currentPage = undefined;
    // if size is less than 10, then create pages for that size
    // else create pages of size 10, since size is > 10 we need to display pages upto length 10 in UI
    if (this.size > 0) {
      if (this.size <= 10) this.createPages(this.size);
      else this.createPages(10);

      // set first page to active
      this.pages[0]['active'] = true;
      this.currentPage = this.pages[0];
    }
  }

  createPages(length: number) {
    for (let i = 0; i < length; i++)
      this.pages.push({ index: i, pageNo: i + 1, active: false });
  }

  previous(emit: boolean, targetIndex: number) {
    const currentIndex = this.currentPage!.index;

    // paginator not reached the start
    if (currentIndex > 0) {
      this.reset();
    } else {
      return;
    }

    this.pages[targetIndex]['active'] = true;
    this.currentPage = this.pages[targetIndex];

    const currentPageNo = this.currentPage.pageNo;

    if (emit) this.emitPage();

    if (targetIndex === 0 && currentPageNo > 1) {
      this.pages.pop();
      const page: Paginator = {
        pageNo: currentPageNo - 1,
        active: false,
        index: 0,
      };
      this.pages.splice(0, 0, page);
      this.syncIndex();
    }
  }

  next(emit: boolean, targetIndex: number) {
    let currentPageNo = this.currentPage!.pageNo;

    // paginator not reached the end
    if (currentPageNo < this.size) {
      this.reset();
    } else {
      return;
    }

    //  page: 1 - 8
    this.pages[targetIndex]['active'] = true;
    this.currentPage = this.pages[targetIndex];

    currentPageNo = this.currentPage.pageNo;

    // page: 9 - size
    if (targetIndex === 9 && currentPageNo + 1 <= this.size) {
      this.pages.splice(0, 1);
      this.pages.push({ pageNo: currentPageNo + 1, active: false, index: 0 });
      this.syncIndex();
    }

    if (emit) this.emitPage();
  }

  navigate(pageNo: number, targetIndex: number) {
    const currentPageNo = this.currentPage!.pageNo;
    if (pageNo > currentPageNo) {
      this.next(false, targetIndex);
    } else if (pageNo < currentPageNo) {
      this.previous(false, targetIndex);
    }
    if (pageNo !== currentPageNo) this.emitPage();
  }

  emitPage() {
    this.page.emit(this.currentPage!.pageNo);
  }

  reset() {
    this.pages.forEach((page) => (page.active = false));
  }

  syncIndex() {
    this.pages.forEach((page, i) => (page.index = i));
  }
}
