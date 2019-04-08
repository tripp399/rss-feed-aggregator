import { TestBed } from '@angular/core/testing';

import { FeedAggregateService } from './feed-aggregate.service';

describe('FeedAggregateService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FeedAggregateService = TestBed.get(FeedAggregateService);
    expect(service).toBeTruthy();
  });
});
