import java.util.*;

public class PathDijkstra {

	
	ArrayList<Edge> edgeTo = new ArrayList<Edge>();
	ArrayList<Double> distTo = new ArrayList<Double>();
	ArrayList<Boolean> markedVertices = new ArrayList<Boolean>();
	double timeTaken;
	boolean noPath = false;

	int startLabel = -1;
	int endLabel = -1;
	int startIndex = -1;
	int endIndex = -1;

	Graph graphIn;

	//set to true to print debug info
	boolean verbose = false;
	
    PathDijkstra (Graph inputGraph, int k, int l){

    	graphIn = inputGraph;
    	startLabel = k;
    	endLabel = l;

    	double maxDistance = 0;

    	if (!noPath)
    	{
			//initialising edgeTo and distTo
			edgeTo.clear();
			distTo.clear();
			markedVertices.clear();

			for (int i=0;i<inputGraph.nodes.length;i++)
			{
				edgeTo.add(null);
				distTo.add(Double.POSITIVE_INFINITY);
			}

			distTo.set(k, 0.0);
			for (int i=0;i<inputGraph.nodes.length;i++)
			{
				markedVertices.add(false);
			}

			/*if (verbose)
			{ System.out.println("-----initial edges-----");
				for (int j=0;j<edgeTo.size();j++)
				{
					if (edgeTo.get(j) != null)
					{
						System.out.println(edgeTo.get(j).toString());
					}
					else
					{
						System.out.println("null");
					}
				}

				System.out.println("-----initial distances-----");

				for (int j=0;j<distTo.size();j++)
				{
					System.out.println(distTo.get(j).toString());
				}

				System.out.println("-----initial marked vertices-----");

				for (int j=0;j<markedVertices.size();j++)
				{
					System.out.println(markedVertices.get(j).toString());
				}
			}*/

			//relaxing edges i number of times
			for (int i=0;i<inputGraph.nodes.length;i++)
			{
				//if (verbose) System.out.println("~~~~~~~~~~iteration " + i + " ~~~~~~~~~~");
				//System.out.println("inputGraph.nodes.length = " + inputGraph.nodes.length);

				int currentVertexIndex = 0;
				double currentMinDistance = Double.POSITIVE_INFINITY;
				int minVertex = 0;

				for (currentVertexIndex=0;currentVertexIndex<inputGraph.nodes.length;currentVertexIndex++)
				{
					if ((distTo.get(currentVertexIndex) < currentMinDistance) && !markedVertices.get(currentVertexIndex))
					{
						minVertex = currentVertexIndex;
						currentMinDistance = distTo.get(currentVertexIndex);
					}

					/*if (verbose)
					{
					 System.out.println("current minVertex = " + minVertex + ", currentMinDistance = " + currentMinDistance);
					 System.out.println("currentVertexIndex = " + currentVertexIndex + ", distTo.get(currentVertexIndex) = " +
					 distTo.get(currentVertexIndex) + ", markedVertices.get(currentVertexIndex) = " + markedVertices.get(currentVertexIndex));
					}*/
				}

				relax(inputGraph, minVertex);
				markedVertices.set(minVertex,true);

				/*if (verbose)
				{
					System.out.println("-----edges-----");

					for (int j=0;j<edgeTo.size();j++)
					{
						if (edgeTo.get(j) != null)
						{
							System.out.println(edgeTo.get(j).toString());
						}
						else
						{
							System.out.println("null");
						}
					}

					System.out.println("-----distances-----");

					for (int j=0;j<distTo.size();j++)
					{
						System.out.println(distTo.get(j).toString());
					}

					System.out.println("-----marked vertices-----");

					for (int j=0;j<markedVertices.size();j++)
					{
						System.out.println(markedVertices.get(j).toString());
					}
				}*/
			}

			/*for (int j=0;j<distTo.size();j++)
			{
					if (distTo.get(j).equals(Double.POSITIVE_INFINITY))
					{
						noPath = true;
						//if (verbose) System.out.println("no path found");
					}

					else if (distTo.get(j) >= maxDistance)
					{
						maxDistance = distTo.get(j);
						//if (verbose) System.out.println("maxDistance is now " + maxDistance);
						timeTaken = Math.floor(maxDistance);
						//if (verbose) System.out.println("timeTaken is now " + timeTaken);
					}
			}*/

			startIndex = graphIn.getNodeIndexFromLabel(k);
			endIndex = graphIn.getNodeIndexFromLabel(l);
	    }
    }
	private void relax(Graph G, int v)
	{
		for (int i=0;i<G.nodes[v].edges.size();i++)
	 	{    		
	 		if (true) //G.nodes[v].edges.get(i) != null)
	  		{
	   			Edge e = G.nodes[v].edges.get(i);
	       		int w = graphIn.getNodeIndexFromLabel(e.dst.label);
	    		
	    		if (distTo.get(w) > distTo.get(v) + e.weight)
	    		{
	    			distTo.set(w, distTo.get(v) + e.weight);
	    			edgeTo.set(w, e);
	    		}
	   		}
	   	}
	}
    
    
    public ArrayList<Edge> getShortestPath(){

    	System.out.println("start label = "+startLabel);
		ArrayList<Edge> pathEdges = new ArrayList<Edge>();

		boolean startNodeFound = false;
		boolean noPathFound = false;

		if ((startIndex==-1)||(endIndex==-1)) return null;

		Edge currentEdge = edgeTo.get(endIndex);

		//System.out.println("edgeTo.size() = "+edgeTo.size());

		while (!startNodeFound)
		{
			System.out.println("---------------\ncurrentEdge = "+currentEdge.toString());
			pathEdges.add(currentEdge);
			if (currentEdge.src.label==startLabel)
			{
				System.out.println("found start");
				startNodeFound = true;
			}
			else
			{
				System.out.println("currentEdge.src.label = "+currentEdge.src.label);
				System.out.println("graphIn.getNodeIndexFromLabel(currentEdge.src.label) = "
				+graphIn.getNodeIndexFromLabel(currentEdge.src.label));
				if (edgeTo.get(graphIn.getNodeIndexFromLabel(currentEdge.src.label))!=null)
				{
					currentEdge = edgeTo.get(graphIn.getNodeIndexFromLabel(currentEdge.src.label));
					System.out.println("currentEdge is now "+currentEdge.toString());
				}
				else
				{
					noPathFound = true;
					break;
				}
			}
		}

   		if (!noPathFound) return pathEdges;
   		else return null;
    }

}
