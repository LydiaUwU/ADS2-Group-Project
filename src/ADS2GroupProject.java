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
        ArrayList<Edge> shortestPath1 = new ArrayList<Edge>();

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

        //testing TST
        TST testTST = new TST();

        testTST.put("she");
        testTST.put("sells");
        testTST.put("sea");
        testTST.put("shells");
        testTST.put("by");
        testTST.put("the");
        testTST.put("sea");
        testTST.put("shore");
        testTST.put("aaaaaaaa");
        testTST.put("zzzzzzzz");
        testTST.put("REAL STREET REAL STREET REAL STREET TEST 123");

        System.out.println(testTST.getSubtree());

        /*System.out.println(testTST.get("she"));
        //sells doesn't work correctly
        System.out.println(testTST.get("sells"));
        System.out.println(testTST.get("sea"));
        System.out.println(testTST.get("shells"));
        System.out.println(testTST.get("by"));
        System.out.println(testTST.get("the"));
        System.out.println(testTST.get("sea"));
        System.out.println(testTST.get("shore"));

        System.out.println(testTST.get("some"));
        System.out.println(testTST.get("fake"));
        System.out.println(testTST.get("words"));

        System.out.println(testTST.get("aaaaaaaa"));
        System.out.println(testTST.get("zzzzzzzz"));
        System.out.println(testTST.get("REAL STREET REAL STREET REAL STREET TEST 123"));
        System.out.println(testTST.get("REAL STREET REAL STREET REAL STREET TEST 123 "));*/
    }
}
