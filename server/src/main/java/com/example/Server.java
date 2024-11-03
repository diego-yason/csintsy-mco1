package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Server {
    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = null;
        do {
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                port++;
            }
        } while (serverSocket == null);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            int contentLength = 0;

            // Read the HTTP request headers
            while (!(line = in.readLine()).isEmpty()) {
                requestBuilder.append(line).append("\n");
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            // Read the HTTP request body (JSON payload)
            char[] charArray = new char[contentLength];
            in.read(charArray, 0, contentLength);
            String requestBody = new String(charArray);

            // Parse JSON request
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> json = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {
            });

            // Process request
            Graph data = App.createGraph();
            int taste = (int) json.get("taste");
            int price = (int) json.get("price");
            int ambiance = (int) json.get("ambiance");
            int location = (int) json.get("location");
            int service = (int) json.get("service");
            App.createHeuristics(data, taste, price, ambiance, location, service);

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

            // Print general graph
            data.printInformation();

            // Start algorithm
            Node[] result = null;
            String algorithm = (String) json.get("algorithm");
            Node start = data.getNode((String) json.get("start"));
            Node end = data.getBestNode();

            switch (algorithm) {
                case "bfs":
                    result = Algorithms.BFS(data, start, end);
                    break;
                case "dfs":
                    result = Algorithms.DFS(data, start, end);
                    break;
                case "backtracking":
                    result = Algorithms.Backtracking(data, start, end);
                    break;
                case "astar":
                    result = Algorithms.AStar(data, start, end);
                    break;
                case "gbfs":
                    result = Algorithms.GBFS(data, start, end);
                    break;
                case "ucs":
                    result = Algorithms.UCS(data, start, end);
                    break;
            }

            // get path total
            double pathTotal = 0;
            for (int i = 0; i < result.length - 1; i++) {
                Edge[] edges = data.getEdges();
                for (Edge edge : edges) {
                    if (edge.a == result[i] && edge.b == result[i + 1]
                            || edge.b == result[i] && edge.a == result[i + 1]) {
                        pathTotal += edge.distance;
                    }
                }
            }

            // Create JSON response
            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append("{\"total\": ").append(pathTotal).append(", \"array\": [");
            for (Node node : result) {
                responseBuilder.append("\"").append(node.name).append("\",");
            }
            if (result.length > 0) {
                responseBuilder.setLength(responseBuilder.length() - 1); // Remove the trailing comma
            }
            responseBuilder.append("]}");
            String response = responseBuilder.toString();

            // Send to client
            String header = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "Content-Length: " + response.getBytes().length + "\r\n"
                    + "Access-Control-Allow-Origin: *\r\n"
                    + "Connection: close\r\n\r\n";
            clientSocket.getOutputStream().write(header.getBytes());
            clientSocket.getOutputStream().write(response.getBytes());
            clientSocket.close();
        }
    }
}
