package server;

public class TreeNode {
    final public TreeNode parent;
    final public Node node;

    public TreeNode(Tree graph, Node node, TreeNode parent) {
        this.node = node;
        this.parent = parent;
    }

    public boolean equals(Node node) {
        return super.equals(node);
    }
}
