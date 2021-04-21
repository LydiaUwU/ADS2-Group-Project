import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Custom class to generate and store a graph based on a .txt file as specified in the assignment spec.
 *
 * @Author: Lydia MacBride
 */

public class Graph {
    Node[] nodes;
    ArrayList<Edge> edges;

    /**
     * @param filename: Filename of .txt file to use to generate the graph.
     */
    Graph(String filename) {
        if (filename != null) {
            edges = new ArrayList<>();

            // Read file and use data to build graph
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String line;

                int lineNo = 0;
                while ((line = reader.readLine()) != null) {
                    Scanner sc = new Scanner(line);
                    if (lineNo == 0) { // Read amount of Nodes and initialize them
                        int numberOfNodes = sc.nextInt();
                        nodes = new Node[numberOfNodes];

                        for (int i = 0; i < numberOfNodes; i++) {
                            nodes[i] = new Node(i);
                        }
                    } else if (lineNo > 1) { // Find and add edges
                        int[] nodeLabels = new int[2];
                        double weight = 0;

                        int i = 0;
                        while (sc.hasNext()) {
                            if (i <= 1) {
                                nodeLabels[i] = sc.nextInt();
                            } else {
                                weight = sc.nextDouble();
                            }

                            i++;
                        }

                        // Add discovered nodes and edge
                        Edge e = new Edge(nodes[nodeLabels[0]], nodes[nodeLabels[1]], weight);

                        nodes[nodeLabels[0]].addEdge(e);
                        edges.add(e);
                    }

                    lineNo++;
                    sc.close();
                }

                reader.close();

            } catch (Exception e) {
                System.err.format("Exception occurred trying to read '%s'.", filename);
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints the nodes and their adjacent edges in a human friendly format.
     *

     public void printNodes() {
     for (Node i : nodes) {
     System.out.println("Node: " + i.label);

     for (Edge j : i.edges) {
     System.out.println("To: " + j.dst.label + ", Weight: " + j.weight + ",");
     }
     }
     }
     */
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
}

