# Fantastic RPG

Simple role play game for command line interface written in vanilla Java.
Third party tools/frameworks used: Maven for dependency management and building, Mockito/JUnit for testing.

## Prerequisites

* Java JDK 8
* Maven 3

## Build & run 

* Run `mvn clean install` from project's root directory to build project. Tests will run.
* Run `java -jar target/fantastic-rpg-1.0.0-jar-with-dependencies.jar` to start game.
 
Disclaimer: This has been built and run only on Debian Jessy.
 
## Game instructions

Explore the world by moving on 2D tile set map, using NESW world directions. 
Moving onto a field will make surrounding fields visible. 
Trying to move on field occupied by a monster, you will face a choice to fight it or flee. 
You may fight only monsters you are up to by experience - this way, progressing through game is forced.
You have only one life, so choose wisely.

## Improvements

Some of the improvements that should be done are:

* Build and execution may be platform dependent. 
* Add more player customizing.
* Implement logging framework.
* Implement file resource based configuration for various hard-coded preferences.
* Prepare game for AI.
* Add more topic & story flexibility.

## Extend & contribute instructions

Make sure build command is successful, ie. no tests are failing. Add tests for every new feature introduced.

Some implementation hints:

* `com.github.maricn.fantasticrpg.io` package contains input/output which could be extended to use something else but console.
* Models in `com.github.maricn.fantasticrpg.model` package are extensible. Different abilities and ways of acquiring them could be implemented. Based on player abilities, some game features could be accessible or not. Also, different kinds of monsters, with different characteristics could be added to game. Changing monster types could effect impression of topic. Adding different map field types to models and map factory could also contribute to game's uniqueness.
* Different repositories could be implemented, for example, using databases.
* Other `ActionCommands` could be implemented, which could reflect different attack types, for instance - spell attack, which could be based on `Player`'s and other character's extension related to this type of attack.
* Current implementation is generating levels 1-30; `MapFactory` algorithm is based on provided level number. This could be improved.

## License

This project is distributed under Apache2 license, as stated in file `LICENSE`.
