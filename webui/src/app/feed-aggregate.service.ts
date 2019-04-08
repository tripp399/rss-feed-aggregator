import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FeedAggregate } from './feed-aggregate';
import { FeedMessage } from './feed-message';

@Injectable({
  providedIn: 'root'
})
export class FeedAggregateService {

  constructor(private httpClient: HttpClient) { }

  getFeedAggregate(): Observable<FeedMessage[]> {
    return this.httpClient.get<FeedMessage[]>('http://localhost:8080/feeds');
  }
}
