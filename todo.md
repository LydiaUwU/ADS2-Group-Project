# Things in italics are things that can be done right now.
## Functionality
### Part 1 (shortest path between stops):
#### Basic functionality (10%):
*Algorithm works partially but gives errors on some simple paths (look at PathDijkstra.java and Graph.java). See the console input before the main menu for debug info.*
#### Edge cases (10%):
Work on basic functionality before doing this.

### Part 2 (search for stops by name):
#### Basic functionality (10%):
Works correctly.
#### Edge cases (10%):
*As far as I can tell, most edge cases are covered, but it only checks the first word of the input (use a different way of getting input from the user that gets all words entered). Edit code for user input after where it says "Type the start of a stop name (or type \"quit\" to quit) and then press ENTER: " in ADS2GroupProject.java.*


### Part 3 (searching for trips with a given arrival time):
#### Basic functionality (10%):
*Currently returns a list of trip IDs, but need to add all the info for the trip (i.e. each stop). Add code to get info of all stops which have a trip ID in this list in getTripsFromArrivalTime() in Graph.java.* Check that input time is valid (<=23:59:59).
#### Edge cases (10%):
Work on basic functionality before doing this.


## Interface (10%)
*The interface works for selecting parts 2 and 3, but part 1 just prints "Put shortest path here" currently. Part 1 of the interface should let the user input 2 stop IDs (not names as far as I can tell, plus it's easier), which just involves getting user input and then creating a PathDijkstra object: PathDijkstra stopsPath = new PathDijkstra(stopsGraph, firstStop, secondStop). This can be done even if  part 1 doesn't actually work right yet.*

## Demo (10%)
Work on parts 1-3 before doing this.

## 2 page design document (20%)
* Not made yet - work on parts 1-3 before doing this.
* Part 1 uses Dijkstra's algorithm, part 2 uses TST, all parts use arrayLists etc, check the code of each part to see what are the main data structures used.*

## Readme.txt
Work on parts 1-3 before doing this.
