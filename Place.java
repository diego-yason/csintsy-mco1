public class Place {
    final public String name;
    final public String code;
    private double heuristic;

    public Place(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Place(Place place) {
        this.name = place.name;
        this.code = place.code;
        this.heuristic = place.heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    public double getHeuristic() {
        return heuristic;
    }
}