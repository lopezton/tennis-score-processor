# Tennis Score Processor
> Tennis Score Processor is an open source library dedicated to processing tennis match scoring.

[![Build Status](https://travis-ci.org/lopezton/tennis-score-processor.svg?branch=master)](https://travis-ci.org/lopezton/tennis-score-processor)
[![Coverage Status](https://coveralls.io/repos/github/lopezton/tennis-score-processor/badge.svg?branch=master)](https://coveralls.io/github/lopezton/tennis-score-processor?branch=master)
![Maven Central](https://img.shields.io/maven-central/v/com.tonelope.tennis/scoreprocessor.svg)

Tennis Score Processor is a Java based library that records and processes tennis match data on a stroke-by-stroke or point-by-point bases. 

## Overview
Tennis Score Processor uses a main processor object called `TennisScoreProcessor` or `TennisScoreProcessorBuilder` to update and modify match data.

## Quick Links

* [Request a feature](https://github.com/lopezton/tennis-score-processor/issues/new?template=issue_template.md)
* [Report a defect](https://github.com/lopezton/tennis-score-processor/issues/new?template=defect_template.md)

## Installation

### Maven
Tennis Score Processor is hosted on [Maven Central](https://search.maven.org/). Include the following in your project's pom.xml:  

```xml
<dependency>
    <groupId>com.tonelope.tennis</groupId>
    <artifactId>scoreprocessor</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Manual
You can download the .jar file directly from the [Github release page](https://github.com/lopezton/tennis-score-processor/releases) and include it on your project's classpath.

## Usage

### Use Cases
Use Tennis Score Processor to:  

* Record match data on a stroke-by-stroke or point-by-point basis
* Analyze recorded match data
* Process tennis scoring results in real-time
* Build a real working tennis scoreboard
* Whatever else your imagine can dream up!

### Get Started
Getting started with Tennis Score Processor is easy. See the following code sample:  

```java
public static void main(String[] args) {

	// Build the processor
	private TennisScoreProcessor tsp = new ScoreKeeperBuilder().build();
	
	// Record a point(s)
	Point point = new Point(Team.HOME);
	tsp.update(point);

	// Print desired results
	System.out.println(tsp.getScore());
}
```

## Development setup

1. Fork/Clone the Repository
2. Install Lombok into your IDE
3. Import the project into your IDE

## Contributing

See [Contributing](/docs/CONTRIBUTING.md) for information on how to contribute to this project. Contributions are open to the community and very much appreciated!

## Meta

The application is currently in it's discovery phase and at the time supports singles scoring features. As the project grows, several types of scoring will be supported as well as other great features including:

* Doubles Scoring
* Live/Post Match Reporting
* Generated Match Metrics

Feedback and Pull Requests are extremely welcome!

Maintained by: Tony Lopez – tony.lopez1013@gmail.com

Distributed under the Apache License. See ``LICENSE`` for more information.

[https://github.com/lopezton/tennis-score-processor](https://github.com/lopezton/tennis-score-processor)
