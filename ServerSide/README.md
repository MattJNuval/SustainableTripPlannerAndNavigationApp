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
You must get your own API Key for [Air Visual](https://www.iqair.com/) and [HERE](https://www.here.com/).
We have two sample keys for you to use:
```
Air Visual API Key: d7664ac9-d9fb-4ed4-b6f6-2e8feac28693
```
```
HERE API Key: ZOBTtCPG_WoP8VHh-xDXFdekw0AzdKkF9S5gGvZkxDY
```

## Usage
To build, all you need to do is run the Gradle Wrapper which installs the necessary Gradle and Java dependencies.
```bash
gradlew build
```
in a terminal in the same directory (for Windows systems, use gradlew.bat).

After building, just execute
```bash
gradlew run <port> <testing-value> <air-visual-api-key> <here-api-key>
```
in a terminal in the same directory (for Windows systems, use gradlew.bat).

You must specify a port number for <port>, a testing value (true/false) for <testing-value>, an Air Visual API Key for <air-visual-api-key>, and a HERE API Key for <here-api-key>.
