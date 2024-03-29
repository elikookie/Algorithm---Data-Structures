import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;


public class GraphLists {
    
    public static void main(String[] args) { // main method	
        Scanner inputScanner = new Scanner(System.in); // create scanner object
    
        System.out.print("Enter the name of the file containing the graph: "); 
        String filename = inputScanner.nextLine(); // read in file name from user input 
        Graph graph = readGraphFromFile(filename); 
    
        System.out.print("Enter the starting vertex: "); 
        int startingVertex = inputScanner.nextInt();
    
        System.out.println("Depth First Traversal:");
        graph.depthFirstTraversal(startingVertex);
        System.out.println();
    
        System.out.println("Breadth First Traversal:");
        graph.breadthFirstTraversal(startingVertex);
        System.out.println();
    
        System.out.println("Prim's Algorithm:");
        graph.prim(startingVertex);
        System.out.println();
    
        System.out.println("Dijkstra's Algorithm:");
        graph.dijkstra(startingVertex);
    
        inputScanner.close();
    }
    

    public static Graph readGraphFromFile(String filename) { 
        try {
            Scanner scanner = new Scanner(new File(filename)); 
            int vertices = scanner.nextInt();
            int edges = scanner.nextInt();

            Graph graph = new Graph(vertices);

            for (int i = 0; i < edges; i++) { // read in edges from file and add to graph 
                int v1 = scanner.nextInt();
                int v2 = scanner.nextInt();
                int weight = scanner.nextInt();
                graph.addEdge(v1, v2, weight);
            }

            scanner.close();

            return graph;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return null;
        }
    }
}
class Graph { 
    private final int vertices;
    private final List<List<Edge>> adjacencyList; 

    public Graph(int vertices) {
        this.vertices = vertices; 
        adjacencyList = new ArrayList<>(vertices + 1); 

        for (int i = 0; i <= vertices; i++) { //
            adjacencyList.add(new ArrayList<>());
        }
    }

    // convert vertex into char for pretty printing
    private char toChar(int u) {
        return (char) (u + 64);
    }

    public void addEdge(int v1, int v2, int weight) {
        adjacencyList.get(v1).add(new Edge(v2, weight));
        adjacencyList.get(v2).add(new Edge(v1, weight));
    }

    public void depthFirstTraversal(int startingVertex) {
        boolean[] visited = new boolean[vertices + 1];
        System.out.print("DFS traversal: ");
        depthFirstTraversalHelper(startingVertex, visited);
        System.out.println();
    }
    
    private void depthFirstTraversalHelper(int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.print(toChar(vertex) + " ");
    
        for (Edge edge : adjacencyList.get(vertex)) {
            if (!visited[edge.vertex]) {
                depthFirstTraversalHelper(edge.vertex, visited);
            }
        }
    }
    
    public void breadthFirstTraversal(int startingVertex) {
        boolean[] visited = new boolean[vertices + 1];
        Queue<Integer> queue = new LinkedList<>();
        visited[startingVertex] = true;
        queue.add(startingVertex);
    
        System.out.print("BFS traversal: ");
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            System.out.print(toChar(vertex) + " ");
    
            for (Edge edge : adjacencyList.get(vertex)) {
                if (!visited[edge.vertex]) {
                    visited[edge.vertex] = true;
                    queue.add(edge.vertex);
                }
            }
        }
        System.out.println();
    }
    

    public void prim(int startingVertex) {
        boolean[] visited = new boolean[vertices + 1];
        int[] parent = new int[vertices + 1];
        int[] key = new int[vertices + 1];
        List<String> mstEdges = new ArrayList<>();
    
        Arrays.fill(key, Integer.MAX_VALUE);
        key[startingVertex] = 0;
    
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new Edge(startingVertex, 0));
    
        while (!priorityQueue.isEmpty()) {
            int vertex = priorityQueue.poll().vertex;
    
            if (!visited[vertex]) {
                visited[vertex] = true;
                for (Edge edge : adjacencyList.get(vertex)) {
                    if (!visited[edge.vertex] && edge.weight < key[edge.vertex]) {
                        parent[edge.vertex] = vertex;
                        key[edge.vertex] = edge.weight;
                        priorityQueue.add(new Edge(edge.vertex, edge.weight));
                    }
                }
                if (parent[vertex] != 0) {
                    mstEdges.add(toChar(parent[vertex]) + " - " + toChar(vertex) + "    " + key[vertex]);
                }
            }
        }
    
        // Print the MST edges and their weights
        System.out.println("Edge   Weight");
        for (String edge : mstEdges) {
            System.out.println(edge);
        }
    }
    
    public void dijkstra(int startingVertex) {
        int[] distances = new int[vertices + 1];
        boolean[] visited = new boolean[vertices + 1];
    
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[startingVertex] = 0;
    
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new Edge(startingVertex, 0));
    
        while (!priorityQueue.isEmpty()) {
            int vertex = priorityQueue.poll().vertex;
    
            if (!visited[vertex]) {
                visited[vertex] = true;
                for (Edge edge : adjacencyList.get(vertex)) {
                    int newDistance = distances[vertex] + edge.weight;
                    if (!visited[edge.vertex] && newDistance < distances[edge.vertex]) {
                        distances[edge.vertex] = newDistance;
                        priorityQueue.add(new Edge(edge.vertex, newDistance));
                    }
                }
            }
        }
    
        // Print the shortest path distances from the starting vertex
        System.out.println("Vertex   Distance from Vertex " + toChar(startingVertex));
        for (int i = 1; i <= vertices; i++) {
            System.out.println(toChar(i) + "         " + distances[i]);
        }
    }
    
}
class Edge implements Comparable<Edge> {
    int vertex;
    int weight;

    public Edge(int vertex, int weight) {
        this.vertex = vertex;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}