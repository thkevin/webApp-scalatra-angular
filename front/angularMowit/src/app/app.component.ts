import { Component } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';
import { MaterialModule } from './shared/modules/material.module'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent {

  constructor( private title: Title) {
    this.setTitle("Mow it noow!!");
  }

  public setTitle( newTitle: string) {
    this.title.setTitle( newTitle );
  }
}
