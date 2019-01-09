package com.geom.voronoi;

import javafx.util.Pair;
import org.kynosarges.tektosyne.geometry.PointD;
import org.kynosarges.tektosyne.geometry.PolygonLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class VoronoiDiagram {
    private DelaunayTriangulation delaunay;

    public VoronoiDiagram(List<MutablePoint> points) {
        System.out.println(pointsDebugString(points));

        List<MutablePoint> omegas = get_omegas();
        delaunay = new DelaunayTriangulation();
        delaunay.insert(new DelaunayTriangle(omegas.get(0), omegas.get(1), omegas.get(2), true));

        // TODO assume input is randomized
        points.forEach(point -> {
            System.out.println("Before:");
            System.out.println(delaunay.validTrianglesDebugString());
            System.out.println("inserting points: " + point);

            LocationQueryMatch match = delaunay.find_surrounding_triangle(point);
            if (match.triangle.isPresent()) {
                DelaunayTriangle triangle = match.triangle.get();
                if (match.locationResult == PolygonLocation.INSIDE) {
                    subdivideTriangleAndLegalize(triangle, point);
                } else if (match.locationResult == PolygonLocation.EDGE && match.edge.isPresent()) {
                    Pair<MutablePoint, MutablePoint> edge = match.edge.get();
                    Optional<DelaunayTriangle> otherTriangleOpt = delaunay.adjacent_triangle_over_edge(triangle, edge.getKey(), edge.getValue());
                    if (otherTriangleOpt.isPresent()) {
                        DelaunayTriangle otherTriangle = otherTriangleOpt.get();
                        subdivideEdgeAndTrianglesAndLegalize(edge, triangle, otherTriangle, point);
                    } else {
                        System.err.println("Cannot split outer edge");
                    }
                } else {
                    System.err.println("Could not subdivide");
                }
            }

            System.out.println(delaunay.validTrianglesDebugString());
        });

//        System.out.println(delaunay.validTrianglesDebugString());
    }

    public static void main(String[] args) {
        List<MutablePoint> points = new ArrayList<MutablePoint>(Arrays.asList(
            new MutablePoint(new PointD(15.3, 17.2))
            , new MutablePoint(new PointD(53.17, 89.81))
            , new MutablePoint(new PointD(321.7, 18.91))
            , new MutablePoint(new PointD(103.0, 131.5))
            , new MutablePoint(new PointD(253.0, 252.1))
            , new MutablePoint(new PointD(134.0, 32.5))
            , new MutablePoint(new PointD(15.0, 98.3))
            , new MutablePoint(new PointD(3.0, 20.1))
            , new MutablePoint(new PointD(104, 200.5))
            , new MutablePoint(new PointD(123, -100.4))
        ));
        VoronoiDiagram vd = new VoronoiDiagram(points);
    }

    private void subdivideEdgeAndTrianglesAndLegalize(
        Pair<MutablePoint, MutablePoint> edge,
        DelaunayTriangle triangle,
        DelaunayTriangle otherTriangle,
        MutablePoint point) {
        triangle.is_valid = false;
        otherTriangle.is_valid = false;

        MutablePoint p_r = point;
        MutablePoint p_i = edge.getKey();
        MutablePoint p_j = edge.getValue();
        MutablePoint p_k = triangle.getRemainingPoint(p_i, p_j);
        MutablePoint p_l = otherTriangle.getRemainingPoint(p_i, p_j);


        delaunay.insert(new DelaunayTriangle(p_r, p_i, p_l, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_l, p_j, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_j, p_k, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_k, p_i, true));

        legalizeEdge(p_r, p_i, p_l);
        legalizeEdge(p_r, p_l, p_j);
        legalizeEdge(p_r, p_k, p_i);
    }

    private String pointsDebugString(List<MutablePoint> points) {
        StringBuilder s = new StringBuilder().append("points: ");

        for (MutablePoint point : points) {
            s.append(point);
        }

        return s.toString();
    }

    private List<MutablePoint> get_omegas() {
        // TODO make dependent on input area
        return Arrays.asList(
            new MutablePoint(new PointD(-500.0, -500.0)),
            new MutablePoint(new PointD(600.0, 0.9)),
            new MutablePoint(new PointD(0.9, 300.0))
        );
    }

    // The point being inserted is point_rf, and (point_i, point_j) is the edge of the triangulation that may need
    // to be flipped.
    private void legalizeEdge(MutablePoint point_r, MutablePoint point_i, MutablePoint point_j) {
        if (!is_legal(point_i, point_j)) {
            Optional<MutablePoint> point_k_option = delaunay.adjacent_triangle_point(point_r, point_i, point_j);

            point_k_option.ifPresentOrElse(
                point_k -> {
                    System.out.println("Before flip");
                    System.out.println(delaunay.validTrianglesDebugString());
                    flip_edge(point_i, point_j, point_r, point_k);
                    System.out.println("After flip");
                    System.out.println(delaunay.validTrianglesDebugString());

                    legalizeEdge(point_r, point_i, point_k);
                    legalizeEdge(point_r, point_k, point_j);
                },
                () -> {
                    // No adjacent triangle found
                }
            );

        }
    }

    // Replaces triangles (p_r, p_i, p_j) (p_k, p_i, p_j) by the triangles (p_r, p_i, p_k) and (p_k, p_j, p_r)
    private void flip_edge(MutablePoint point_i, MutablePoint point_j, MutablePoint point_r, MutablePoint point_k) {
        Optional<DelaunayTriangle> triangle_a = delaunay.find_triangle_with_vertices(Arrays.asList(point_r, point_i, point_j));
        Optional<DelaunayTriangle> triangle_b = delaunay.find_triangle_with_vertices(Arrays.asList(point_k, point_i, point_j));

        System.out.println("Flipping.\np_r: " + point_r + ",\np_i: " + point_i + ",\np_j: " + point_j + ",\np_k: " + point_k);
        if (triangle_a.isPresent() && triangle_b.isPresent()) {
            triangle_a.get().point_a = point_r;
            triangle_a.get().point_b = point_i;
            triangle_a.get().point_c = point_k;

            triangle_b.get().point_a = point_k;
            triangle_b.get().point_b = point_j;
            triangle_b.get().point_c = point_r;
        } else {
            System.out.println("Could not flip edge");
        }
    }

    // A legal edge has the property that there exists a circle through point_i and point_j which does not contain any
    // other point in its interior
    private boolean is_legal(MutablePoint mpoint_i, MutablePoint mpoint_j) {
        if (get_omegas().contains(mpoint_i) || get_omegas().contains(mpoint_j)) {
            return true;
        }
        PointD point_i = mpoint_i.getPointD();
        PointD point_j = mpoint_j.getPointD();
        // TODO make faster
        PointD center_point = new PointD(
            0.5 * point_i.x + 0.5 * point_j.x,
            0.5 * point_i.y + 0.5 * point_j.y
        );
        double r = point_i.subtract(center_point).length();

        for (DelaunayTriangle triangle : delaunay.get_valid_triangles()) {
            for (MutablePoint mpoint : triangle.points()) {
                PointD pointd = mpoint.getPointD();
                double dist = pointd.subtract(center_point).length();
                if (dist < r) {
                    if (pointd != point_i && pointd != point_j) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void subdivideTriangleAndLegalize(DelaunayTriangle old_triangle, MutablePoint new_point) {
        old_triangle.is_valid = false;
        MutablePoint p_r = new_point;
        MutablePoint p_i = old_triangle.point_a;
        MutablePoint p_j = old_triangle.point_b;
        MutablePoint p_k = old_triangle.point_c;

        delaunay.insert(new DelaunayTriangle(p_r, p_i, p_j, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_j, p_k, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_k, p_i, true));

        legalizeEdge(p_r, p_i, p_j);
        legalizeEdge(p_r, p_j, p_k);
        legalizeEdge(p_r, p_k, p_i);
    }
}
