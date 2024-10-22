package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.io.FileNotFoundException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Graph data = createGraph();

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
        data.printInformation();

        // set up server

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/heuristic", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
            }
        });

        server.createContext("/run", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
            }
        });
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
            data.addNode(new Place(line[1], line[0], line[2]));
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

        // sample heuristic
        for (Node node : data.nodes) {
            node.setHeuristic(
                    node.accessibility * 0.4 +
                            node.p * 1.4 +
                            node.price * 2.4 +
                            node.s * 1.6 +
                            node.time * 2);
        }

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
