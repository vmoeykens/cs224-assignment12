import java.util.LinkedList;

/**
 * @author Vincent Moeykens
 */

class Main {
    public static void main(String[] args){
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


        FordFulkerson f = new FordFulkerson();
 
        System.out.println("The maximum possible flow is "
                           + f.maxFlow(dwGraph, 0, 7));
    }
}

class FordFulkerson {
    public int maxFlow(DirectedWeightedGraph G, int s, int t) {
        // Construct the flow graph
        DirectedWeightedGraph flows = new DirectedWeightedGraph(G.getNumNodes());
        // Construct a residual graph
        DirectedWeightedGraph residualGraph = constructResidualGraph(G);

        LinkedList<Integer> augmentingPath = findAugmentingPath(residualGraph, 0, 7);
        while (augmentingPath.peek() != -1) {
            flows = augment(flows, augmentingPath, residualGraph);
        }

        return 1;
    }

    DirectedWeightedGraph constructResidualGraph(DirectedWeightedGraph graph) {
        return new DirectedWeightedGraph(graph.getGraph());
    }
    
    /**
     * Determine the value of the bottleneck edge in the augmenting path
     * @param augmentingPath
     * @param residualGraph
     * @return
     */
    int bottleneck(LinkedList<Integer> augmentingPath, DirectedWeightedGraph residualGraph) {
        int bottleneckVal = Integer.MAX_VALUE;
        for (int i = 0; i < augmentingPath.size() - 2; i++) {
            if (residualGraph.getEdge(augmentingPath.get(i), augmentingPath.get(i + 1)) < bottleneckVal) {
                bottleneckVal = residualGraph.getEdge(augmentingPath.get(i), augmentingPath.get(i + 1));
            }
        }
        return bottleneckVal;
    }


    /**
     * Adjust the flows as necessary based on the augmenting path found
     * @param flows current flows graph
     * @param augmentingPath current augmenting path
     * @return edge weight of the bottleneck
     */
    DirectedWeightedGraph augment(DirectedWeightedGraph flows, LinkedList<Integer> augmentingPath, DirectedWeightedGraph residualGraph) {
        int b = bottleneck(augmentingPath, residualGraph);
        return new DirectedWeightedGraph(1);
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
                    // If we find a connection to the sink
                    // node, then there is no point in BFS
                    // anymore We just have to set its parent
                    // and can return true
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
            return finalPath;
        }

        LinkedList<Integer> result = new LinkedList<Integer>();
        result.push(-1);
 
        return result;
    }
}

class DirectedWeightedGraph {
    // Adjacency matrix to represent graph
    int graph[][];
    int numNodes;

    DirectedWeightedGraph(int numNodes) {
        this.numNodes = numNodes;
        this.graph = new int[numNodes][numNodes];
    }

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

    public void modifyEdge(int source, int destination, int newWeight) {
        this.graph[source][destination] = newWeight;
    }

    public int[][] getGraph() {
        return this.graph;
    }

    public int getNumNodes() {
        return this.numNodes;
    }
}