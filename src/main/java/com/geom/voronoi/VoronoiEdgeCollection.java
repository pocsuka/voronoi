package com.geom.voronoi;

import java.util.ArrayList;
import java.util.List;

public class VoronoiEdgeCollection {
    private List<Edge> edges;

    VoronoiEdgeCollection() {
        this.edges = new ArrayList<>();
    }

    private boolean hasEdge(Edge query_edge) {
        for (Edge edge : getEdges()) {
            if (edge.equals(query_edge)) {
                return true;
            }
        }
        return false;
    }

    void insertIfDoesNotExist(Edge edge) {
        if (!hasEdge(edge)) {
            addEdge(edge);
        }
    }

    private void addEdge(Edge edge) {
        edges.add(edge);
    }

    List<Edge> getEdges() {
        return edges;
    }

    String edgesDebugString() {
        StringBuilder s = new StringBuilder();
        for (Edge edge : edges) {
            s.append(edge).append("\n");
        }
        return s.toString();
    }
}
