import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.Comparator;
import java.util.Collections;
import java.util.Arrays;

/**
 * Custom class to generate and store a graph based on a .txt file as specified in the assignment spec.
 *
 * @Author: Lydia MacBride
 */

public class Graph {
    Node[] nodes;
    ArrayList<Edge> edges;
    //not sure where to put this
    ArrayList<Transfer> transfers = new ArrayList<Transfer>();

    TST stops = new TST();

    /**
     * @param filenames: Filename of .txt file to use to generate the graph.
     */

    /* stops.txt, stop_times.txt, transfers.txt#
     (is this overkill? can the filenames just be given to the scanners directly?) /*/
    Graph(String[] filenames) {
        if (filenames != null) {
            edges = new ArrayList<>();

            // Read file and use data to build graph
            try {
                /*
                --------------- STOPS.TXT ---------------
                 */

                /* getting number of stops by seeing how many lines are in stops.txt - make more efficient
                   also adapt this part to create a ternary search tree */
                int numberOfStops = 0;
                ArrayList<Integer> stopNumbers = new ArrayList<>();

                Scanner stopsScanner = new Scanner(new File(filenames[0]));

                //skip first line of stops.txt as it is just the variable names
                stopsScanner.nextLine();

                /* getting stops info (adding to ArrayList stopNumbers, which will be then used to create the labels
                for the nodes */
                while(stopsScanner.hasNextLine())
                {
                    String[] currentLine = stopsScanner.nextLine().trim().split(",");

                    //adding current stop to stopNumbers
                    stopNumbers.add(Integer.parseInt(currentLine[0]));
                    numberOfStops++;

                    //make stop names more usable by putting some words at the end
                    String stopName = currentLine[2];

                    if (stopName.substring(0,8).equals("FLAGSTOP"))
                    {
                        stopName = stopName.substring(9) + " " + stopName.substring(0,8);
                    }

                    if (stopName.substring(0,2).equals("WB")||
                        stopName.substring(0,2).equals("NB")||
                        stopName.substring(0,2).equals("SB")||
                        stopName.substring(0,2).equals("EB"))
                    {
                        stopName = stopName.substring(3) + " " + stopName.substring(0,2);
                    }

                    //adding current stop to stops TST
                    ArrayList<String> stopInfo = new ArrayList<String>();

                    stopInfo.add(currentLine[3]); //description
                    stopInfo.add(currentLine[0]); //id
                    stopInfo.add(currentLine[1]); //code
                    stopInfo.add(currentLine[4]); //lat
                    stopInfo.add(currentLine[5]); //lon
                    stopInfo.add(currentLine[6]); //zone id
                    stopInfo.add(currentLine[7]); //url
                    stopInfo.add(currentLine[8]); //location type
                    if (currentLine.length>9) stopInfo.add(currentLine[9]); //parent station
                    else stopInfo.add("N/A");

                    //add current stop to TST, with the stop id as key, and give the other stop info too
                    stops.put(stopName, stopInfo);
                }

                //set size of nodes array to the number of stops
                nodes = new Node[numberOfStops];

                //set each element of nodes to be a node and then set its label to the correct stop number
                for (int i = 0; i < numberOfStops; i++) {
                    nodes[i] = new Node(i);
                    nodes[i].label = stopNumbers.get(i);
                }

                //sort stops by stop number, this will allow transfers to be added to the graph quicker
                Arrays.sort(nodes, Node::compareTo);

                //print out sorted array of nodes
                /*for (int i = 0; i < nodes.length; i++) {
                    System.out.println("nodes["+i+"] label = "+nodes[i].label);
                }*/

                /*
                --------------- TRANSFERS.TXT ---------------
                 */

                //open transfers.txt
                Scanner transferScanner = new Scanner(new File(filenames[2]));

                //skip first line of transfers.txt as this is just the variable names
                transferScanner.nextLine();

                //reading lines from transfers.txt and adding them to transfers ArrayList
                while(transferScanner.hasNextLine()) {
                    String[] currentLine = transferScanner.nextLine().trim().split(",");

                    //if no value for minTransferTime is provided, set it to 0 for simplicity
                    int time = 0;
                    if (currentLine.length==4) time = Integer.parseInt(currentLine[3]);

                    //add current transfer to transfers
                    transfers.add(new Transfer(Integer.parseInt(currentLine[0]),
                            Integer.parseInt(currentLine[1]),Integer.parseInt(currentLine[2]),time, -1, ""));
                }

                /*
                  --------------- STOP_TIMES.TXT ---------------
                */

                //open stop_times.txt
                Scanner stopTimesScanner = new Scanner(new File(filenames[1]));

                        /*set previous line info to -1, so line 1 will never match with line 0
                          (which is the variable names)*/
                int previousTripId = -1;
                int previousStopId = -1;

                //skip first line of stop_times.txt as this is just the variable names
                stopTimesScanner.nextLine();

                        /*reading lines from stop_times.txt and adding them to transfers ArrayList.
                          this is done by keeping the previous line's info in memory, and comparing it
                          to the current line. if both have the same trip id, a transfer is added to transfers.
                          note that the same transfer may occur many times during the day, so transfers has
                          a lot of duplicates (time information is not currently stored yet).
                         */


                while(stopTimesScanner.hasNextLine()) {
                    String[] currentLine = stopTimesScanner.nextLine().trim().split(",");

                    //read current line info
                    int currentTripId = Integer.parseInt(currentLine[0]);
                    int currentStopId = Integer.parseInt(currentLine[3]);

                    if (currentTripId==previousTripId)
                    {
                        //add current transfer to transfers
                        transfers.add(new Transfer(previousStopId, currentStopId,1,0, currentTripId, currentLine[1].trim()));
                    }
                    previousTripId = currentTripId;
                    previousStopId = currentStopId;
                }

                //ADDING TRANSFERS TO GRAPH

                /* sort transfers by toStopIds, then by fromStopIds. this makes sure that all duplicates are later
                   removed (because simply sorting by fromStopIds leaves the toStopIds unsorted)*/
                Collections.sort(transfers, Transfer::compareTo);
                Collections.sort(transfers, Transfer::compareFrom);

                //print all transfers - should be sorted by fromStopId
                //for (int i=0;i<transfers.size();i++) System.out.println("transfers["+i+"] = "+transfers.get(i).toString());

                /* i starts at the lowest numbered node and j starts at the lowest numbered transfer.
                   find the first transfer from node i, and then add all other transfers from node i
                   in sequence. j will now point to the first transfer from the next node. repeat these
                   steps for the next node */

                //start at lowest numbered transfer (according to fromStopId)
                int j=0;
                boolean skipRestOfNodes = false;

                //System.out.println("nodes.length = "+nodes.length);
                //for each node find transfers from it
                for (int i=0;i<nodes.length;i++)
                {
                    //System.out.println("checking node "+i);
                    int previousToStopId = -1;

                    //if (i%2000==0) System.out.println(i);
                    //if all transfers have been checked there is nothing left to compare
                    if (skipRestOfNodes) break;

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

                        else if (transfers.get(j).fromStopId == nodes[i].label) {
                            if (transfers.get(j).toStopId != previousToStopId) {
                                /*System.out.println(
                                        "transfers.get(" + j + ").fromStopId = " + transfers.get(j).fromStopId
                                       +", transfers.get(" + j + ").toStopId = " + transfers.get(j).toStopId
                                       +", transfers.get(" + j + ").transferType = " + transfers.get(j).transferType
                                       +", nodes[" + i + "].label = " + nodes[i].label
                                       +", previousToStopId = " + previousToStopId);*/

                                /* calculate weight of edge (if transferType is 0, then cost is 2,
                                   if transferType is 2 then cost is minTransferTime/100 */
                                float weight = 0.0f;

                                //cost is 2 if transfer type is 0 (immediate transfer possible, from transfers.txt)
                                if (transfers.get(j).transferType == 0) weight = 2.0f;
                                    //cost is 1 if transfer type is 1 (from stop_times.txt)
                                else if (transfers.get(j).transferType == 1) weight = 1.0f;
                                    //cost is minimum transfer time/100 if transfer type is 2 (from transfers.txt)
                                else if (transfers.get(j).transferType == 2)
                                    weight = (transfers.get(j).minTransferTime / 100);
                                else System.out.println("invalid transfer type" + transfers.get(j).transferType);

                                //need to loop through nodes to get node corresponding to toStopId

                                int toStopId = 0;
                                for (int k = 0; k < nodes.length; k++) {
                                    if (nodes[k].label == transfers.get(j).toStopId) toStopId = k;
                                }

                                //print out toStopIds
                                //System.out.println("toStopId = " + toStopId);

                                //add edge to nodes and edges
                                Edge e = new Edge(nodes[i], nodes[toStopId], weight);

                                nodes[i].addEdge(e);
                                edges.add(e);

                                previousToStopId = transfers.get(j).toStopId;
                            }
                            //go to next transfer and repeat checks in the next iteration of the loop
                            j++;
                        }

                        //go to next transfer and repeat checks in the next iteration of the loop
                        else if (transfers.get(j).fromStopId < nodes[i].label) {
                            j++;
                        }

                        /* if the current transfer number is higher than the current node number then
                          all transfers have been added, or there were no transfers from the current node */
                        else if (transfers.get(j).fromStopId > nodes[i].label) {
                            allTransfersFound = true;
                        }
                    }
                }
            } catch (Exception e) {
                //System.err.format("Exception occurred trying to read '%s'.", filename);
                e.printStackTrace();
            }
        }
    }

    public int getNodeIndexFromLabel(int label)
    {
        int index = 0;
        boolean indexFound = false;
        while (!indexFound&&(index<this.nodes.length))
        {
            if (this.nodes[index].label==label) indexFound = true;
            else index++;
        }

        if (index==this.nodes.length) return -1;
        else return index;
    }

    public void getTripsFromArrivalTime(String arrivalTime)
    {
        System.out.println("Arrival time: "+arrivalTime);

        Collections.sort(transfers, Transfer::compareId);

        for (int i=0;i<transfers.size();i++)
        {
            //System.out.println("transfers.get("+i+").arrivalTime = ["+transfers.get(i).arrivalTime+"]");
            if (transfers.get(i).arrivalTime.equals(arrivalTime))
            {
                System.out.println("Trip "+transfers.get(i).tripId+" has arrival time "+
                        transfers.get(i).arrivalTime);
            }
        }
    }
}

/**
 * Custom class that stores information about a path
 */
class Path {
    Node src, dst;
    Double distance;

    Path() {
        distance = -1.0;
    }

    Path(Node src, Node dst, Double distance) {
        this.src = src;
        this.dst = dst;
        this.distance = distance;
    }
}

/**
 * Custom class that stores information about a Node
 */
class Node {
    int label;
    ArrayList<Edge> edges;

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

        for (int i = 0; i < edges.size(); i++){
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

    public int compareTo(Node o) {
        return this.label - o.label;
    }
}

/**
 * Custom class that stores information about an edge
 */
class Edge {
    Node src, dst;
    double weight;

    /**
     * @param src: Source Node
     * @param dst: Destination Node
     * @param weight: Weight of the Edge
     */
    Edge(Node src, Node dst, double weight) {
        this.src = src;
        this.dst = dst;
        this.weight = weight;
    }

    public String toString()
    {
        return "("+this.src.label+","+this.dst.label+","+this.weight+")";
    }

}

