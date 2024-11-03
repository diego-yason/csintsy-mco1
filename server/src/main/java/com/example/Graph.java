package com.example;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Graph {
    protected ArrayList<Node> nodes;
    protected ArrayList<Edge> edges;

    public Graph() {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
    }

    public void addNode(Place place) {
        nodes.add(new Node(this, place));
    }

    public void addEdge(Node a, Node b, double distance) {
        edges.add(new Edge(a, b, distance));
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public Node getNode(String code) {
        for (Node node : nodes) {
            if (node.code.equals(code)) {
                return node;
            }
        }
        return null;
    }

    public Edge[] getEdges() {
        return edges.toArray(new Edge[edges.size()]);
    }

    public double getDistance(Node current, Node neighbor) {
        for (Edge edge : edges) {
            if (edge.a == current && edge.b == neighbor || edge.a == neighbor && edge.b == current) {
                return edge.distance;
            }
        }
        throw new IllegalArgumentException("No edge between nodes");
    }

    public void printInformation() throws IOException {
        File file = new File("results/trees/graph.gv");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);

        writer.write("digraph G {\n");
        for (Node node : nodes) {
            writer.write(
                    node.code + " [xlabel=<<font color=\"darkgreen\"><B>" + node.getHeuristic() + "</B></font>>];\n");
        }

        for (Edge edge : edges) {
            writer.write(edge.a.code + " -> " + edge.b.code + " [label=\"" + edge.distance + "\"];\n");
        }

        writer.write("}");
        writer.close();
    }
}