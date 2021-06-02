package com.company;

// FindMaxFlow representation in Java
// In this project i am using fordFulkerson algorithm

// Source - https://github.com/shahwaiz90/FordFulkersonAlgorithm , https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
// IIT - 2019715
// UOW - W1790171
// NAME - BAVEEGA KUGANESAN

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    //2D Matrix array
    private static int matrix[][];
    // Declare the variable
    private static int numOfVertices;
    private static int max;

    public static void main(String[] args) throws IOException {
        //Calling the console method
        find();
        console();
    }

    //Console method
    public static void console() throws IOException {
        // Asking the option
        System.out.println("*** 1 - Inserting the edge ***");
        System.out.println("*** 2 - Deleting the edge ***");
        System.out.println("\n");
        System.out.println("********** Select The Numbers ********** : ");
        Scanner input = new Scanner(System.in);

        String choose = input.nextLine();
        // Enter 1 then insertingEdge method is working
        if (choose.trim().equals("1")) {
            insertingEdge();
            // Enter 2 then deletingEdge method is working
        } else if (choose.trim().equals("2")) {
            deletingEdge();

        }
        print();
    }

    // InsertingEdge function is starting......
    private static void insertingEdge() {
        int[] edgeInsert = getDetails(true);
        matrix[edgeInsert[0]][edgeInsert[1]] = edgeInsert[2];
        System.out.println("Edge is added!");

    }


    // DeletingEdge function is starting......
    private static void deletingEdge() {
        int[] edgeDelete = getDetails(false);
        matrix[edgeDelete[0]][edgeDelete[1]] = 0;
        System.out.println("Edge is removed!");
    }


    // Get the details for the users............
    private static int[] getDetails(boolean insertingEdge) {
        //  Inserting , Deleting an edge from the graph

        int fromNode = validationPart("Enter 'from' node : ");
        int toNode = validationPart("Enter 'to' node : ");
        if (insertingEdge) {
            int capacity = validationPart("Enter the capacity : ");
            // Return the fromNode, toNode, capacity
            return new int[]{fromNode, toNode, capacity};
        }
        // Return the fromNode, toNode
        return new int[]{fromNode, toNode};
    }

    // Validation part is starting.........
    private static int validationPart(String get) {
        // Validation method
        Scanner input = new Scanner(System.in);
        System.out.print(get);

        return input.nextInt();
    }

    // Initialize the matrix.........
    public Main(int numVertices) {
        this.numOfVertices = numVertices;
        matrix = new int[numVertices][numVertices];

    }

    // Adding edges.............
    public static void addEdge(int i, int j, int capacity) {
        Main.matrix[i][j] = capacity;
    }

    // Printing the matrix.......................
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < numOfVertices; i++) {
            for (int value : matrix[i]) {
                s.append(value + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }


    static boolean bfs(int rGraph[][], int s, int t, int parent[]) {
        // Create a visited array and mark all vertices as
        // not visited
        boolean visited[] = new boolean[numOfVertices];
        for (int i = 0; i < numOfVertices; ++i)
            visited[i] = false;

        // Create a queue, enqueue source vertex and mark
        // source vertex as visited
        LinkedList<Integer> queue
                = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        // Standard BFS Loop
        while (queue.size() != 0) {
            int u = queue.poll();

            for (int v = 0; v < numOfVertices; v++) {
                if (visited[v] == false
                        && rGraph[u][v] > 0) {
                    // If we find a connection to the sink
                    // node, then there is no point in BFS
                    // anymore We just have to set its parent
                    // and can return true
                    if (v == t) {
                        parent[v] = u;
                        return true;
                    }
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        // We didn't reach sink in BFS starting from source,
        // so return false
        return false;
    }

    // Returns tne maximum flow from s to t in the given
    // graph
    static int fordFulkerson(int graph[][], int s, int t)
    // Ford Fulkerson Algorithm code
    {
        int u, v;
        // Create a residual graph and fill the residual
        // graph with given capacities in the original graph
        // as residual capacities in residual graph

        // Residual graph where rGraph[i][j] indicates
        // residual capacity of edge from i to j (if there
        // is an edge. If rGraph[i][j] is 0, then there is
        // not)
        int rGraph[][] = new int[numOfVertices][numOfVertices];

        for (u = 0; u < numOfVertices; u++)
            for (v = 0; v < numOfVertices; v++)
                rGraph[u][v] = graph[u][v];

        // This array is filled by BFS and to store path
        int parent[] = new int[numOfVertices];

        int max_flow = 0; // There is no flow initially

        int counter = 0;


        // Augment the flow while there is path from source
        // to sink
        while (bfs(rGraph, s, t, parent)) {

            counter++;
            // Find minimum residual capacity of the edges
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                path_flow
                        = Math.min(path_flow, rGraph[u][v]);

            }


            ArrayList<String> diplayPaths = new ArrayList<>();
            // update residual capacities of the edges and
            // reverse edges along the path
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= path_flow;
                rGraph[v][u] += path_flow;

                String paths = (u + ">" + v + " ");
                matrix[u][v] += path_flow;
                //adding values to the arraylist
                diplayPaths.add(paths);
            }

            Collections.reverse(diplayPaths);
            System.out.println();
            System.out.println("Augmented path " + counter + " : " + diplayPaths);

            System.out.println("Path flow is: " + path_flow);
            // Add path flow to overall flow
            max_flow += path_flow;
        }

        System.out.println();

        // Return the overall flow
        return max_flow;
    }


    // Finding method is starting..............
    public static void find() {
        System.out.println("     #*#*#*#*#*#*#*#*#*#*  FINDING THE MAXIMUM FLOW  *#*#*#*#*#*#*#*#*#*#*#     ");
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("\n");
        System.out.println("     #####Text File is Reading#####");
        System.out.println("\n");
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("D:\\2nd Year\\Algorithm\\w1790171\\AlgoCW\\bridge_1.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println("Can't find the file!");
        }

        String line = null;
        max = 0;

        try {
            max = Integer.parseInt(br.readLine());
        } catch (IOException exception) {
            System.out.println("Error has occurred!");
        }

        Main graph = new Main(max);


        while (true) {
            try {
                if (!((line = br.readLine()) != null))
                    break;
            } catch (IOException exception) {
                System.out.println("No lines to read!");
            }
            {
                String[] values = line.split(" ");
                graph.addEdge(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
            }
        }
            try {
                br.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            print();
    }

    public static void print() {

        System.out.println("     *** TOTAL  NUMBER  OF  NODES = " + max + " " + "***");
        System.out.println("     *** SOURCE NODE IS = " + 0 + " " + "***");
        System.out.println("     *** TARGET NODE IS = " + (max - 1) + " " + "***");
        System.out.println();




        System.out.println("    ********** Residual ** Network ** Flow ** Graph **********    ");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " " +
                        "");
            }
            System.out.println();
        }

        long start = System.nanoTime();

        System.out.println("    ***  The Maximum  Flow Is "
                + fordFulkerson(matrix, 0, max - 1));

        long end = System.nanoTime();
        /**
         * Calculating Elapsed time of the program
         * Elapsed time = End -  Start
         **/
        double elapsedTime = (double) (end - start);
        System.out.println("  ***  Total Elapsed Time : " + elapsedTime * 0.000000001 * 1000 + " ms" + " " + "***");
        System.out.println("\n");
        System.out.println("---------------------------------------------------------------------------------");

        try {
            console();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
