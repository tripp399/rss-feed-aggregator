import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FeedAggregateService {

  constructor(private httpClient: HttpClient) { }

  getFeedAggregate(): Observable<any> {
    this.httpClient.get()
  }
}
