import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import {Campaign} from './campaign';
import {FullCampaign} from './full-campaign';

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  private uri_root = 'http://localhost:8080'

  constructor( private http: HttpClient) { }

  getMowersCount(): Observable<any> {
    return this.http.get<any>(this.uri_root+'/countmowers')
    .pipe(
      tap(count => console.log('mowers counted')),
      catchError(this.handleError('getMowersCount', null))
    );
  }

  getCampaignsCount(): Observable<any> {
    return this.http.get<any>(this.uri_root+'/countcampaigns')
    .pipe(
      tap(count => console.log('campaigns counted')),
      catchError(this.handleError('getCampaignsCount', null))
    );
  }

  getCampaign(id: string): Observable<FullCampaign> {
    return this.http.get<FullCampaign>(this.uri_root+'/getcampaign/'+id)
    .pipe(
      tap(campaigns => console.log('fetched campaign with id:'+id)),
      catchError(this.handleError('getCampaign', null))
    );
  }

  getAllCampaigns(): Observable<Campaign[]> {
    return this.http.get<Campaign[]>(this.uri_root+'/getcampaigns').pipe(
      tap(_ => console.log(`found all campaigns`)),
      catchError(this.handleError<Campaign[]>('getAllCampaigns', []))
    );
  }


  searchCampaigns(term: string): Observable<Campaign[]> {
    if (!term.trim()) {
      return this.getAllCampaigns()
    }
    return this.http.get<Campaign[]>(`${this.uri_root}/getcampaigns/name/${term}`).pipe(
      tap(_ => console.log(`found campaigns matching name like "${term}"`)),
      catchError(this.handleError<Campaign[]>('searchCampaign', []))
    );
  }

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
