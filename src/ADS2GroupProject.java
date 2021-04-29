import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

//this is the main class for now
//sample line that was changed
public class ADS2GroupProject {
    public static void main(String[] args) {
        // create graph with edges taken from stop_times.txt and transfers.txt

        String[] filenames = {"inputs/stops.txt","inputs/stop_times.txt","inputs/transfers.txt"};
        Graph stopsGraph = new Graph(filenames);
        
        //print out all edges in the graph, sorted by node (14975 edges, 8757 nodes total)
        /*for (int i=0;i<stopsGraph.nodes.length;i++)
        {
            for (int j=0;j<stopsGraph.nodes[i].edges.size();j++)
            {
                System.out.println(stopsGraph.nodes[i].edges.get(j).toString());
                edgeCounter++;
            }
        }*/

        //1477, 1866 no path
        //1817, 1819 path (1817,1818,1819)
        /*646, 1278 should work but doesn't (heading towards the start, it takes another direction and says that
          one of the edges is null when shouldn't be*/
        PathDijkstra stopsPath1 = new PathDijkstra(stopsGraph, 841, 842);
        ArrayList<Edge> shortestPath1;

        if (stopsPath1.getShortestPath()!=null)
        {
            shortestPath1 = stopsPath1.getShortestPath();

            for (int i=0;i<shortestPath1.size();i++)
            {
                System.out.println(shortestPath1.get(i).toString());
            }
        }

        else System.out.println("no path");

        for (int i=0;i<stopsGraph.nodes[646].edges.size();i++)
            System.out.println(stopsGraph.nodes[646].edges.get(i).toString());

        //testing stop info
        //stopsGraph.stops.getSubtree();

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
                boolean innerLoop = true;
                while (innerLoop)
                {
                    System.out.println("Enter ID of first stop (or type \"quit\" to quit)");
                    input = inputScanner.next();
                    if (input.equals("quit")) innerLoop = false;
                    else
                    {

                        // If node/stop/whatever exists
                        if (true)
                        {

                            System.out.println("Enter ID of second stop (or type \"quit\" to quit)");
                            input = inputScanner.next();
                            if (input.equals("quit")) innerLoop = false;
                            else
                            {
                                // If 2nd node/stop/whatever exists
                                if (true)
                                {
                                    // Do the thing, print path.
                                    System.out.println("Put shortest path here");
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
                while (innerLoop)
                {
                    System.out.println("Type the start of a stop name (or type \"quit\" to quit) and then press ENTER: ");
                    inputScanner = new Scanner(System.in);

                    input = inputScanner.next();

                    if (input.equals("quit")) innerLoop = false;
                    else
                    {
                        stopsGraph.stops.getSubtree(stopsGraph.stops.getNodeFromKey(input.toUpperCase()), input.toUpperCase(), true);
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
