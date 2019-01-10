package com.geom.voronoi;

import org.kynosarges.tektosyne.geometry.PointD;
import org.kynosarges.tektosyne.geometry.PolygonLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class VoronoiDiagram {
    private DelaunayTriangulation delaunay;
    private VoronoiEdgeCollection voronoi;

    public VoronoiDiagram(List<Vertex> points) {
        System.out.println(pointsDebugString(points));

        List<Vertex> omegas = get_omegas();
        delaunay = new DelaunayTriangulation();
        delaunay.insert(new DelaunayTriangle(omegas.get(0), omegas.get(1), omegas.get(2), true));

        // TODO assume input is randomized
        points.forEach(point -> {
            LocationQueryMatch match = delaunay.find_surrounding_triangle(point);
            if (match.triangle.isPresent()) {
                DelaunayTriangle triangle = match.triangle.get();
                if (match.locationResult == PolygonLocation.INSIDE) {
                    subdivideTriangleAndLegalize(triangle, point);
                } else if (match.locationResult == PolygonLocation.EDGE && match.edge.isPresent()) {
                    Edge edge = match.edge.get();
                    Optional<DelaunayTriangle> otherTriangleOpt = delaunay.adjacent_triangle_over_edge(triangle, edge);
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
        });
        delaunay.removeIncidentTriangles(omegas);
        VoronoiEdgeCollection voronoi = delaunay.toVoronoi();

        System.out.println(delaunay.validTrianglesDebugString());
        System.out.println(voronoi.edgesDebugString());

    }

    public static void main(String[] args) {
        List<Vertex> points = new ArrayList<>(Arrays.asList(
//            new Vertex(new PointD(15.3, 17.2))
//            , new Vertex(new PointD(53.17, 89.81))
//            , new Vertex(new PointD(321.7, 18.91))
//            , new Vertex(new PointD(103.0, 131.5))
//            , new Vertex(new PointD(-253.0, -152.1))
//            , new Vertex(new PointD(134.0, 32.5))
//            , new Vertex(new PointD(15.0, 98.3))
//            , new Vertex(new PointD(3.0, 20.1))
//            , new Vertex(new PointD(104, 200.5))
//            , new Vertex(new PointD(123, -100.4))
            new Vertex(new PointD(0, 0))
            , new Vertex(new PointD(10, 0))
            , new Vertex(new PointD(20, 0))
            , new Vertex(new PointD(30, 0))
            , new Vertex(new PointD(40, 0))
            , new Vertex(new PointD(50, 0))
            , new Vertex(new PointD(60, 0))
            , new Vertex(new PointD(70, 0))
            , new Vertex(new PointD(80, 0))
            , new Vertex(new PointD(90, 0))
            , new Vertex(new PointD(100, 0))
            , new Vertex(new PointD(110, 0))
            , new Vertex(new PointD(120, 0))
            , new Vertex(new PointD(130, 0))
            , new Vertex(new PointD(140, 0))
            , new Vertex(new PointD(150, 0))
            , new Vertex(new PointD(160, 0))
            , new Vertex(new PointD(170, 0))
            , new Vertex(new PointD(180, 0))
            , new Vertex(new PointD(190, 0))
            , new Vertex(new PointD(200, 0))
            , new Vertex(new PointD(0, 10))
            , new Vertex(new PointD(10, 10))
            , new Vertex(new PointD(20, 10))
            , new Vertex(new PointD(30, 10))
            , new Vertex(new PointD(40, 10))
            , new Vertex(new PointD(50, 10))
            , new Vertex(new PointD(60, 10))
            , new Vertex(new PointD(70, 10))
            , new Vertex(new PointD(80, 10))
            , new Vertex(new PointD(90, 10))
            , new Vertex(new PointD(100, 10))
            , new Vertex(new PointD(110, 10))
            , new Vertex(new PointD(120, 10))
            , new Vertex(new PointD(130, 10))
            , new Vertex(new PointD(140, 10))
            , new Vertex(new PointD(150, 10))
            , new Vertex(new PointD(160, 10))
            , new Vertex(new PointD(170, 10))
            , new Vertex(new PointD(180, 10))
            , new Vertex(new PointD(190, 10))
            , new Vertex(new PointD(200, 10))
            , new Vertex(new PointD(0, 20))
            , new Vertex(new PointD(10, 20))
            , new Vertex(new PointD(20, 20))
            , new Vertex(new PointD(30, 20))
            , new Vertex(new PointD(40, 20))
            , new Vertex(new PointD(50, 20))
            , new Vertex(new PointD(60, 20))
            , new Vertex(new PointD(70, 20))
            , new Vertex(new PointD(80, 20))
            , new Vertex(new PointD(90, 20))
            , new Vertex(new PointD(100, 20))
            , new Vertex(new PointD(110, 20))
            , new Vertex(new PointD(120, 20))
            , new Vertex(new PointD(130, 20))
            , new Vertex(new PointD(140, 20))
            , new Vertex(new PointD(150, 20))
            , new Vertex(new PointD(160, 20))
            , new Vertex(new PointD(170, 20))
            , new Vertex(new PointD(180, 20))
            , new Vertex(new PointD(190, 20))
            , new Vertex(new PointD(200, 20))
        ));
        VoronoiDiagram vd = new VoronoiDiagram(points);
    }

    private void subdivideEdgeAndTrianglesAndLegalize(
        Edge edge,
        DelaunayTriangle triangle,
        DelaunayTriangle otherTriangle,
        Vertex point) {
        triangle.is_valid = false;
        otherTriangle.is_valid = false;

        Vertex p_r = point;
        Vertex p_i = edge.getStart();
        Vertex p_j = edge.getEnd();
        Vertex p_k = triangle.getRemainingPoint(p_i, p_j);
        Vertex p_l = otherTriangle.getRemainingPoint(p_i, p_j);


        delaunay.insert(new DelaunayTriangle(p_r, p_i, p_l, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_l, p_j, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_j, p_k, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_k, p_i, true));

        legalizeEdge(p_r, p_i, p_l);
        legalizeEdge(p_r, p_l, p_j);
        legalizeEdge(p_r, p_k, p_i);
    }

    private String pointsDebugString(List<Vertex> points) {
        StringBuilder s = new StringBuilder().append("points: ");

        for (Vertex point : points) {
            s.append(point);
        }

        return s.toString();
    }

    private List<Vertex> get_omegas() {
        // TODO make dependent on input area
        return Arrays.asList(
            new Vertex(new PointD(-10.0, -50.0)),
            new Vertex(new PointD(300.0, 10)),
            new Vertex(new PointD(-30, 50.0))
        );
    }

    // The point being inserted is point_rf, and (point_i, point_j) is the edge of the triangulation that may need
    // to be flipped.
    private void legalizeEdge(Vertex point_r, Vertex point_i, Vertex point_j) {
        if (!is_legal(point_i, point_j)) {
            Optional<Vertex> point_k_option = delaunay.adjacent_triangle_point(point_r, point_i, point_j);

            point_k_option.ifPresentOrElse(
                point_k -> {
                    flip_edge(point_i, point_j, point_r, point_k);

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
    private void flip_edge(Vertex point_i, Vertex point_j, Vertex point_r, Vertex point_k) {
        Optional<DelaunayTriangle> triangle_a = delaunay.find_triangle_with_vertices(Arrays.asList(point_r, point_i, point_j));
        Optional<DelaunayTriangle> triangle_b = delaunay.find_triangle_with_vertices(Arrays.asList(point_k, point_i, point_j));

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
    private boolean is_legal(Vertex mpoint_i, Vertex mpoint_j) {
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
            for (Vertex mpoint : triangle.points()) {
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

    private void subdivideTriangleAndLegalize(DelaunayTriangle old_triangle, Vertex new_point) {
        old_triangle.is_valid = false;
        Vertex p_r = new_point;
        Vertex p_i = old_triangle.point_a;
        Vertex p_j = old_triangle.point_b;
        Vertex p_k = old_triangle.point_c;

        delaunay.insert(new DelaunayTriangle(p_r, p_i, p_j, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_j, p_k, true));
        delaunay.insert(new DelaunayTriangle(p_r, p_k, p_i, true));

        legalizeEdge(p_r, p_i, p_j);
        legalizeEdge(p_r, p_j, p_k);
        legalizeEdge(p_r, p_k, p_i);
    }
}
