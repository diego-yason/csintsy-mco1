public class TreeNode extends Node {
    final public TreeNode parent;

    public TreeNode(Graph graph, Node node, TreeNode parent) {
        super(graph, node);
        this.parent = parent;
    }

    public boolean equals(Node node) {
        return super.equals(node);
    }
}
