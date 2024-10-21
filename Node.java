import java.util.ArrayList;

public class Node extends Place {
    private Graph graph;

    public Node(Graph graph, Place node) {
        super(node);
        this.graph = graph;
    }

    public Node[] getNeighbors() {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        for (Edge edge : graph.getEdges()) {
            if (edge.a == this) {
                neighbors.add(edge.b);
            } else if (edge.b == this) {
                neighbors.add(edge.a);
            }
        }
        return neighbors.toArray(new Node[neighbors.size()]);
    }
}
