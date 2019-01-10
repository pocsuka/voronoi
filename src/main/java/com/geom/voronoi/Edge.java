package com.geom.voronoi;

import java.util.Objects;

public class Edge {
    private Vertex start;
    private Vertex end;

    public Edge(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
    }

    Vertex getStart() {
        return start;
    }

    Vertex getEnd() {
        return end;
    }

    private Edge get_reverse() {
        return new Edge(end, start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return (start.equals(edge.start) && end.equals(edge.end)) || (start.equals(edge.end) && end.equals(edge.start));
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "edge: " + start + ", " + end;
    }
}
