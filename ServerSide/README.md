# Server-side of Sustainable Trip Planner And Navigation App
The server-side is not the application itself, but rather a seperate application that helps to process the actual (client) application's requests such as routing. This contributes to a server-client relationship.
* [Getting Started](#Getting-Started)
* [API Keys](#API-Keys)
* [Usage](#Usage)

## Getting Started
* [Software Dependencies](#Software-Dependencies)

### Software Dependencies
* Java 1.8.0_221

## API Keys
It is recommended that you change API keys to your own.
```java
sustainability_app.server.comm.ServerClientCommunication.AIR_VISUAL_API_KEY
sustainability_app.server.comm.ServerClientCommunication.HERE_API_KEY
```

## Usage
To build, all you need to do is run the Gradle Wrapper which installs the necessary Gradle and Java dependencies.
```bash
gradlew build
```
in a terminal in the same directory (for Windows systems, use gradlew.bat).

After building, just execute
```bash
gradlew run <port> false
```
in a terminal in the same directory (for Windows systems, use gradlew.bat).

You must specify a port.