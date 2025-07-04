# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and
WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Diagram link

### presentation

https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEyPYAFmA6CD6GAEoo9kiqFnJIEGiBAO4hSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAERdlGjAALYogxWDMIMANHO46unQHNOzC3Mo48BICBtzAL6YwuUwxazsXJRVQyNQY5OHW4PLqqtQ6zNzi4M7ewOP0GJzYnG4sEuZ1EVSgcQSOSgAApYvFEpRYgBHKKJACUpzKoguJVk8iUKnUVXsKDAAFVukiHk8UPjSYplGpVMSjDoKgAxJCcGD0yhsmA6SwwJkTMQ6OHAADWwu6MHS2RCUu6zJgwAQ8o4kpQAA8ERo2eTORcoYSVFURVA2QSRCorSUzrcYAo9ShgAamor0ABRI0qbAEVJO84FS55cxVAAsTgAzH0hpN1MBqdM5oGoN5KprRjKdd7fZL5Ar0CCzJxMOaOepXaVnSgqmgfAgEJGiZd6xTVBUQPKcvbGd02aztBbG5djBUFBwOMrRdpuy7e1OGwOhz6cgofGAQkjgAeQhO65v+9y5wul-vD47oevLmCbgXUQiMWoO1hXxCm+6Bb3FqMqzFUmx-Ceh5NBAlZoGBczHJGlBNrGGBVAATE4TipsMIHPDA4G-HMUEhDBcEIZsJzoBwpheL4-gBNA7DUjAAAyEDxGkASZNkuTIOY3KAVUdSNK0HQGOoKRoKm0qTIs7yfBwJyAdyf4esBRbPMCfyKWsLygtc-7Wi2VQIJxgpIhxXFYjiYD4k+hgbmSW5UjSo5ySyF4uVes68gKQr2mKEqFo8MqYHKPpKvaqrqqF2ocBAagwGgEDMMaCLeeyvkmVQMIpR2XaOQBZQetISWqAAcmlwYIsGobhmgyGQjGAnoTAibxrh6aqJmUzgbm+ZVNVy6wGqh4wIecjxcWGWJKo1Y0Vl05ctGbo2q2oUwHNYAaMVznZZyFTlEgABmli1PoHxrGOWkoApV1KZOPmWn5FRwKkp1IFA4wwHpXwwKd0AzZMEXykqACSaAgNAcLgDAl0rGsgPA55y1biVpmnbqBhrk5uUelDMN5ig4CI9dXz1SgYbSc1qFtWACbYd1nJ9dmgyDdAVRE7DpPMOTSmLbWfavblMIWFEePciLlI7nIKD3kepHnjLq0lDei6eqej4bU26kFtZgqxKoP6YPrmPlHceF3ZRxGDKR5FVoRiHVqpa3wAzmHMwM1thQRRGvA7sFOwHJw1rRnjeH4gReCg6DsZxvjMDxWQ5JgaHMATBbVNIgZsYGTSBm07SSao0l9EHcF0y+RkeuZ9hJ0MldVuBgxUWbteQutpkwPXSfHqejtoA5usHStFQcCg3A5EPA-QcHw-o756u8jIU80oYiuOpFio6oPC9GBVKVpdtJqJEvovd3ltrYwguP7Vn3O7FxeCWIrQ9UzTEZu61+SMx1Tguo+x6mzAaeYuZa3nnBQ+yVUrpTPmAIWtFVYW3yhLMQD8Sj6yqOgju4IULu2EjAfoKlSotRKBnL2Thw50SjoxOES42IIhgAAcRlFyFOfF04MyEmQkSLCC7F3sDKCu+8q5uywZ3MyCIvQIDYemJuYiW4u0MvgruzZr6bWQIkWR8i1BzzIgvEeLZpaXiOr3GRnY9GqAMUPZ6h0Zwr2kToqx7DbFGIvjOMWtp4AQE7HzaSAAePRWh5BFClu7bBvj-HgCCSE7Q4TzaEL4f0OYwj0zZmqEMdJKAIbSGzAARgwkmeMfxeIjnwv1NufwIigAVPaZkGxXg5MqjKGYIIYAtFIdQAhv84zVCwk4NoqTBg5IWiJbJMo8mFOKaUuY5SUANNAu0mpCA6lLO0tUtJMpWmTHaUcTp1FayRwYoEbAPgoDYG4PAYchg9EZFTvxP+vCenZwaM0IRIixhKJkgMFpMpulRkkWoqocsch6MUVA5REFtmTGmaHPBb4XmaNBbcvR7i4J-ByfCl2xjNGmJepSGAVBdRIFCQrH5GL0D2JWteVeX0hRb1XCg5JpkmXyAiSSMxRLEpqD0bVRISJsXSBpRjN6DKlzCrBlFGAwqYCCkLDATxq1vGtmFVDEUETH6sKmdIDVBhP6NWrn09qABWb2aZWZZjAUNHVcLpDyq5LqUsBp5VpBGEg5VqCfF6LyQoEl+xdDcE5TIblA4YBgpQOi9+C9FjCtFcvHkqLdyGDQCgdIsriyhOAF61lKLM32q1cCt8VJdWIuMtqyZ9r2ZIR-hQz2xDRm6prZ6k50cAiWCnuZDNAApCACr7kBFqSABU3DnnarqLScS7QcmiKhb8-oVzgCdqgB9cyUA426sBQQ4tEIqgACt+1oAhfbH5LasXNoDrpNZK7agQDLo1FtqikWqoPUe9Fzc0CLFhbk-JV6lg3soHeh90kW14p7Fywl4aSXIHJTGuCVLF4sqcYDQUd5tbMrDd61s7Kc37Ugw48NCRgPZGkvUbA6AhW6oUve0jqQE2vScRKgtv7pW72wLRxqMAIDKD8KgJcubX0kcavUXj2B+NFo0R6YTZGxP8cNbTOtHs-5VHNThYBVr+o5nAQWGTqRHXcbkygdYRzkFYbzflPTaB-WAiDRg0eBHx4wEPYKaNPzN32poyB+juaUO+iXEuldk0IAsZ1MwDj3mmostfXalAOsTGRKkbFx0STK1Nurf+9LKArNPuNfWlTjbhXnp-TlhFS022MS8Muj2PpYDAGwFcwgyR9OcLThnZFHoc55wLkXDoxhq67o9CAbgeBZFInxEkq++Vhu1bG+B9cjnXIRpG1AObvmk1r2noYZ1MA4ZrDNOZ19JstySewcd-s5belX1uOdzkeXlNxhqIMloNCgA

