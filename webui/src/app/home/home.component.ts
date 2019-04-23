import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatPaginator } from '@angular/material';

import { FeedAggregateService } from '../feed-aggregate.service';
import { FeedAggregate } from '../feed-aggregate';
import { FeedMessage } from '../feed-message';

const testAggregateList = [
  {
    title: 'Title 1', description: 'Description 1', link: 'http://podcasts.joerogan.net/feed', author: 'Author 1', pubDate: new Date() 
  },
  {
    title: 'Title 2', description: 'Description 2', link: 'http://rss.nytimes.com/services/xml/rss/nyt/World.xml',
    author: 'Author 2', pubDate: new Date()
  }
];

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  feedAggregate: FeedAggregate = new FeedAggregate();
  // tableColumns = ['title', 'description'];
  tableColumns = ['feed'];
  dataSource = new MatTableDataSource<FeedMessage>(testAggregateList);

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private aggregateService: FeedAggregateService) { }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.aggregateService.getFeedAggregate()
      .toPromise()
      .then(aggregate => {
        this.feedAggregate.aggregateList = aggregate;
        this.dataSource.data = aggregate;
        console.log('this.feedAggregate:');
        console.log(this.feedAggregate);
      });
  }

  parseHtml(html: string) {
    document.getElementById('desc').innerHTML = html;
  }

  redirectToSource(url: string): void {
    window.open(url, '_blank');
  }

  getDomainFromUrl(url: string) {
    if (!(url.length === 0)) {
      const domain = url.match(/:\/\/(.[^/]+)/);
      if (!(domain == null)) {
        return domain[1];
      }
    }
  }

}
