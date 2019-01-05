import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PlaygroundComponent } from './playground/playground.component';
import { HomeComponent } from './home/home.component';
import { AllCampaignsComponent } from './all-campaigns/all-campaigns.component';


const appRoutes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  {path: 'home', component: HomeComponent },
  {path: 'playground/:id', component: PlaygroundComponent },
  {path: 'campaigns', component: AllCampaignsComponent }
]


@NgModule({
  imports: [ RouterModule.forRoot(appRoutes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
