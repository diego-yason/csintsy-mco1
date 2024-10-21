import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.HashMap;

public class Algorithms {
    public static Node[] BFS(Graph graph, Node start, Node goal) {
        Tree tree = new Tree(start);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(tree.root);

        TreeNode goalTree = null;
        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            if (current.node.equals(goal)) {
                goalTree = current;
                break;
            }

            Node[] neighbors = current.node.getNeighbors();
            for (Node neighbor : neighbors) {
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, current);
                    tree.addEdge(current, neighborTN, graph.getDistance(current.node, neighbor));
                    queue.add(neighborTN);
                }
            }
        }

        try {
            tree.printInformation(graph, "bfs");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tree.getPathToRoot(goalTree);
    }

    public static Node[] DFS(Graph graph, Node start, Node goal) {
        Tree tree = new Tree(start);
        Stack<TreeNode> stack = new Stack<>();
        stack.push(tree.root);

        TreeNode goalTreeNode = null;
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            if (current.node.equals(goal)) {
                goalTreeNode = current;
                break;
            }

            Node[] neighbors = current.node.getNeighbors();
            for (Node neighbor : neighbors) {
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, current);
                    tree.addEdge(current, neighborTN, graph.getDistance(current.node, neighbor));
                    stack.push(neighborTN);
                }
            }
        }

        try {
            tree.printInformation(graph, "dfs");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tree.getPathToRoot(goalTreeNode);
    }

    public static Node[] Backtracking(Graph graph, Node start, Node goal) {
        Tree tree = new Tree(start);

        Stack<TreeNode> stack = new Stack<>();
        stack.push(tree.root);

        TreeNode goalTreeNode = null;
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();

            if (current.node.equals(goal)) {
                goalTreeNode = current;
            }

            Node[] neighbors = current.node.getNeighbors();
            for (Node neighbor : neighbors) {
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, current);
                    tree.addEdge(current, neighborTN, graph.getDistance(current.node, neighbor));
                    stack.push(neighborTN);
                }
            }
        }

        try {
            tree.printInformation(graph, "backtracking");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tree.getPathToRoot(goalTreeNode);
    }

    public static Node[] GBFS(Graph graph, Node start, Node end) {
        // Tree tree = new Tree(start);
        throw new UnsupportedOperationException("Unimplemented method 'GBFS'");
    }

    public static Node[] AStar(Graph graph, Node start, Node end) {
        // TODO Auto-generated method stub

        throw new UnsupportedOperationException("Unimplemented method 'AStar'");
    }

    public static Node[] UCS(Graph graph, Node start, Node end) {
        Tree tree = new Tree(start);
        NodeTripletList cumulativeList = new NodeTripletList();
        ArrayList<TreeNode> toBeVisited = new ArrayList<>();
        ArrayList<TreeNode> visited = new ArrayList<>();

        double previousCost = 0;
        TreeNode visiting = tree.root;
        while (!visiting.node.equals(end)) {
            Node[] neighbors = visiting.node.getNeighbors();
            for (Node neighbor : neighbors) {
                if (!tree.contains(neighbor)) {
                    TreeNode neighborTN = tree.addNode(neighbor, visiting);
                    tree.addEdge(visiting, neighborTN, graph.getDistance(visiting.node, neighbor));
                    toBeVisited.add(neighborTN);
                    cumulativeList.add(new NodeTriplet(visiting.node, neighbor,
                            previousCost + graph.getDistance(visiting.node, neighbor)));
                }
            }

            visited.add(visiting);
            toBeVisited.remove(visiting);
            final TreeNode currentVisiting = visiting;
            toBeVisited.sort((a, b) -> {
                return Double.compare(cumulativeList.find(a.parent.node, a.node).getValue(),
                        cumulativeList.find(b.parent.node, b.node).getValue());
            });

            visiting = toBeVisited.get(0);
            previousCost = cumulativeList.find(visiting.parent.node, visiting.node).getValue();
        }

        try {
            tree.printInformation(graph, "ucs");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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