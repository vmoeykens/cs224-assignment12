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
        // Construct a residual graph
        
        return 1;
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

    public int getEdgeWeight(int source, int destination) {
        return this.graph[source][destination];
    }

    public int[][] getGraph() {
        return this.graph;
    }
}