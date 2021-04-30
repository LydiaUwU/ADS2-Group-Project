import java.util.*;

/**
 * Custom class to handle dijkstra pathing.
 *
 * @Authors: Lydia MacBride, David King, Liam O Lionaird, Sarah Klein
 */

public class PathDijkstra {
    ArrayList<Edge> edgeTo;
    ArrayList<Double> distTo;
    ArrayList<Boolean> markedVertices;
    double timeTaken;
    boolean noPath = false;

    int startLabel = -1;
    int endLabel = -1;
    int startIndex = -1;
    int endIndex = -1;

    Graph graphIn;


    PathDijkstra(Graph inputGraph, int k, int l) {
        graphIn = inputGraph;
        startLabel = k;
        endLabel = l;

        double maxDistance = 0;

        if (!noPath) {
            //initialising edgeTo and distTo
            edgeTo = new ArrayList<>();
            distTo = new ArrayList<>();
            markedVertices = new ArrayList<>();

            for (int i = 0; i < inputGraph.nodes.length; i++) {
                edgeTo.add(null);
                distTo.add(Double.POSITIVE_INFINITY);
            }

            distTo.set(graphIn.getNodeIndexFromLabel(k), 0.0);
            for (int i = 0; i < inputGraph.nodes.length; i++) {
                markedVertices.add(false);
            }


            //relaxing edges i number of times
            for (int i = 0; i < inputGraph.nodes.length; i++) {

                int currentVertexIndex = 0;
                double currentMinDistance = Double.POSITIVE_INFINITY;
                int minVertex = 0;

                for (currentVertexIndex = 0; currentVertexIndex < inputGraph.nodes.length; currentVertexIndex++) {
                    if ((distTo.get(currentVertexIndex) < currentMinDistance) && !markedVertices.get(currentVertexIndex)) {
                        minVertex = currentVertexIndex;
                        currentMinDistance = distTo.get(currentVertexIndex);
                    }

                }

                relax(inputGraph, minVertex);
                markedVertices.set(minVertex, true);

            }


            startIndex = graphIn.getNodeIndexFromLabel(k);
            endIndex = graphIn.getNodeIndexFromLabel(l);
        }
    }

    private void relax(Graph G, int v) {
        for (int i = 0; i < G.nodes[v].edges.size(); i++) {
            if (true) { //G.nodes[v].edges.get(i) != null
                Edge e = G.nodes[v].edges.get(i);
                int w = graphIn.getNodeIndexFromLabel(e.dst.label);

                if (distTo.get(w) > distTo.get(v) + e.weight) {
                    distTo.set(w, distTo.get(v) + e.weight);
                    edgeTo.set(w, e);
                }
            }
        }
    }

    public ArrayList<Edge> getShortestPath() {
        ArrayList<Edge> pathEdges = new ArrayList<Edge>();

        boolean startNodeFound = false;
        boolean noPathFound = false;

        if ((startIndex == -1) || (endIndex == -1)) {
            return null;
        }

        Edge currentEdge = edgeTo.get(endIndex);


        while (!startNodeFound) {
            pathEdges.add(currentEdge);
            if (currentEdge.src.label == startLabel) {
                startNodeFound = true;
            } else {
                if (edgeTo.get(graphIn.getNodeIndexFromLabel(currentEdge.src.label)) != null) {
                    currentEdge = edgeTo.get(graphIn.getNodeIndexFromLabel(currentEdge.src.label));

                } else {
                    noPathFound = true;
                    break;
                }
            }
        }

        if (!noPathFound) {
            return pathEdges;
        } else {
            return null;
        }
    }
}
