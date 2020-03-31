# Server-side of Sustainable Trip Planner And Navigation App
The server-side is not the application itself, but rather a seperate application that helps to process the actual (client) application's requests such as routing. This contributes to a server-client relationship.
* [Getting Started](#Getting-Started)
* [Usage](#Usage)

## Getting Started
* [Software Dependencies](#Software-Dependencies)

### Software Dependencies
* Java 1.8.0_221

## Usage
To build, all you need to do is run the Gradle Wrapper which installs the necessary Gradle and Java dependencies.
```bash
gradlew(.bat) build
```

After building, just execute
```bash
gradlew(.bat) run <port>
```
in a terminal in the same directory.

You must specify a port number.