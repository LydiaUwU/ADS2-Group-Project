import java.util.*;

//TODO: Convert to K&R and put spaces between terms

//this is the main class for now

/**
 * Main class to handle user interactions.
 *
 * @Authors: Lydia MacBride, David King, Liam O Lionaird, Sarah Klein
 */


public class ADS2GroupProject {
    public static void main(String[] args) {
        // create graph with edges taken from stop_times.txt and transfers.txt

        String[] filenames = {"inputs/stops.txt", "inputs/stop_times.txt", "inputs/transfers.txt"};
        Graph stopsGraph = new Graph(filenames);

        boolean outerLoop = true;

        while (outerLoop) {
            System.out.println("Enter the number of your choice (or type \"quit\" to quit) and then press ENTER:\n" + "1: Shortest path between 2 bus stops\n" + "2: Search for bus stops by name\n" + "3: Search for trips with a given arrival time");
            Scanner inputScanner = new Scanner(System.in);

            String input = inputScanner.nextLine();

            switch (input) {
                case "quit":
                    outerLoop = false;
                    break;
                case "1": {
                    int sourceStop, destinationStop;

                    boolean innerLoop = true;
                    while (innerLoop) {
                        System.out.println("Enter ID of first stop (or type \"back\" to go back) and then press ENTER: ");
                        input = inputScanner.nextLine();
                        if (input.equals("back")) {
                            innerLoop = false;
                        } else {
                            try {
                                sourceStop = Integer.parseInt(input);

                                // If 1st node exists
                                if (stopsGraph.getNodeIndexFromLabel(sourceStop) >= 0) {
                                    System.out.println("Enter ID of second stop (or type \"back\" to go back) and then press ENTER: ");
                                    input = inputScanner.nextLine();

                                    if (input.equals("back")) {
                                        innerLoop = false;
                                    } else {
                                        try {
                                            destinationStop = Integer.parseInt(input);

                                            //If both stops are the same
                                            if (sourceStop == destinationStop) {
                                                System.out.println("Source stop and destination stop are the same.");
                                            }
                                            // If 2nd node exists
                                            else if (stopsGraph.getNodeIndexFromLabel(destinationStop) >= 0) {

                                                PathDijkstra stopsPath = new PathDijkstra(stopsGraph, sourceStop, destinationStop);
                                                ArrayList<Edge> shortestPath;

                                                if (stopsPath.getShortestPath() != null) {
                                                    double totalCost = 0;

                                                    shortestPath = stopsPath.getShortestPath();

                                                    for (int i = shortestPath.size() - 1; i >= 0; i--) {
                                                        System.out.println((shortestPath.size() - i - 1) + ":\t" + shortestPath.get(i).src.name);
                                                        totalCost += shortestPath.get(i).weight;
                                                    }

                                                    System.out.println(shortestPath.size() - 1 + ":\t" + shortestPath.get(0).dst.name);

                                                    totalCost = Math.round(totalCost * 100) / 100;

                                                    System.out.println("Cost: " + totalCost);

                                                } else {
                                                    System.out.println("No path found.");
                                                }
                                            } else {
                                                System.out.println("No stop by that ID found.");
                                            }
                                        }
                                        catch (NumberFormatException e) {
                                            System.out.println("No stop by that ID found.");
                                        }
                                    }
                                } else {
                                    System.out.println("No stop by that ID found.");
                                }
                            }
                            catch (NumberFormatException e) {
                                System.out.println("No stop by that ID found.");
                            }
                        }
                    }
                    break;
                }
                case "2": {
                    boolean innerLoop = true;
                    while (innerLoop) {
                        System.out.println("Type the start of a stop name (or type \"back\" to go back) and then press ENTER: ");
                        try {
                            inputScanner = new Scanner(System.in);

                            //TODO check edge cases

                            input = inputScanner.nextLine();

                            if (input.equals("back")) {
                                innerLoop = false;
                            } else {
                                stopsGraph.stops.getSubtree(stopsGraph.stops.getNodeFromKey(input.toUpperCase()), input.toUpperCase(), true);
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            System.out.println("Nothing entered.");
                        }
                    }
                    break;
                }
                case "3": {
                    boolean innerLoop = true;
                    while (innerLoop) {
                        System.out.println("Enter a stop time in the format hh:mm:ss (or type \"back\" to go back) and then press ENTER: ");
                        inputScanner = new Scanner(System.in);

                        input = inputScanner.next();

                        if (input.equals("back")) {
                            innerLoop = false;
                        } else {
                            stopsGraph.getTripsFromArrivalTime(input);
                        }
                    }
                    break;
                }
                default:
                    System.out.println("Not a valid option.");
                    break;
            }
        }
    }
}
