import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Kruskal {

    public static void main(String[] args) { // main method
        Scanner inputScanner = new Scanner(System.in); // create scanner object

        System.out.print("Enter the name of the file containing the graph: ");
        String filename = inputScanner.nextLine(); // read in file name from user input
        Graph graph = readGraphFromFile(filename);

        System.out.println("Kruskal's Algorithm:");
        graph.kruskal();
        System.out.println();

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
    private final List<Edge> edgeList;

    public Graph(int vertices) {
        this.vertices = vertices;
        edgeList = new ArrayList<>();
    }

    // convert vertex into char for pretty printing
    private char toChar(int u) {
        return (char) (u + 64);
    }

    public void addEdge(int v1, int v2, int weight) {
        edgeList.add(new Edge(v1, v2, weight));
    }

    public int find(int[] parent, int vertex) {
        if (parent[vertex] == -1) {
            return vertex;
        }
        return find(parent, parent[vertex]);
    }

    public void union(int[] parent, int x, int y) {
        int xParent = find(parent, x);
        int yParent = find(parent, y);
        parent[xParent] = yParent;
    }

    public void kruskal() {
        // Sort edges by weight
        Collections.sort(edgeList);

        int[] parent = new int[vertices + 1];
        for (int i = 0; i <= vertices; i++) {
            parent[i] = -1;
        }

        List<Edge> mstEdges = new ArrayList<>();

        for (Edge edge : edgeList) {
            int x = find(parent, edge.v1);
            int y = find(parent, edge.v2);

            if (x != y) {
                mstEdges.add(edge);
                union(parent, x, y);
            }
        }

        // Print the MST edges and their weights
        System.out.println("Edge   Weight");
        for (Edge edge : mstEdges) {
            System.out.println(toChar(edge.v1) + " - " + toChar(edge.v2) + "    " + edge.weight);
        }
    }
}

class Edge implements Comparable<Edge> {
    int v1;
    int v2;
    int weight;

    public Edge(int v1, int v2, int weight) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}