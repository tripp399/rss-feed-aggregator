# RSS Feed Aggregator

## Authors

- Nishtha Shrivastav (2594-5398)
    nishthaShrivastav
- Pulkit Tripathi (9751-9461)
    tripp399, pt-repos
- Akash Shingte (4874-1966)
    cieloazure

## Contributions

- Nishtha Shrivatav
    - Developed the logic for parsing OPML Files parallely.
    - Developed the Thread pool implementation.
    - Developed the Direct Mapping implementation.
    - Tests for Thread pool with different thread sizes.

- Pulkit Tripathi
    - Set up the framework of the Spring project.
    - Callable FeedParser implementation
    - SimpleFeedAggregator (aggregation using threadpool without blocking queues and direct mapping) implementation
    - Tests for correctness of aggregation.
    - Complete Angular front end for displaying feeds.
    - Architecture design 

- Akash Shingte
    - Implementation of BlockingQueueFeedAggregator and FeedScanner.
    - Tests for the Blocking Queue implementation of aggregation
    - Refactored FeedMessage with comparator.
    - Execution speed tests.
    - Architecture design

## Project Description
RSS is a type of web feed which allows interested users to receive updates about their preferred web-based content within a very short time after it is updated.RSS Feed Reader aggregates multiple RSS feeds to display them in a single place.
RSS Feed aggregator is the perfect project to demonstrate the power of multithreading. It is a means to show the famous problem of producer-consumer. There are multiple RSS Feed available on the web. To display the content of various sites in one place we need to -
1. Request for that web page
2. Wait till we receive a response of the feed
3. Parse the elements of the feed
4. Aggregate the result of various sources

Performing these 4 tasks in a sequential manner is a time consuming process and doesn’t utilize the computing power we have.If we delegated the tasks to multiple processors to do these process in a concurrent manner we will achieve faster retrieval of information.

In this project, we used various techniques to delegate tasks for concurrent execution. The techniques we implemented are as follows -

- Blocking queues
- Thread Pools
- Direct Mapping to Threads
- Sequential execution (to display the efficiency of concurrent techniques by comparison)

## Pre-requisite for building

1. Maven
2. Node package manager

### Node package manager

[Npm installation instructions](https://www.npmjs.com/get-npm)

### Maven

[Maven installation instructions](https://maven.apache.org/install.html)

## Build the project

`mvn compile`

## Executing the project

`mvn package`

## To start application:

### Using maven

`mvn spring-boot:run`

### Using jar file:

`java -jar .\target\rss-aggregator-0.0.1-SNAPSHOT.jar`

### Angular JS application

`cd webui`

`ng serve --open`
