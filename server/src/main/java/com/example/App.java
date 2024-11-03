package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.io.FileNotFoundException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {
    public static void serverMain(String[] args) throws FileNotFoundException,
            IOException {
        Graph data = createGraph();

        // print heuristic
        File heuristicFile = new File("results/heuristic.tsv");
        heuristicFile.createNewFile();
        data.nodes.sort((a, b) -> Double.compare(b.getHeuristic(),
                a.getHeuristic()));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(heuristicFile))) {
            for (Node node : data.nodes) {
                writer.write(node.name + "\t" + node.getHeuristic());
                writer.newLine();
            }
        }
        data.printInformation();

        // set up server

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                exchange.sendResponseHeaders(200, 0);
            }
        });

        server.createContext("/search", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // check headers
                // algorithm, start, end
                String algorithm = exchange.getRequestHeaders().getFirst("algorithm");
                String start = exchange.getRequestHeaders().getFirst("start");
                String end = exchange.getRequestHeaders().getFirst("end");

                if (algorithm == null || start == null || end == null) {
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }

                Node[] result = null;
                try {
                    switch (algorithm) {
                        case "bfs":
                            result = Algorithms.BFS(data, data.getNode(start), data.getNode(end));
                            break;
                        case "dfs":
                            result = Algorithms.DFS(data, data.getNode(start), data.getNode(end));
                            break;
                        case "ucs":
                            result = Algorithms.UCS(data, data.getNode(start), data.getNode(end));
                            break;
                        case "gbfs":
                            result = Algorithms.GBFS(data, data.getNode(start), data.getNode(end));
                            break;
                        case "astar":
                            result = Algorithms.AStar(data, data.getNode(start), data.getNode(end));
                            break;
                        default:
                            String response = "Invalid algorithm";
                            exchange.sendResponseHeaders(400, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                            return;
                    }

                    writeResults(result, algorithm + ".tsv");
                    String response = "[";
                    for (Node node : result) {
                        response += node.name + ",";
                    }
                    response = response.substring(0, response.length() - 1) + "]";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (Exception e) {
                    exchange.sendResponseHeaders(500, 0);
                    System.err.println(e);
                    return;
                }
            }
        });

        server.createContext("/heuristics", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                exchange.sendResponseHeaders(501, 0);
            }
        });

        server.start();

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Graph data = createGraph();

        // set heuristics
        for (Node node : data.nodes) {
            node.setHeuristic(
                    node.accessibility * 0.4 +
                            node.p * 1.4 +
                            node.price * 2.4 +
                            node.s * 1.6 +
                            node.time * 2);
        }

        // reverse values
        /// find largest heuristic
        double largest = 0;
        for (Node node : data.nodes) {
            if (node.getHeuristic() > largest) {
                largest = node.getHeuristic();
            }
        }
        // reverse values
        for (Node node : data.nodes) {
            node.setHeuristic(Math.abs(largest - node.getHeuristic()));
        }

        // print heuristic
        File heuristicFile = new File("results/heuristic.tsv");
        heuristicFile.createNewFile();
        data.nodes.sort((a, b) -> Double.compare(b.getHeuristic(), a.getHeuristic()));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(heuristicFile))) {
            for (Node node : data.nodes) {
                writer.write(node.name + "\t" + node.getHeuristic());
                writer.newLine();
            }
        }

        // sample run
        Node start = data.getNode("Belgan");
        Node end = data.nodes.get(data.nodes.size() - 1);

        Node[] result = Algorithms.Backtracking(data, start, end);
        writeResults(result, "backtracking.txt");

        result = Algorithms.DFS(data, start, end);
        writeResults(result, "dfs.txt");

        result = Algorithms.BFS(data, start, end);
        writeResults(result, "bfs.txt");

        result = Algorithms.UCS(data, start, end);
        writeResults(result, "ucs.txt");

        result = Algorithms.GBFS(data, start, end);
        writeResults(result, "gbfs.txt");

        result = Algorithms.AStar(data, start, end);
        writeResults(result, "astar.txt");

        data.printInformation();

        System.out.println("Done");

    }

    private static Graph createGraph() throws FileNotFoundException {
        Graph data = new Graph();

        File dataFile = new File("data.tsv");
        File pathFile = new File("path.tsv");

        Scanner scanner = new Scanner(dataFile);
        // skip header line
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t");
            data.addNode(new Place(line[1], line[0], line[2], line[3], line[4]));
        }
        scanner.close();

        scanner = new Scanner(pathFile);
        // skip header line
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t");
            Node a = data.getNode(line[0]);
            Node b = data.getNode(line[1]);
            data.addEdge(a, b, Double.parseDouble(line[2]));
        }
        scanner.close();

        return data;
    }

    private static void writeResults(Node[] result, String fileName) throws IOException {
        File file = new File("results/" + fileName);
        file.createNewFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Node node : result) {
                writer.write(node.name);
                writer.newLine();
            }
        }
    }
}