### editable

https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEyPYAFmA6CD6GAEoo9kiqFnJIEGiBAO4hSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAERdlGjAALYogxWDMIMANHO46unQHNOzC3Mo48BICBtzAL6YwuUwxazsXJRVQyNQY5OHW4PLqqtQ6zNzi4M7ewOP0GJzYnG4sEuZ1EVSgcQSOSgAApYvFEpRYgBHKKJACUpzKoguJVk8iUKnUVXsKDAAFVukiHk8UPjSYplGpVMSjDoKgAxJCcGD0yhsmA6SwwJkTMQ6OHAADWwu6MHS2RCUu6zJgwAQ8o4kpQAA8ERo2eTORcoYSVFURVA2QSRCorSUzrcYAo9ShgAamor0ABRI0qbAEVJO84FS55cxVAAsTgAzH0hpN1MBqdM5oGoN5KprRjKdd7fZL5Ar0CCzJxMOaOepXaVnSgqmgfAgEJGiZd6xTVBUQPKcvbGd02aztBbG5djBUFBwOMrRdpuy7e1OGwOhz6cgofGAQkjgAeQhO65v+9y5wul-vD47oevLmCbgXUQiMWoO1hXxCm+6Bb3FqMqzFUmx-Ceh5NBAlZoGBczHJGlBNrGGBVAATE4TipsMIHPDA4G-HMUEhDBcEIZsJzoBwpheL4-gBNA7DUjAAAyEDxGkASZNkuTIOY3KAVUdSNK0HQGOoKRoKm0qTIs7yfBwJyAdyf4esBRbPMCfyKWsLygtc-7Wi2VQIJxgpIhxXFYjiYD4k+hgbmSW5UjSo5ySyF4uVes68gKQr2mKEqFo8MqYHKPpKvaqrqqF2ocBAagwGgEDMMaCLeeyvkmVQMIpR2XaOQBZQetISWqAAcmlwYIsGobhmgyGQjGAnoTAibxrh6aqJmUzgbm+ZVNVy6wGqh4wIecjxcWGWJKo1Y0Vl05ctGbo2q2oUwHNYAaMVznZZyFTlEgABmli1PoHxrGOWkoApV1KZOPmWn5FRwKkp1IFA4wwHpXwwKd0AzZMEXykqACSaAgNAcLgDAl0rGsgPA55y1biVpmnbqBhrk5uUelDMN5ig4CI9dXz1SgYbSc1qFtWACbYd1nJ9dmgyDdAVRE7DpPMOTSmLbWfavblMIWFEePciLlI7nIKD3kepHnjLq0lDei6eqej4bU26kFtZgqxKoP6YPrmPlHceF3ZRxGDKR5FVoRiHVqpa3wAzmHMwM1thQRRGvA7sFOwHJw1rRnjeH4gReCg6DsZxvjMDxWQ5JgaHMATBbVNIgZsYGTSBm07SSao0l9EHcF0y+RkeuZ9hJ0MldVuBgxUWbteQutpkwPXSfHqejtoA5usHStFQcCg3A5EPA-QcHw-o756u8jIU80oYiuOpFio6oPC9GBVKVpdtJqJEvovd3ltrYwguP7Vn3O7FxeCWIrQ9UzTEZu61+SMx1Tguo+x6mzAaeYuZa3nnBQ+yVUrpTPmAIWtFVYW3yhLMQD8Sj6yqOgju4IULu2EjAfoKlSotRKBnL2Thw50SjoxOES42IIhgAAcRlFyFOfF04MyEmQkSLCC7F3sDKCu+8q5uywZ3MyCIvQIDYemJuYiW4u0MvgruzZr6bWQIkWR8i1BzzIgvEeLZpaXiOr3GRnY9GqAMUPZ6h0Zwr2kToqx7DbFGIvjOMWtp4AQE7HzaSAAePRWh5BFClu7bBvj-HgCCSE7Q4TzaEL4f0OYwj0zZmqEMdJKAIbSGzAARgwkmeMfxeIjnwv1NufwIigAVPaZkGxXg5MqjKGYIIYAtFIdQAhv84zVCwk4NoqTBg5IWiJbJMo8mFOKaUuY5SUANNAu0mpCA6lLO0tUtJMpWmTHaUcTp1FayRwYoEbAPgoDYG4PAYchg9EZFTvxP+vCenZwaM0IRIixhKJkgMFpMpulRkkWoqocsch6MUVA5REFtmTGmaHPBb4XmaNBbcvR7i4J-ByfCl2xjNGmJepSGAVBdRIFCQrH5GL0D2JWteVeX0hRb1XCg5JpkmXyAiSSMxRLEpqD0bVRISJsXSBpRjN6DKlzCrBlFGAwqYCCkLDATxq1vGtmFVDEUETH6sKmdIDVBhP6NWrn09qABWb2aZWZZjAUNHVcLpDyq5LqUsBp5VpBGEg5VqCfF6LyQoEl+xdDcE5TIblA4YBgpQOi9+C9FjCtFcvHkqLdyGDQCgdIsriyhOAF61lKLM32q1cCt8VJdWIuMtqyZ9r2ZIR-hQz2xDRm6prZ6k50cAiWCnuZDNAApCACr7kBFqSABU3DnnarqLScS7QcmiKhb8-oVzgCdqgB9cyUA426sBQQ4tEIqgACt+1oAhfbH5LasXNoDrpNZK7agQDLo1FtqikWqoPUe9Fzc0CLFhbk-JV6lg3soHeh90kW14p7Fywl4aSXIHJTGuCVLF4sqcYDQUd5tbMrDd61s7Kc37Ugw48NCRgPZGkvUbA6AhW6oUve0jqQE2vScRKgtv7pW72wLRxqMAIDKD8KgJcubX0kcavUXj2B+NFo0R6YTZGxP8cNbTOtHs-5VHNThYBVr+o5nAQWGTqRHXcbkygdYRzkFYbzflPTaB-WAiDRg0eBHx4wEPYKaNPzN32poyB+juaUO+iXEuldk0IAsZ1MwDj3mmostfXalAOsTGRKkbFx0STK1Nurf+9LKArNPuNfWlTjbhXnp-TlhFS022MS8Muj2PpYDAGwFcwgyR9OcLThnZFHoc55wLkXDoxhq67o9CAbgeBZFInxEkq++Vhu1bG+B9cjnXIRpG1AObvmk1r2noYZ1MA4ZrDNOZ19JstySewcd-s5belX1uOdzkeXlNxhqIMloNCgA

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the
  state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing
the moves of chess and finishes with sending game moves over the network between your client and server. You will start
each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project.
Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
|----------------------------|-------------------------------------------------|
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the
project, and one in each of the modules. The root POM defines any global dependencies and references the module POM
files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
