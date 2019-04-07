import { Component, OnInit } from '@angular/core';
import { FeedAggregateService } from '../feed-aggregate.service';
import { FeedAggregate } from '../feed-aggregate';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  feedAggregate: FeedAggregate = new FeedAggregate();

  constructor(private faService: FeedAggregateService) { }

  ngOnInit() {
    this.faService.getFeedAggregate()
      .toPromise()
      .then(aggregate => {
        this.feedAggregate.aggregateList = aggregate;
        console.log('this.feedAggregate:');
        console.log(this.feedAggregate);
      });
    // this.feedAggregate.aggregateList = [
    //   { title: 'Title 1', description: 'Description 1', link: 'Link 1', author: 'Author 1', guid: 'guid 1' },
    //   { title: 'Title 2', description: 'Description 2', link: 'Link 2', author: 'Author 2', guid: 'guid 2' }
    // ];
  }

}
