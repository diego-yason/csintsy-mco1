package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String s, request = "";
            while ((s = in.readLine()) != null) {
                System.out.println(s);
                request += s;
                if (s.isEmpty()) {
                    break;
                }
            }

            // Parse JSON request
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, Object> jsonMap = objectMapper.readValue(request, Map.class);
                System.out.println("Parsed JSON: " + jsonMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
