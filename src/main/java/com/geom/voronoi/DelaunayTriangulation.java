package com.geom.voronoi;

import javafx.util.Pair;
import org.kynosarges.tektosyne.geometry.GeoUtils;
import org.kynosarges.tektosyne.geometry.PointD;
import org.kynosarges.tektosyne.geometry.PolygonLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DelaunayTriangulation {
    private final List<DelaunayTriangle> triangulation;

    public DelaunayTriangulation() {
        triangulation = new ArrayList<>();
    }

    List<DelaunayTriangle> get_valid_triangles() {
        return triangulation.stream()
            .filter(triangle -> triangle.is_valid)
            .collect(Collectors.toList());
    }

    void insert(DelaunayTriangle delaunayTriangle) {
        triangulation.add(delaunayTriangle);
    }

    String debugString() {
        StringBuilder s = new StringBuilder();
        for (DelaunayTriangle triangle : triangulation) {
            s.append(triangle).append("\n");
        }
        return s.toString();
    }

    String validTrianglesDebugString() {
        StringBuilder s = new StringBuilder();
        for (DelaunayTriangle triangle : get_valid_triangles()) {
            s.append("triangle: ")
                .append(triangle.point_a)
                .append(", ")
                .append(triangle.point_b)
                .append(", ")
                .append(triangle.point_c)
                .append("\n");
        }
        return s.toString();
    }

    Optional<DelaunayTriangle> find_triangle_with_vertices(List<CoordinatePoint> vertices) {
        for (DelaunayTriangle triangle : get_valid_triangles()) {
            if (triangle.points().containsAll(vertices)) {
                return Optional.of(triangle);
            }
        }
        return Optional.empty();
    }

    LocationQueryMatch find_surrounding_triangle(CoordinatePoint mpoint) {
        PointD point = mpoint.getPointD();

        for (DelaunayTriangle triangle : get_valid_triangles()) {
            PointD[] polygon_of_triangle = new PointD[]{
                triangle.point_a.getPointD(), triangle.point_b.getPointD(), triangle.point_c.getPointD()
            };
            PolygonLocation result = GeoUtils.pointInPolygon(point, polygon_of_triangle);
            if (result == PolygonLocation.INSIDE) {
                return new LocationQueryMatch(PolygonLocation.INSIDE, Optional.of(triangle), Optional.empty());
            } else if (result == PolygonLocation.EDGE || result == PolygonLocation.VERTEX) {
                Optional<Pair<CoordinatePoint, CoordinatePoint>> edge = get_incident_edge_of_point(mpoint, triangle);
                if (edge.isPresent()) {
                    return new LocationQueryMatch(PolygonLocation.EDGE, Optional.of(triangle), edge);
                } else {
                    System.err.println("Could not find incident edge for point.");
                }
            }
        }
        System.err.println("Expected to find containing triangle for " + point);
        return new LocationQueryMatch(PolygonLocation.OUTSIDE, Optional.empty(), Optional.empty());
    }

    private Optional<Pair<CoordinatePoint, CoordinatePoint>> get_incident_edge_of_point(CoordinatePoint mpoint, DelaunayTriangle triangle) {
        PointD point = mpoint.getPointD();

        PolygonLocation edge_ab_test = GeoUtils.pointInPolygon(point, new PointD[]{triangle.point_a.getPointD(), triangle.point_b.getPointD()});
        if (edge_ab_test == PolygonLocation.EDGE || edge_ab_test == PolygonLocation.VERTEX) {
            return Optional.of(new Pair<>(triangle.point_a, triangle.point_b));
        }
        PolygonLocation edge_ac_test = GeoUtils.pointInPolygon(point, new PointD[]{triangle.point_a.getPointD(), triangle.point_c.getPointD()});
        if (edge_ac_test == PolygonLocation.EDGE || edge_ac_test == PolygonLocation.VERTEX) {
            return Optional.of(new Pair<>(triangle.point_a, triangle.point_c));
        }
        PolygonLocation edge_bc_test = GeoUtils.pointInPolygon(point, new PointD[]{triangle.point_b.getPointD(), triangle.point_c.getPointD()});
        if (edge_bc_test == PolygonLocation.EDGE || edge_bc_test == PolygonLocation.VERTEX) {
            return Optional.of(new Pair<>(triangle.point_b, triangle.point_c));
        }

        return Optional.empty();
    }

    // Find the triangle neighboring triangle over the edge (vertex_a, vertex_b)
    Optional<DelaunayTriangle> adjacent_triangle_over_edge(DelaunayTriangle triangle, CoordinatePoint vertex_a, CoordinatePoint vertex_b) {
        CoordinatePoint initialPoint = triangle.getRemainingPoint(vertex_a, vertex_b);

        for (DelaunayTriangle other_triangle : get_valid_triangles()) {
            if (other_triangle.points().containsAll(Arrays.asList(vertex_a, vertex_b))) {
                if (other_triangle.getRemainingPoint(vertex_a, vertex_b) != initialPoint) {
                    return Optional.of(other_triangle);
                }
            }
        }
        return Optional.empty();
    }

    // Returns point_k != point_r, which is the remaining defining point of triangle (point_i, point_j, point_k)
    Optional<CoordinatePoint> adjacent_triangle_point(CoordinatePoint point_r, CoordinatePoint point_i, CoordinatePoint point_j) {
        // TODO way too slow
        for (DelaunayTriangle triangle : get_valid_triangles()) {
            if (triangle.hasPoint(point_i) && triangle.hasPoint(point_j)) {
                CoordinatePoint other_point = triangle.getRemainingPoint(point_i, point_j);
                if (other_point != point_r) {
                    return Optional.of(other_point);
                }
            }
        }
        return Optional.empty();
    }

    void removeIncidentTriangles(List<CoordinatePoint> omegas) {
        for (CoordinatePoint omega : omegas) {
            for (DelaunayTriangle triangle : get_valid_triangles()) {
                if (triangle.hasPoint(omega)) {
                    triangle.is_valid = false;
                }
            }
        }
    }
}
