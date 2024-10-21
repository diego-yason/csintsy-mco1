public class Edge {
    final public Node a;
    final public Node b;
    final public double distance;

    public Edge(Node node1, Node node2, double distance) {
        this.a = node1;
        this.b = node2;
        this.distance = distance;
    }
}
