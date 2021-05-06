/**
 * @author Vincent Moeykens
 */

import java.util.LinkedList;

class Main {
    public static void main(String[] args){
        // Set up our directed, weighted graph
        DirectedWeightedGraph dwGraph = new DirectedWeightedGraph(8);
        dwGraph.addEdge(0, 1, 10);
        dwGraph.addEdge(0, 2, 5);
        dwGraph.addEdge(0, 3, 15);
        dwGraph.addEdge(1, 2, 4);
        dwGraph.addEdge(1, 4, 9);
        dwGraph.addEdge(1, 5, 15);
        dwGraph.addEdge(2, 3, 4);
        dwGraph.addEdge(2, 5, 8);
        dwGraph.addEdge(3, 6, 30);
        dwGraph.addEdge(4, 5, 15);
        dwGraph.addEdge(4, 7, 10);
        dwGraph.addEdge(5, 6, 15);
        dwGraph.addEdge(5, 7, 10);
        dwGraph.addEdge(6, 2, 6);
        dwGraph.addEdge(6, 7, 10);

        // Pick our source and sink nodes
        int s = 0;
        int t = 7;

        // Initialize a ford fulkerson object
        FordFulkerson f = new FordFulkerson();

        // Run the algorithm and get the output residual graph
        DirectedWeightedGraph outputResidualGraph = f.maxFlow(dwGraph, s, t);

        // Calculate the max flow based on the output flow from the sink node in the final residual graph
        int maxFlow = 0;
        for (int i = 0; i < outputResidualGraph.getNumNodes(); i++) {
            maxFlow += outputResidualGraph.getEdge(t, i);
        }

        // Display the max flow
        System.out.println("Maximum flow value  = " + maxFlow);
    }
}

class FordFulkerson {
    /**
     * Run the ford fulkerson algorithm
     * @param G Input graph
     * @param s Source node
     * @param t Sink node
     * @return Final residual graph after running the algorithm
     */
    public DirectedWeightedGraph maxFlow(DirectedWeightedGraph G, int s, int t) {
        // Construct the flow graph
        DirectedWeightedGraph flows = new DirectedWeightedGraph(G.getNumNodes());
        // Construct a residual graph
        DirectedWeightedGraph residualGraph = constructResidualGraph(G);
        LinkedList<Integer> augmentingPath = findAugmentingPath(residualGraph, s, t);
        while (augmentingPath.peek() != -1) {
            System.out.println(residualGraph);
            DirectedWeightedGraph flowsPrime = augment(flows, augmentingPath, residualGraph);
            updateResidualGraph(residualGraph, flows, flowsPrime);
            augmentingPath = findAugmentingPath(residualGraph, s, t);
        }
        
        return residualGraph;
    }

    /**
     * Construct the initial residual graph
     * @param graph Original graph to build the residual off of
     * @return Directed weighted residual graph
     */
    DirectedWeightedGraph constructResidualGraph(DirectedWeightedGraph graph) {
        return new DirectedWeightedGraph(graph.getGraph());
    }

    void updateResidualGraph(DirectedWeightedGraph residualGraph, DirectedWeightedGraph flows, DirectedWeightedGraph flowsPrime) {
        for (int i = 0; i < residualGraph.getNumNodes(); i++) {
            for (int j = 0; j < residualGraph.getNumNodes(); j++) {
                int flowsWeight = flowsPrime.getEdge(i, j);
                if (flowsWeight > 0) {
                    residualGraph.modifyEdge(i, j, -flowsWeight);
                    residualGraph.modifyEdge(j, i, flowsWeight);
                }
            }
        }
    }
    
    /**
     * Determine the value of the bottleneck edge in the augmenting path
     * @param augmentingPath The augmenting path to find the bottleneck of 
     * @param residualGraph The current residual graph to determine the bottleneck value
     * @return The current bottlneck value for this augmenting path
     */
    int bottleneck(LinkedList<Integer> augmentingPath, DirectedWeightedGraph residualGraph) {
        int bottleneckVal = Integer.MAX_VALUE;
        for (int i = 0; i < augmentingPath.size() - 1; i++) {
            if (residualGraph.getEdge(augmentingPath.get(i), augmentingPath.get(i + 1)) < bottleneckVal) {
                bottleneckVal = residualGraph.getEdge(augmentingPath.get(i), augmentingPath.get(i + 1));
            }
        }
        System.out.println("Flow amount: " + bottleneckVal + "\n");
        return bottleneckVal;
    }


