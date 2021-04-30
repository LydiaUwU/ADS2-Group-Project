import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.Collections;
import java.util.Arrays;

//TODO: Remove unused code

/**
 * Custom class to generate and store a graph based on a .txt file as specified in the assignment spec.
 *
 * @Authors: Lydia MacBride, David King
 */

public class Graph {
    Node[] nodes;
    ArrayList<Edge> edges;
    ArrayList<Transfer> transfers = new ArrayList<>();
    TST stops = new TST();

    /**
     * @param filenames: Filename of .txt file to use to generate the graph.
     */

    // stops.txt, stop_times.txt, transfers.txt# (is this overkill? can the filenames just be given to the scanners directly?)
    Graph(String[] filenames) {
        if (filenames != null) {
            edges = new ArrayList<>();

            // Read file and use data to build graph
            try {
                // --------------- STOPS.TXT ---------------

                // getting number of stops by seeing how many lines are in stops.txt - make more efficient also adapt this part to create a ternary search tree
                int numberOfStops = 0;
                ArrayList<Integer> stopNumbers = new ArrayList<>();

                //this is to allow stop ids to be converted to stop names
                ArrayList<String> stopNames = new ArrayList<>();

                Scanner stopsScanner = new Scanner(new File(filenames[0]));

                //skip first line of stops.txt as it is just the variable names
                stopsScanner.nextLine();

                // getting stops info (adding to ArrayList stopNumbers, which will be then used to create the labels for the nodes
                while (stopsScanner.hasNextLine()) {
                    String[] currentLine = stopsScanner.nextLine().trim().split(",");

                    //adding current stop to stopNumbers
                    stopNumbers.add(Integer.parseInt(currentLine[0]));

                    //adding current stop to stopNames to allow stop id to stop name conversion
                    String stopName = parseStopName(currentLine[2]);
                    stopNames.add(stopName);

                    numberOfStops++;

                    //make stop names more usable by putting some words at the end

                    //adding current stop to stops TST
                    ArrayList<String> stopInfo = new ArrayList<>();

                    stopInfo.add(currentLine[3]); //description
                    stopInfo.add(currentLine[0]); //id
                    stopInfo.add(currentLine[1]); //code
                    stopInfo.add(currentLine[4]); //lat
                    stopInfo.add(currentLine[5]); //lon
                    stopInfo.add(currentLine[6]); //zone id
                    stopInfo.add(currentLine[7]); //url
                    stopInfo.add(currentLine[8]); //location type
                    if (currentLine.length > 9) {
                        stopInfo.add(currentLine[9]); //parent station
                    } else {
                        stopInfo.add("N/A");
                    }

                    //add current stop to TST, with the stop id as key, and give the other stop info too
                    stops.put(stopName, stopInfo);
                }

                //set size of nodes array to the number of stops
                nodes = new Node[numberOfStops];

                //set each element of nodes to be a node and then set its label to the correct stop number
                for (int i = 0; i < numberOfStops; i++) {
                    nodes[i] = new Node(i);
                    nodes[i].label = stopNumbers.get(i);
                    nodes[i].name = stopNames.get(i);
                }

                //sort stops by stop number, this will allow transfers to be added to the graph quicker
                Arrays.sort(nodes, Node::compareTo);

                // --------------- TRANSFERS.TXT ---------------

                //open transfers.txt
                Scanner transferScanner = new Scanner(new File(filenames[2]));

                //skip first line of transfers.txt as this is just the variable names
                transferScanner.nextLine();

                //reading lines from transfers.txt and adding them to transfers ArrayList
                while (transferScanner.hasNextLine()) {
                    String[] currentLine = transferScanner.nextLine().trim().split(",");

                    //if no value for minTransferTime is provided, set it to 0 for simplicity
                    int time = 0;
                    if (currentLine.length == 4) {
                        time = Integer.parseInt(currentLine[3]);
                    }

                    //add current transfer to transfers
                    transfers.add(new Transfer(Integer.parseInt(currentLine[0]),
                            Integer.parseInt(currentLine[1]), Integer.parseInt(currentLine[2]), time, -1, ""));
                }

                // --------------- STOP_TIMES.TXT ---------------

                //open stop_times.txt
                Scanner stopTimesScanner = new Scanner(new File(filenames[1]));

                //set previous line info to -1, so line 1 will never match with line 0 (which is the variable names)
                int previousTripId = -1;
                int previousStopId = -1;

                //skip first line of stop_times.txt as this is just the variable names
                stopTimesScanner.nextLine();

                /*
                  reading lines from stop_times.txt and adding them to transfers ArrayList.
                  this is done by keeping the previous line's info in memory, and comparing it
                  to the current line. if both have the same trip id, a transfer is added to transfers.
                  note that the same transfer may occur many times during the day, so transfers has
                  a lot of duplicates (time information is not currently stored yet).
                 */


                while (stopTimesScanner.hasNextLine()) {
                    String[] currentLine = stopTimesScanner.nextLine().trim().split(",");

                    //read current line info
                    int currentTripId = Integer.parseInt(currentLine[0]);
                    int currentStopId = Integer.parseInt(currentLine[3]);

                    if (currentTripId == previousTripId) {
                        //add current transfer to transfers
                        transfers.add(new Transfer(previousStopId, currentStopId, 1, 0, currentTripId, currentLine[1].trim()));
                    }
                    previousTripId = currentTripId;
                    previousStopId = currentStopId;
                }

                //ADDING TRANSFERS TO GRAPH

                /* sort transfers by toStopIds, then by fromStopIds. this makes sure that all duplicates are later
                   removed (because simply sorting by fromStopIds leaves the toStopIds unsorted)*/
                Collections.sort(transfers, Transfer::compareTo);
                Collections.sort(transfers, Transfer::compareFrom);

                /* i starts at the lowest numbered node and j starts at the lowest numbered transfer.
                   find the first transfer from node i, and then add all other transfers from node i
                   in sequence. j will now point to the first transfer from the next node. repeat these
                   steps for the next node */

                //start at lowest numbered transfer (according to fromStopId)
                int j = 0;
                boolean skipRestOfNodes = false;

                //System.out.println("nodes.length = "+nodes.length);
                //for each node find transfers from it
                for (Node node : nodes) {
                    //System.out.println("checking node "+i);
                    int previousToStopId = -1;

                    //if (i%2000==0) System.out.println(i);
                    //if all transfers have been checked there is nothing left to compare
                    if (skipRestOfNodes) {
                        break;
                    }

                    boolean allTransfersFound = false;

                    while (!allTransfersFound) {
                        //if all transfers have been checked there is nothing left to compare
                        if (j >= transfers.size()) {
                            allTransfersFound = true;
                            skipRestOfNodes = true;
                            break;
                        }

                        /* if current transfer's fromStopId and current node label are the same,
                           and the transfer isn't a duplicate, add the transfer
                           as a node to the edge, and add the edge to edges */

                        else if (transfers.get(j).fromStopId == node.label) {
                            if (transfers.get(j).toStopId != previousToStopId) {
                                /* calculate weight of edge (if transferType is 0, then cost is 2,
                                   if transferType is 2 then cost is minTransferTime/100 */
                                float weight = 0.0f;

                                //cost is 2 if transfer type is 0 (immediate transfer possible, from transfers.txt)
                                if (transfers.get(j).transferType == 0) {
                                    weight = 2.0f;
                                }
                                //cost is 1 if transfer type is 1 (from stop_times.txt)
                                else if (transfers.get(j).transferType == 1) {
                                    weight = 1.0f;
                                }
                                //cost is minimum transfer time/100 if transfer type is 2 (from transfers.txt)
                                else if (transfers.get(j).transferType == 2) {
                                    weight = ((float) transfers.get(j).minTransferTime / 100);
                                } else {
                                    System.out.println("invalid transfer type" + transfers.get(j).transferType);
                                }

                                //need to loop through nodes to get node corresponding to toStopId

                                int toStopId = 0;
                                for (int k = 0; k < nodes.length; k++) {
                                    if (nodes[k].label == transfers.get(j).toStopId) {
                                        toStopId = k;
                                    }
                                }


                                //add edge to nodes and edges
                                Edge e = new Edge(node, nodes[toStopId], weight);

                                node.addEdge(e);
                                edges.add(e);

                                previousToStopId = transfers.get(j).toStopId;
                            }
                            //go to next transfer and repeat checks in the next iteration of the loop
                            j++;
                        }

                        //go to next transfer and repeat checks in the next iteration of the loop
                        else if (transfers.get(j).fromStopId < node.label) {
                            j++;
                        }

                        /* if the current transfer number is higher than the current node number then
                          all transfers have been added, or there were no transfers from the current node */
                        else if (transfers.get(j).fromStopId > node.label) {
                            allTransfersFound = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Given a node label (i.e. its id), return the position of that node in nodes[].
     *
     * @param label: The label of the node to input.
     * @return int: The index of the input label.
     * @Authors: David King
     */

    public int getNodeIndexFromLabel(int label) {
        int index = 0;
        boolean indexFound = false;
        while (!indexFound && (index < this.nodes.length)) {
            if (this.nodes[index].label == label) {
                indexFound = true;
            } else {
                index++;
            }
        }

        if (index == this.nodes.length) {
            return -1;
        } else {
            return index;
        }
    }

    /**
     * Output the information for a provided trip
     *
     * @param arrivalTime: Arrival time that is found at some point in the trip.
     * @Authors: Lydia MacBride, David King
     */

    public void getTripsFromArrivalTime(String arrivalTime) {
        // Check for invalid arrival time
        Scanner scanner = new Scanner(arrivalTime).useDelimiter(":");
        if (scanner.nextInt() > 23 || scanner.nextInt() > 59 || scanner.nextInt() > 59) {
            System.out.println("Invalid arrival time provided.");
            return;
        }
        scanner.close();

        System.out.println("Showing results for trips with arrival time " + arrivalTime + ".");

        transfers.sort(Transfer::compareId);

        int transfersFound = 0;
        for (Transfer transfer : transfers) {
            if (transfer.arrivalTime.equals(arrivalTime)) {
                System.out.println("Trip " + transfer.tripId +
                        ", From " + transfer.fromStopId +
                        ", To " + transfer.toStopId +
                        ", Type " + transfer.transferType);
                transfersFound++;
            }
        }

        if (transfersFound >= 1) {
            System.out.println(transfersFound + ((transfersFound == 1) ? " transfer" : " transfers") + " found.");
        } else {
            System.out.println("No transfers fitting search parameters found.");
        }
    }

    /**
     * Allow more useful searching of stop names by moving certain words to the end
     *
     * @param input: The stop name as found in stops.txt.
     * @return String: The parsed stop name.
     * @Authors: David King
     */

    public String parseStopName(String input) {
        String output = input;
        if (output.substring(0, 8).equals("FLAGSTOP")) {
            output = output.substring(9) + " " + output.substring(0, 8);
        }

        if (output.substring(0, 2).equals("WB") ||
                output.substring(0, 2).equals("NB") ||
                output.substring(0, 2).equals("SB") ||
                output.substring(0, 2).equals("EB")) {
            output = output.substring(3) + " " + output.substring(0, 2);
        }

        return output;
    }

}

/**
 * Custom class that stores information about a path
 *
 * @Authors: Lydia MacBride
 */
class Path {
    Node src, dst;
    Double distance;

    Path() {
        distance = -1.0;
    }

    /**
     * @param src: The start of the path segment.
     * @param dst: The end of the path segment.
     * @param distance: The length of the path segment.
     *
     */
    Path(Node src, Node dst, Double distance) {
        this.src = src;
        this.dst = dst;
        this.distance = distance;
    }
}

/**
 * Custom class that stores information about a Node
 *
 * @Authors: Lydia MacBride, David King
 */
class Node {
    int label;
    ArrayList<Edge> edges;

    //used to allow stop id to stop name conversion
    String name;

    /**
     * @param label: Integer label of the Node
     */
    Node(int label) {
        this.label = label;
        edges = new ArrayList<>();
    }

    /**
     * @param edge: Edge to add to the Node as traversable
     */
    void addEdge(Edge edge) {
        edges.add(edge);
    }

    /**
     * @return Node[]: Array of neighbouring Nodes
     */
    Node[] getNeighbours() {
        Node[] output = new Node[edges.size()];

        for (int i = 0; i < edges.size(); i++) {
            output[i] = edges.get(i).dst;
        }

        return output;
    }

    /**
     * @param dst: Destination Node
     * @return double: Either the length of the edge to dst Node or -1 if dst is not a neighbour
     */
    double length(Node dst) {
        for (Edge i : edges) {
            if (i.dst == dst) {
                return i.weight;
            }
        }

        return -1;
    }

    /**
        @param o: The node to compare to.
    */
    public int compareTo(Node o) {
        return this.label - o.label;
    }
}

/**
 * Custom class that stores information about an edge
 *
 * @Authors: Lydia MacBride
 */
class Edge {
    Node src, dst;
    double weight;

    /**
     * @param src:    Source Node
     * @param dst:    Destination Node
     * @param weight: Weight of the Edge
     */
    Edge(Node src, Node dst, double weight) {
        this.src = src;
        this.dst = dst;
        this.weight = weight;
    }

    public String toString() {
        return "(" + this.src.label + "," + this.dst.label + "," + this.weight + ")";
    }
}

/*
                                                   -#=
                                   :-=======-.    ++:%
                         #+*-  .==-:            .#-  %.              :
                        :#  =*:                -#.   *=             %@++*+
                        =+    ++         ::   +=     -#   :      =*%@@@#-
                        +=            . :*  :+.       @.  -*-     .%%*+:
                        #-   .       ===+             ..    -#=    -
                      .#+.  :+    -++-*-                .     :**:
                     .#.   =*.:=*+-:*+                  .-===++=+=:.
                     *:  -##++-. -*=         :-++++=.            .:*@#-
                      :+*-    -++:         .=:.                 :++-
                  :=**=.        .::-        :-=++*++++:
               =%@+:        ====----.     .=-.. .#    :         :====++=
    .=: .=.      :+**-.      -+**%=-.     .:     #=-***:        .====:.:#-
    -@@%@#.      :-  -+*-  ==:   #.  +=    -=++++*%-=+       -==-        +#:
   :#@@@%%:     :*  +*-.:   .:   .#%#-            =         :=-.          .*#:
   .:%@:       :#    -++:    %*=%*++  :+#@#               :#  -*%%*+-       .+#=.
    .*:       -*        -+.  :  :.    :*@#               -%.:=-:.   .          :**-.
             ++          +:             ::.:#.          +#.                      .##.
           .#- =:      .*. --       .--++--:          :%+       ::             :+#:
          =*:+%%      -@*=  :+:       .             .*@#         #::*       :+#+.
        :%#**+#.     :+:      :+=                 :*@%:=*        :@--#    =*=:
        +=:.*+                  .=+=:          :+%@%-   :-        =%..=
          :%-                  .   .-=++++++*%@@@%-       -=.:==+++.
            -+*=:             +=  +:        -@@@#.       *: +*:
               .-+++=-.       @: .@         :%%-       -*:    =*-
                    .:=+*+++-:*  :+       =#+:*+---=+**-        =#-
                                          :     :-::              =#-
                                                                    +#-
                                                                      :
 */

