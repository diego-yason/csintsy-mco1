import java.util.ArrayList;

public class Tree extends Graph {
    final public TreeNode root;

    public Tree(Node root) {
        super();
        this.root = addNode(root, null);
    }

    public TreeNode addNode(Node node, TreeNode parent) {
        TreeNode newNode = new TreeNode(this, node, parent);
        nodes.add(newNode);
        return newNode;
    }

    public void addEdge(Node a, Node b, double distance) {
        edges.add(new Edge(a, b, distance));
    }

    public boolean contains(Node neighbor) {
        return nodes.contains(neighbor);
    }

    public Node[] getPathToRoot(TreeNode node) {
        ArrayList<Node> path = new ArrayList<Node>();
        do {
            path.add(node);
            node = node.parent;
        } while (node != null);

        // reverse array
        Node[] pathArray = new Node[path.size()];
        for (int i = 0; i < path.size(); i++) {
            pathArray[i] = path.get(path.size() - i - 1);
        }

        return pathArray;
    }
}
