package com.example;

public class Place {
    final public String name;
    final public String code;

    final public int opening;
    final public int closing;

    final public int ambiance;
    final public int service;
    final public int location; // TODO: remember wtf this means
    final public int price; // TODO: remember wtf this means
    final public int taste;

    private double heuristic;

    public Place(String name, String code, String PTSPA, String opening, String closing) {
        this.name = name;
        this.code = code;

        String[] PTSPAArray = PTSPA.split("");
        this.ambiance = Integer.parseInt(PTSPAArray[0]) + 1;
        this.service = Integer.parseInt(PTSPAArray[1]) + 1;
        this.location = Integer.parseInt(PTSPAArray[2]) + 1;
        this.price = Integer.parseInt(PTSPAArray[3]) + 1;
        this.taste = Integer.parseInt(PTSPAArray[4]) + 1;

        this.opening = Integer.parseInt(opening);
        this.closing = Integer.parseInt(closing);
    }

    public Place(Place place) {
        this.name = place.name;
        this.code = place.code;

        this.opening = place.opening;
        this.closing = place.closing;

        this.ambiance = place.ambiance;
        this.service = place.service;
        this.location = place.location;
        this.price = place.price;
        this.taste = place.taste;

        this.heuristic = place.heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    public double getHeuristic() {
        return heuristic;
    }
}