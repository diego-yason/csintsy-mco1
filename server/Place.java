package server;

public class Place {
    final public String name;
    final public String code;

    final public int price;
    final public int time;
    final public int s; // TODO: remember wtf this means
    final public int p; // TODO: remember wtf this means
    final public int accessibility;

    private double heuristic;

    public Place(String name, String code, String PTSPA) {
        this.name = name;
        this.code = code;

        String[] PTSPAArray = PTSPA.split("");
        this.price = Integer.parseInt(PTSPAArray[0]);
        this.time = Integer.parseInt(PTSPAArray[1]);
        this.s = Integer.parseInt(PTSPAArray[2]);
        this.p = Integer.parseInt(PTSPAArray[3]);
        this.accessibility = Integer.parseInt(PTSPAArray[4]);
    }

    public Place(Place place) {
        this.name = place.name;
        this.code = place.code;

        this.price = place.price;
        this.time = place.time;
        this.s = place.s;
        this.p = place.p;
        this.accessibility = place.accessibility;

        this.heuristic = place.heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    public double getHeuristic() {
        return heuristic;
    }
}