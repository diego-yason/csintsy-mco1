public class TreeEdge {
    final public TreeNode a;
    final public TreeNode b;
    final public double distance;

    public TreeEdge(TreeNode node1, TreeNode node2, double distance) {
        this.a = node1;
        this.b = node2;
        this.distance = distance;
    }
}
