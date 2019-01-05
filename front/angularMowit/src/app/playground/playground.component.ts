import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { BackendService } from '../backend.service';
import {FullCampaign} from '../full-campaign';


@Component({
  selector: 'app-playground',
  templateUrl: './playground.component.html',
  styleUrls: ['./playground.component.css']
})
export class PlaygroundComponent implements OnInit {
  fullCamp: FullCampaign;

  isDataAvailable:boolean = false;

  constructor( private title: Title, private backend: BackendService, private location: Location, private route: ActivatedRoute) {
    this.setTitle("Mow it nooow - Playground");
  }

  public setTitle( newTitle: string) {
    this.title.setTitle( newTitle );
  }

  ngOnInit() {
    this.getIdCampaign()
  }

  private getIdCampaign(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.backend.getCampaign(id)
    .subscribe( res => {
        this.fullCamp = res;
        this.isDataAvailable = true
      })
  }

}
