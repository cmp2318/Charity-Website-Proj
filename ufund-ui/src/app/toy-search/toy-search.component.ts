import { Component, Input } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';

import { Toy } from '../toy';
import { ToyService } from '../toy.service';

@Component({
  selector: 'app-toy-search',
  templateUrl: './toy-search.component.html',
  styleUrls: ['./toy-search.component.css']
})
export class ToySearchComponent {
  toys$!: Observable<Toy[]>;
  private searchTerms = new Subject<string>();

  @Input() userId: string | undefined;

  constructor(private toyService: ToyService) {}

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.toys$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.toyService.searchToys(term)),
    );
  }
}
