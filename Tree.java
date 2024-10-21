import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class Tree {
    private ArrayList<TreeNode> nodes;
    private ArrayList<TreeEdge> edges;
    final public TreeNode root;

    public Tree(Node root) {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.root = addNode(root, null);
    }

    public TreeNode addNode(Node node, TreeNode parent) {
        TreeNode newNode = new TreeNode(this, node, parent);
        nodes.add(newNode);
        return newNode;
    }

    public void addEdge(TreeNode a, TreeNode b, double distance) {
        edges.add(new TreeEdge(a, b, distance));
    }

    public boolean contains(Node neighbor) {
        for (TreeNode node : nodes) {
            if (node.node.equals(neighbor)) {
                return true;
            }
        }
        return false;
    }

    public Node[] getPathToRoot(TreeNode node) {
        ArrayList<TreeNode> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }

        // reverse array
        Node[] pathArray = new Node[path.size()];
        for (int i = 0; i < path.size(); i++) {
            pathArray[i] = path.get(path.size() - i - 1).node;
        }

        return pathArray;
    }

    public void printInformation(Graph graph, String algorithm) throws IOException {

        File file = new File("results/trees/" + algorithm + ".txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);

        for (TreeNode node : nodes) {
            writer.write(node.node.code + " [xlabel=\"" + node.node.getHeuristic() + "\"];\n");
        }

        for (TreeNode node : nodes) {
            if (node.parent != null)
                writer.write(node.parent.node.code + " -> " + node.node.code + " [label=\""
                        + graph.getDistance(node.parent.node, node.node) + "\"];\n");
        }

        writer.close();
    }
}
