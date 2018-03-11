# Tennis Score Processor
> Tennis Score Processor is an open source library dedicated to processing tennis match scoring.

[![Build Status](https://travis-ci.org/lopezton/tennis-score-processor.svg?branch=master)](https://travis-ci.org/lopezton/tennis-score-processor)
[![Coverage Status](https://coveralls.io/repos/github/lopezton/tennis-score-processor/badge.svg?branch=master)](https://coveralls.io/github/lopezton/tennis-score-processor?branch=master)
![Maven Central](https://img.shields.io/maven-central/v/com.tonelope.tennis/scoreprocessor.svg)


Tennis Score Processor is a Java based library that records and processes tennis match data on a stroke-by-stroke or point-by-point bases. 

Use Tennis Score Processor to:  

* Record match data on a stroke-by-stroke or point-by-point basis
* Analyze recorded match data
* Process tennis scoring results in real-time
* Build a real working tennis scoreboard
* Whatever else your imagine can dream up!

The application is currently in it's discovery phase and at the time supports simple scoring features. As the project grows, several types of scoring will be supported as well as other great features including:

* Live/Post Match Reporting
* Event Registration after Point, Game, Set, or Match completion
* Generated Match Metrics

Feedback and Pull Requests are welcome.

## Installation

### Maven
Tennis Score Processor is hosted on [Maven Central](https://search.maven.org/). Include the following in your project's pom.xml:  

```xml
<dependency>
    <groupId>com.tonelope.tennis</groupId>
    <artifactId>scoreprocessor</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Manual
You can download the .jar file directly from the [Github release page](https://github.com/lopezton/tennis-score-processor/releases) and include it on your project's classpath.

## Usage example

Tennis Score Processor uses a main processor object called ```MatchProcessor``` to update and modify the ```Match``` object, a simple POJO containing match data.  See the following diagram:  

![Entry Point](docs/images/uml/entry-point.png)  

Getting started with Tennis Score Processor is easy. Create a ```Match``` entity and update it with the necessary information for the processor to be able to perform updates.

```java
public static void main(String[] args) {
		
		// Build the match data
		Player player1 = new Player("Roger", "Federer");
		Player player2 = new Player("Rafael", "Nadal");
		
		List<Player> players = new ArrayList<>();
		players.add(player1);
		players.add(player2);
		
		// Initialize the playerConfig data
		PlayerConfig playerConfig = new PlayerConfig(players, player1, player2);
		
		// Utilize the framework's match factory to create a new match entity
		MatchFactory matchFactory = new DefaultMatchFactory();
		
		// Instantiate the matchProcessor
		MatchProcessor matchProcessor = new MatchProcessor(matchFactory.create(new MatchRules(), playerConfig));
		
		// Use the matchProcessor to update match results on stroke or point basis
		matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
	}
```

## Development setup

1. Fork/Clone the Repository
2. Install Lombok into your IDE
3. Import the project into your IDE

```sh
mvn clean test
```

## Release History

* 0.0.1-SNAPSHOT
    * Work in progress

## Meta

Tony Lopez – tony.lopez1013@gmail.com

Distributed under the Apache License. See ``LICENSE`` for more information.

[https://github.com/lopezton/tennis-score-processor](https://github.com/lopezton/tennis-score-processor)

## Contributing

1. Fork it (<https://github.com/lopezton/tennis-score-processor/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

This project uses a forking Git Flow for it's contribution and deployment strategies.