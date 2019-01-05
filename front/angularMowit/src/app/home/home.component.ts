import { Component, OnInit } from '@angular/core';
import {Meta, Title} from '@angular/platform-browser';
import {BackendService} from '../backend.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  startCampaign: boolean;
  campaignsCount: number;
  mowersCount: number;

  constructor( private title: Title, private backend : BackendService) {
    this.startCampaign = false;
    this.setTitle("Mow it nooow - Home");
  }

  setTitle( newTitle: string) {
    this.title.setTitle( newTitle );
  }

  starting(): void {
    this.startCampaign = !this.startCampaign;
  }

  initCampaignsCount(): void {
    this.backend.getCampaignsCount()
    .subscribe( res => {
      this.campaignsCount = res.count
    })
  }

  initMowersCount(): void {
    this.backend.getMowersCount()
    .subscribe( res => {
      this.mowersCount = res.count
    })
  }

  ngOnInit() {
    this.initMowersCount()
    this.initCampaignsCount()
  }

}
