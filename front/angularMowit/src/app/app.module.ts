import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from './app.component';
import { PlaygroundComponent } from './playground/playground.component';
import { HomeComponent } from './home/home.component';
import { AllCampaignsComponent } from './all-campaigns/all-campaigns.component';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from './shared/modules/material.module';
import { AppRoutingModule } from './/app-routing.module';
import { NewCampaignFormComponent } from './new-campaign-form/new-campaign-form.component';
import { MAT_LABEL_GLOBAL_OPTIONS } from '@angular/material';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    PlaygroundComponent,
    HomeComponent,
    AllCampaignsComponent,
    NewCampaignFormComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [ ],
  bootstrap: [AppComponent]
})
export class AppModule { }
