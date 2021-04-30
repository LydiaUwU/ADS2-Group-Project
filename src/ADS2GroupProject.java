import java.util.*;

//TODO: Convert to K&R and put spaces between terms

//this is the main class for now
//sample line that was changed
public class ADS2GroupProject {
    public static void main(String[] args) {
        // create graph with edges taken from stop_times.txt and transfers.txt

        String[] filenames = {"inputs/stops.txt","inputs/stop_times.txt","inputs/transfers.txt"};
        Graph stopsGraph = new Graph(filenames);

        boolean outerLoop = true;

        while (outerLoop)
        {
            System.out.println("Enter the number of your choice (or type \"quit\" to quit):\n" +
                    "1: Shortest path between 2 bus stops\n" +
                    "2: Search for bus stops by name\n" +
                    "3: Search for trips with a given arrival time");
            Scanner inputScanner = new Scanner(System.in);

            String input = inputScanner.next();

            if (input.equals("quit")) outerLoop = false;
            else if (input.equals("1"))
            {
                int sourceStop, destinationStop;

                boolean innerLoop = true;
                while (innerLoop)
                {
                    //TODO check edge cases

                    System.out.println("Enter ID of first stop (or type \"quit\" to quit)");
                    input = inputScanner.next();
                    if (input.equals("quit")) innerLoop = false;
                    else
                    {
                        sourceStop = Integer.parseInt(input);
                        // If node/stop/whatever exists
                        if (stopsGraph.getNodeIndexFromLabel(sourceStop)>=0)
                        {

                            System.out.println("Enter ID of second stop (or type \"quit\" to quit)");
                            input = inputScanner.next();
                            if (input.equals("quit")) innerLoop = false;
                            else
                            {
                                destinationStop = Integer.parseInt(input);

                                // If 2nd node/stop/whatever exists
                                if (stopsGraph.getNodeIndexFromLabel(destinationStop)>=0)
                                {
                                    // Do the thing, print path.
                                    PathDijkstra stopsPath = new PathDijkstra(stopsGraph, sourceStop, destinationStop);
                                    ArrayList<Edge> shortestPath;

                                    if (stopsPath.getShortestPath()!=null)
                                    {
                                        double totalCost = 0;

                                        shortestPath = stopsPath.getShortestPath();

                                        System.out.println("1:\t" + shortestPath.get(0).dst.name);
                                        for (int i=0;i<shortestPath.size();i++)
                                        {
                                            System.out.println((i+2) + ":\t" + shortestPath.get(i).src.name);
                                            totalCost += shortestPath.get(i).weight;
                                        }

                                        System.out.println("Cost: " + totalCost);

                                    }

                                    else System.out.println("No path found.");
                                }
                                else
                                {
                                    System.out.println("No stop by that ID found.");

                                }

                            }
                        }
                        else
                        {
                            System.out.println("No stop by that ID found.");
                        }


                    }
                }
            }
            else if (input.equals("2"))
            {
                boolean innerLoop = true;
                while (innerLoop) {
                    System.out.println("Type the start of a stop name (or type \"quit\" to quit) and then press ENTER: ");
                    try {
                        inputScanner = new Scanner(System.in);

                        //TODO check edge cases

                        input = inputScanner.nextLine();

                        if (input.equals("quit")) innerLoop = false;
                        else {
                            stopsGraph.stops.getSubtree(stopsGraph.stops.getNodeFromKey(input.toUpperCase()), input.toUpperCase(), true);
                        }
                    }
                    catch (StringIndexOutOfBoundsException e) {
                        System.out.println("Nothing entered.");
                    }
                }
            }
            else if (input.equals("3"))
           {
               boolean innerLoop = true;
               while (innerLoop)
               {
                   System.out.println("Enter a stop time in the format hh:mm:ss (or type \"quit\" to quit) and then press ENTER: ");
                   inputScanner = new Scanner(System.in);

                   input = inputScanner.next();

                   if (input.equals("quit")) innerLoop = false;
                   else {
                       stopsGraph.getTripsFromArrivalTime(input);
                   }
               }

           }
           else
           {
                System.out.println("Not a valid option.");
           }
        }
    }
}
