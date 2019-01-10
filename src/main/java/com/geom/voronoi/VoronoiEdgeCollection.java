package com.geom.voronoi;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class VoronoiEdgeCollection {
    private List<Pair<CoordinatePoint, CoordinatePoint>> edges;

    VoronoiEdgeCollection() {
        this.edges = new ArrayList<>();
    }

    private boolean hasEdge(Pair<CoordinatePoint, CoordinatePoint> query_edge) {
        for (Pair<CoordinatePoint, CoordinatePoint> edge : edges) {
            if (isEqual(edge, query_edge)) {
                return true;
            }
        }
        return false;
    }

    void insertIfDoesNotExist(Pair<CoordinatePoint, CoordinatePoint> edge) {
        if (!hasEdge(edge)) {
            addEdge(edge);
        }
    }

    private void addEdge(Pair<CoordinatePoint, CoordinatePoint> edge) {
        edges.add(edge);
    }

    List<Pair<CoordinatePoint, CoordinatePoint>> getEdges() {
        return edges;
    }

    private Pair<CoordinatePoint, CoordinatePoint> reverse(Pair<CoordinatePoint, CoordinatePoint> edge) {
        return new Pair<>(edge.getValue(), edge.getKey());
    }

    private boolean isEqual(Pair<CoordinatePoint, CoordinatePoint> edge_a, Pair<CoordinatePoint, CoordinatePoint> edge_b) {
        return edge_a.equals(edge_b) || edge_a.equals(reverse(edge_b));
    }

    String edgesDebugString() {
        StringBuilder s = new StringBuilder();
        for (Pair<CoordinatePoint, CoordinatePoint> edge : edges) {
            s.append("edge: ").append(edge.getKey()).append(", ").append(edge.getValue()).append("\n");
        }
        return s.toString();
    }
}
