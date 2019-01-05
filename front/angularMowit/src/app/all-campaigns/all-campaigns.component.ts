import { Component, OnInit } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Title } from '@angular/platform-browser';

import {BackendService} from '../backend.service';
import {Campaign} from '../campaign';

@Component({
  selector: 'app-all-campaigns',
  templateUrl: './all-campaigns.component.html',
  styleUrls: ['./all-campaigns.component.css']
})
export class AllCampaignsComponent implements OnInit {
  campaigns$: Observable<Campaign[]>;
  private searchTerms = new Subject<string>();

  constructor( private title: Title, private backend: BackendService) {
    this.setTitle("Mow it nooow - Campaigns");
  }

  public setTitle( newTitle: string) {
    this.title.setTitle( newTitle );
  }

  ngOnInit(): void {
    this.campaigns$ = this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((term: string) => this.backend.searchCampaigns(term))
    )
  }

  search(term: string): void {
    this.searchTerms.next(term);
  }
}