    /**
     * Adjust the flows as necessary based on the augmenting path found
     * @param flows current flows graph
     * @param augmentingPath current augmenting path
     * @return edge weight of the bottleneck
     */
    DirectedWeightedGraph augment(DirectedWeightedGraph flows, LinkedList<Integer> augmentingPath, DirectedWeightedGraph residualGraph) {
        DirectedWeightedGraph newFlows = new DirectedWeightedGraph(flows.getNumNodes());
        int b = bottleneck(augmentingPath, residualGraph);
        for (int i = 0; i < augmentingPath.size() - 1; i++) {
            int currentEdgeWeight = residualGraph.getEdge(augmentingPath.get(i), augmentingPath.get(i + 1));
            if (currentEdgeWeight != 0) {
                newFlows.modifyEdge(augmentingPath.get(i), augmentingPath.get(i + 1), b);
            } else {
                newFlows.modifyEdge(augmentingPath.get(i), augmentingPath.get(i + 1), -b);
            }
        }
        return newFlows;
    }

    /**
     * Modified breadth first search Java implementation from the following website: https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
     * Code has been changed to fit the requirements of the findAugmentingPath method
     * 
     * @param graph Input graph
     * @param s source node
     * @param t sink node
     * @return Path from the source to the sink node (if it exists). If it does not exist, returns the array [-1]
     */
    LinkedList<Integer> findAugmentingPath(DirectedWeightedGraph graph, int s, int t) {
        int[] outputPath = new int[graph.getNumNodes()];

        boolean visited[] = new boolean[graph.getNumNodes()];
 
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        outputPath[s] = -1;

        boolean pathExists = false;
 
        // Standard BFS Loop
        while (queue.size() != 0 && pathExists == false) {
            int u = queue.poll();
 
            for (int v = 0; v < graph.getNumNodes(); v++) {
                if (visited[v] == false
                    && graph.getEdge(u, v) > 0) {
                    if (v == t) {
                        outputPath[v] = u;
                        pathExists = true;
                        break;
                    }
                    queue.add(v);
                    outputPath[v] = u;
                    visited[v] = true;
                }
            }
        }

        if (pathExists) {
            LinkedList<Integer> finalPath = new LinkedList<Integer>();
            finalPath.push(t);
            int start = t;
            while (outputPath[start] != -1) {
                finalPath.push(outputPath[start]);
                start = outputPath[start];
            }
            System.out.println("Augmenting path found: ");
            for (int i = 0; i < finalPath.size(); i++) {
                if (finalPath.get(i) == s) {
                    System.out.print("s -> ");
                } else if (finalPath.get(i) == t) {
                    System.out.println("t");
                } else {
                    System.out.print(finalPath.get(i));
                    System.out.print(" -> ");
                }
            }
            return finalPath;
        }

        LinkedList<Integer> result = new LinkedList<Integer>();
        result.push(-1);
        System.out.println("No augmenting path found!");
        return result;
    }
}

class DirectedWeightedGraph {
    // Adjacency matrix to represent graph
    int graph[][];
    int numNodes;

    /**
     * Constructor to initialize all edge weights to 0 for the total number of nodes in the graph
     * @param numNodes Total number of nodes in the graph
     */
    DirectedWeightedGraph(int numNodes) {
        this.numNodes = numNodes;
        this.graph = new int[numNodes][numNodes];
    }

    /**
     * Constructor to initialize based on a 2 dimensional int array of edge weights
     * @param graph Source graph to base the new DirectedWeightGraph object off of
     */
    DirectedWeightedGraph(int[][] graph) {
        this.graph = graph;
        this.numNodes = graph[0].length;
    }

    public void addEdge(int source, int destination, int weight) {
        this.graph[source][destination] = weight;
    }

    public int getEdge(int source, int destination) {
        return this.graph[source][destination];
    }

    public void modifyEdge(int source, int destination, int weightDelta) {
        this.graph[source][destination] += weightDelta;
    }

    public void setEdge(int source, int destination, int newWeight) {
        this.graph[source][destination] = newWeight;
    }

    public int[][] getGraph() {
        return this.graph;
    }

    public int getNumNodes() {
        return this.numNodes;
    }


    @Override
    public String toString() {
        String graphOutput = "";
        int counter = 0;
        for (int i = 0; i < this.getNumNodes(); i++) {
            for (int j = 0; j < this.getNumNodes(); j++) {
                if (this.getEdge(i, j) > 0 ) {
                    graphOutput += ("(" + i + ", " + j + "): " + this.getEdge(i, j) + ", ");
                    counter += 1;
                    if (counter % 5 == 0) {
                        graphOutput += "\n";
                    }    
                }
            }
        }
        return graphOutput;
    }

}