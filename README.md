# RSS Feed Aggregator

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

### Maven

[Maven installation instructions](https://maven.apache.org/install.html)

### Node 

[Node](https://nodejs.org/en/download/)

### Node package manager

[Npm installation instructions](https://www.npmjs.com/get-npm)


## Install maven dependencies

`mvn install`

## Build the project

`mvn compile`

## To start application:

### Using maven

`mvn spring-boot:run`

### Using jar file:
Execute:
`mvn pacakage`

Then:
`java -jar .\target\rss-aggregator-0.0.1-SNAPSHOT.jar`

### Angular application

The angular application resides in `webui` directory

Navigate to it using: `cd ./webui`

## Install dependencies for front end

`npm install`

## Run the front end

`ng serve --open`
