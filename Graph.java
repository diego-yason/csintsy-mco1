import java.util.ArrayList;

public class Graph {
    protected ArrayList<Node> nodes;
    protected ArrayList<Edge> edges;

    public Graph() {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
    }

    public void addNode(Place place) {
        nodes.add(new Node(this, place));
    }

    public void addEdge(Node a, Node b, double distance) {
        edges.add(new Edge(a, b, distance));
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public Node searchNode(String code) {
        for (Node node : nodes) {
            if (node.code.equals(code)) {
                return node;
            }
        }
        return null;
    }

    public Edge[] getEdges() {
        return edges.toArray(new Edge[edges.size()]);
    }

    public double getDistance(Node current, Node neighbor) {
        for (Edge edge : edges) {
            if (edge.a == current && edge.b == neighbor) {
                return edge.distance;
            }
        }
        return 0;
    }
}