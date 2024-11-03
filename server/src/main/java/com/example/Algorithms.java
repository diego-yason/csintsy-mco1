package com.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Algorithms {
    public static Node[] BFS(Graph graph, Node start, Node goal) throws IOException {
        // statistics for paper
        int nodesVisited = 0;
        int nodesCreated = 0;
        int edgesAnalyzed = 0;
        long startTime = System.nanoTime();

        Tree tree = new Tree(start);
        nodesCreated++;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(tree.root);

        TreeNode goalTree = null;
        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            nodesVisited++;
            if (current.node.equals(goal)) {
                goalTree = current;
                break;
            }

            Node[] neighbors = current.node.getNeighbors();
            for (Node neighbor : neighbors) {
                edgesAnalyzed++;
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, current);
                    nodesCreated++;
                    tree.addEdge(current, neighborTN, graph.getDistance(current.node, neighbor));
                    queue.add(neighborTN);
                }
            }
        }

        tree.printInformation(graph, "bfs");

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;

        File file = new File("results/statistics.tsv");
        FileWriter writer = new FileWriter(file, true);
        writer.append(
                "BFS\t" + System.currentTimeMillis() + "\t" + nodesVisited + "\t" + nodesCreated + "\t" + edgesAnalyzed
                        + "\t" + elapsedTime + "\n");
        writer.close();

        return tree.getPathToRoot(goalTree);
    }

    public static Node[] DFS(Graph graph, Node start, Node goal) throws IOException {
        // statistics for paper
        int nodesVisited = 0;
        int nodesCreated = 0;
        int edgesAnalyzed = 0;
        long startTime = System.nanoTime();

        Tree tree = new Tree(start);
        nodesCreated++;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(tree.root);

        TreeNode goalTreeNode = null;
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            nodesVisited++;
            if (current.node.equals(goal)) {
                goalTreeNode = current;
                break;
            }

            Node[] neighbors = current.node.getNeighbors();
            for (Node neighbor : neighbors) {
                edgesAnalyzed++;
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, current);
                    nodesCreated++;
                    tree.addEdge(current, neighborTN, graph.getDistance(current.node, neighbor));
                    stack.push(neighborTN);
                }
            }
        }

        tree.printInformation(graph, "dfs");

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;

        File file = new File("results/statistics.tsv");
        FileWriter writer = new FileWriter(file, true);
        writer.append(
                "DFS\t" + System.currentTimeMillis() + "\t" + nodesVisited + "\t" + nodesCreated + "\t" + edgesAnalyzed
                        + "\t" + elapsedTime + "\n");
        writer.close();

        return tree.getPathToRoot(goalTreeNode);
    }

    public static Node[] Backtracking(Graph graph, Node start, Node goal) throws IOException {
        // statistics for paper
        int nodesVisited = 0;
        int nodesCreated = 0;
        int edgesAnalyzed = 0;
        long startTime = System.nanoTime();

        Tree tree = new Tree(start);
        nodesCreated++;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(tree.root);

        TreeNode goalTreeNode = null;
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            nodesVisited++;

            if (current.node.equals(goal)) {
                goalTreeNode = current;
            }

            Node[] neighbors = current.node.getNeighbors();
            for (Node neighbor : neighbors) {
                edgesAnalyzed++;
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, current);
                    nodesCreated++;
                    tree.addEdge(current, neighborTN, graph.getDistance(current.node, neighbor));
                    stack.push(neighborTN);
                }
            }
        }

        tree.printInformation(graph, "backtracking");

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;

        File file = new File("results/statistics.tsv");
        FileWriter writer = new FileWriter(file, true);
        writer.append(
                "Backtracking\t" + System.currentTimeMillis() + "\t" + nodesVisited + "\t" + nodesCreated + "\t"
                        + edgesAnalyzed
                        + "\t" + elapsedTime + "\n");
        writer.close();

        return tree.getPathToRoot(goalTreeNode);
    }

    public static Node[] GBFS(Graph graph, Node start, Node end) throws IOException {
        // statistics for paper
        int nodesVisited = 0;
        int nodesCreated = 0;
        int edgesAnalyzed = 0;
        long startTime = System.nanoTime();

        Tree tree = new Tree(start);
        nodesCreated++;

        TreeNode current = tree.root;
        ArrayList<TreeNode> toBeVisited = new ArrayList<>();
        while (current.node != end) {
            Node[] neighbors = current.node.getNeighbors();
            nodesVisited++;

            for (Node neighbor : neighbors) {
                edgesAnalyzed++;
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, current);
                    nodesCreated++;
                    tree.addEdge(current, neighborTN, graph.getDistance(current.node, neighbor));
                    toBeVisited.add(neighborTN);

                }
            }
            toBeVisited.sort((a, b) -> Double.compare(a.node.getHeuristic(), b.node.getHeuristic()));
            current = toBeVisited.remove(0);
        }

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;

        tree.printInformation(graph, "gbfs");

        File file = new File("results/statistics.tsv");
        FileWriter writer = new FileWriter(file, true);
        writer.append(
                "GBFS\t" + System.currentTimeMillis() + "\t" + nodesVisited + "\t" + nodesCreated + "\t" + edgesAnalyzed
                        + "\t" + elapsedTime + "\n");
        writer.close();

        return tree.getPathToRoot(current);
    }

    public static Node[] AStar(Graph graph, Node start, Node end) throws IOException {
        // statistics for paper
        int nodesVisited = 0;
        int nodesCreated = 0;
        int edgesAnalyzed = 0;
        long startTime = System.nanoTime();

        Tree tree = new Tree(start);
        nodesCreated++;

        ArrayList<TreeNode> toBeVisited = new ArrayList<>();
        Map<Node, Double> cumulativeCost = new java.util.HashMap<>();

        TreeNode current = tree.root;
        cumulativeCost.put(start, 0.0);
        while (current.node != end) {
            Node[] neighbors = current.node.getNeighbors();
            nodesVisited++;

            for (Node neighbor : neighbors) {
                edgesAnalyzed++;
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, current);
                    nodesCreated++;
                    tree.addEdge(current, neighborTN, graph.getDistance(current.node, neighbor));
                    toBeVisited.add(neighborTN);
                    cumulativeCost.put(neighbor,
                            cumulativeCost.get(current.node) + graph.getDistance(current.node, neighbor));
                }
            }

            toBeVisited.sort((a, b) -> Double.compare(a.node.getHeuristic() + cumulativeCost.get(a.node),
                    b.node.getHeuristic() + cumulativeCost.get(b.node)));
            current = toBeVisited.remove(0);
        }

        tree.printInformation(graph, "astar");

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;

        File file = new File("results/statistics.tsv");
        FileWriter writer = new FileWriter(file, true);
        writer.append(
                "AStar\t" + System.currentTimeMillis() + "\t" + nodesVisited + "\t" + nodesCreated + "\t"
                        + edgesAnalyzed
                        + "\t" + elapsedTime + "\n");
        writer.close();
        return tree.getPathToRoot(current);
    }

    public static Node[] UCS(Graph graph, Node start, Node end) throws IOException {
        // statistics for paper
        int nodesVisited = 0;
        int nodesCreated = 0;
        int edgesAnalyzed = 0;
        long startTime = System.nanoTime();

        Tree tree = new Tree(start);
        nodesCreated++;
        NodeTripletList cumulativeList = new NodeTripletList();
        ArrayList<TreeNode> toBeVisited = new ArrayList<>();
        ArrayList<TreeNode> visited = new ArrayList<>();

        double previousCost = 0;
        TreeNode visiting = tree.root;
        nodesVisited++;
        while (!visiting.node.equals(end)) {
            Node[] neighbors = visiting.node.getNeighbors();
            for (Node neighbor : neighbors) {
                edgesAnalyzed++;
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, visiting);
                    nodesCreated++;
                    tree.addEdge(visiting, neighborTN, graph.getDistance(visiting.node, neighbor));
                    toBeVisited.add(neighborTN);
                    cumulativeList.add(new NodeTriplet(visiting.node, neighbor,
                            previousCost + graph.getDistance(visiting.node, neighbor)));
                }
            }

            visited.add(visiting);
            toBeVisited.remove(visiting);
            toBeVisited.sort((a, b) -> {
                return Double.compare(cumulativeList.find(a.parent.node, a.node).getValue(),
                        cumulativeList.find(b.parent.node, b.node).getValue());
            });

            visiting = toBeVisited.get(0);
            nodesVisited++;
            previousCost = cumulativeList.find(visiting.parent.node, visiting.node).getValue();
        }

        tree.printInformation(graph, "ucs");

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;

        File file = new File("results/statistics.tsv");
        FileWriter writer = new FileWriter(file, true);
        writer.append(
                "UCS\t" + System.currentTimeMillis() + "\t" + nodesVisited + "\t" + nodesCreated + "\t" + edgesAnalyzed
                        + "\t" + elapsedTime + "\n");
        writer.close();

        return tree.getPathToRoot(visiting);
    }
}

class NodeTriplet {
    private Node a;
    private Node b;
    private double value;

    public NodeTriplet(Node a, Node b, double value) {
        this.a = a;
        this.b = b;
        this.value = value;
    }

    public boolean isSame(Node a, Node b) {
        return (this.a.equals(a) && this.b.equals(b)) || (this.a.equals(b) && this.b.equals(a));
    }

    public double getValue() {
        return value;
    }
}

class NodeTripletList extends ArrayList<NodeTriplet> {
    public NodeTriplet find(Node a, Node b) {
        for (NodeTriplet triplet : this) {
            if (triplet.isSame(a, b)) {
                return triplet;
            }
        }
        return null;
    }
}