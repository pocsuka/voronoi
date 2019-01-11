package com.geom.voronoi.data;

import org.kynosarges.tektosyne.geometry.LineD;

public class LinePointDistanceWrapper implements Comparable<LinePointDistanceWrapper> {

    private LineD edge;
    private double distance;

    public LinePointDistanceWrapper(LineD edge, double distance) {
        this.edge = edge;
        this.distance = distance;
    }

    public LineD getEdge() {
        return edge;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(LinePointDistanceWrapper other) {
        return Double.compare(this.distance, other.distance);
    }

}
