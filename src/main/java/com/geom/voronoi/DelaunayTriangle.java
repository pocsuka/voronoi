package com.geom.voronoi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DelaunayTriangle {
    Vertex point_a;
    Vertex point_b;
    Vertex point_c;

    boolean is_valid;

    DelaunayTriangle(Vertex point_a, Vertex point_b, Vertex point_c, boolean is_valid) {
        this.point_a = point_a;
        this.point_b = point_b;
        this.point_c = point_c;
        this.is_valid = is_valid;
    }

    List<Vertex> points() {
        return Arrays.asList(point_a, point_b, point_c);
    }

    boolean hasPoint(Vertex point) {
        return point_a == point || point_b == point || point_c == point;
    }

    Vertex getRemainingPoint(Vertex point_x, Vertex point_y) {
        List<Vertex> all_points = new ArrayList<>(points());
        all_points.remove(point_x);
        all_points.remove(point_y);
        return all_points.get(0);
    }

    @Override
    public String toString() {
        return "DelaunayTriangle{" +
            "a:" + point_a +
            ", b:" + point_b +
            ", c:" + point_c +
            ", is_valid=" + is_valid +
            '}';
    }

    Vertex getCentroid() {
        return Vertex.getCentroid(Arrays.asList(point_a, point_b, point_c));
    }
}
