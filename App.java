import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class App {
    public static void main(String[] args) throws FileNotFoundException, IOException {
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

        // sample run
        Node start = data.getNode("Infinit_OAP");
        Node end = data.getNode("BonChon");

        Node[] result = Algorithms.Backtracking(data, start, end);
        writeResults(result, "backtracking.txt");

        result = Algorithms.DFS(data, start, end);
        writeResults(result, "dfs.txt");

        result = Algorithms.BFS(data, start, end);
        writeResults(result, "bfs.txt");

        result = Algorithms.UCS(data, start, end);
        writeResults(result, "ucs.txt");

        // result = Algorithms.GBFS(data, start, end);
        // writeResults(result, "gbfs.txt");

        // result = Algorithms.AStar(data, start, end);
        // writeResults(result, "astar.txt");

        data.printInformation();

        System.out.println("Done");

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
