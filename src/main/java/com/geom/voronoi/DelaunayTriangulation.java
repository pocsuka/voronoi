package com.geom.voronoi;

import org.kynosarges.tektosyne.geometry.*;

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

    Optional<DelaunayTriangle> find_triangle_with_vertices(List<Vertex> vertices) {
        for (DelaunayTriangle triangle : get_valid_triangles()) {
            if (triangle.points().containsAll(vertices)) {
                return Optional.of(triangle);
            }
        }
        return Optional.empty();
    }

    LocationQueryMatch find_surrounding_triangle(Vertex mpoint) {
        PointD point = mpoint.getPointD();

        for (DelaunayTriangle triangle : get_valid_triangles()) {
            PointD[] polygon_of_triangle = new PointD[]{
                triangle.point_a.getPointD(), triangle.point_b.getPointD(), triangle.point_c.getPointD()
            };
            PolygonLocation result = GeoUtils.pointInPolygon(point, polygon_of_triangle);
            if (result == PolygonLocation.INSIDE) {
                return new LocationQueryMatch(PolygonLocation.INSIDE, Optional.of(triangle), Optional.empty());
            } else if (result == PolygonLocation.EDGE || result == PolygonLocation.VERTEX) {
                Optional<Edge> edge = get_incident_edge_of_point(mpoint, triangle);
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

    private Optional<Edge> get_incident_edge_of_point(Vertex mpoint, DelaunayTriangle triangle) {
        PointD point = mpoint.getPointD();

        LineLocation edge_ab_test = (new LineD(triangle.point_a.getPointD(), triangle.point_b.getPointD())).locate(point);
        if (edge_ab_test == LineLocation.BETWEEN || edge_ab_test == LineLocation.START || edge_ab_test == LineLocation.END) {
            return Optional.of(new Edge(triangle.point_a, triangle.point_b));
        }
        LineLocation edge_ac_test = (new LineD(triangle.point_a.getPointD(), triangle.point_c.getPointD()).locate(point));
        if (edge_ac_test == LineLocation.BETWEEN || edge_ac_test == LineLocation.START || edge_ac_test == LineLocation.END) {
            return Optional.of(new Edge(triangle.point_a, triangle.point_c));
        }
        LineLocation edge_bc_test = (new LineD(triangle.point_b.getPointD(), triangle.point_c.getPointD()).locate(point));
        if (edge_bc_test == LineLocation.BETWEEN || edge_bc_test == LineLocation.START || edge_bc_test == LineLocation.END) {
            return Optional.of(new Edge(triangle.point_b, triangle.point_c));
        }

        return Optional.empty();
    }

    // Find the triangle neighboring triangle over the edge
    Optional<DelaunayTriangle> adjacent_triangle_over_edge(DelaunayTriangle triangle, Edge edge) {
        Vertex initialPoint = triangle.getRemainingPoint(edge.getStart(), edge.getEnd());

        for (DelaunayTriangle other_triangle : get_valid_triangles()) {
            if (other_triangle.points().containsAll(Arrays.asList(edge.getStart(), edge.getEnd()))) {
                if (other_triangle.getRemainingPoint(edge.getStart(), edge.getEnd()) != initialPoint) {
                    return Optional.of(other_triangle);
                }
            }
        }
        return Optional.empty();
    }

    // Returns point_k != point_r, which is the remaining defining point of triangle (point_i, point_j, point_k)
    Optional<Vertex> adjacent_triangle_point(Vertex point_r, Vertex point_i, Vertex point_j) {
        // TODO way too slow
        for (DelaunayTriangle triangle : get_valid_triangles()) {
            if (triangle.hasPoint(point_i) && triangle.hasPoint(point_j)) {
                Vertex other_point = triangle.getRemainingPoint(point_i, point_j);
                if (other_point != point_r) {
                    return Optional.of(other_point);
                }
            }
        }
        return Optional.empty();
    }

    void removeIncidentTriangles(List<Vertex> omegas) {
        for (Vertex omega : omegas) {
            for (DelaunayTriangle triangle : get_valid_triangles()) {
                if (triangle.hasPoint(omega)) {
                    triangle.is_valid = false;
                }
            }
        }
    }

    VoronoiEdgeCollection toVoronoi() {
        VoronoiEdgeCollection voronoi = new VoronoiEdgeCollection();

        for (DelaunayTriangle triangle : get_valid_triangles()) {
            Vertex p_a = triangle.getCentroid();

            for (DelaunayTriangle neighbor : getNeighbors(triangle)) {
                Vertex p_b = neighbor.getCentroid();

                voronoi.insertIfDoesNotExist(new Edge(p_a, p_b));
            }
        }

        return voronoi;
    }

    private List<DelaunayTriangle> getNeighbors(DelaunayTriangle triangle) {
        List<Edge> edges = Arrays.asList(
            new Edge(triangle.point_a, triangle.point_b),
            new Edge(triangle.point_b, triangle.point_c),
            new Edge(triangle.point_c, triangle.point_a)
        );

        List<DelaunayTriangle> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            Optional<DelaunayTriangle> candidate = adjacent_triangle_over_edge(triangle, edge);
            candidate.ifPresent(neighbors::add);
        }

        return neighbors;
    }
}
