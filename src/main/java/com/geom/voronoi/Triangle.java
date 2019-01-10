package com.geom.voronoi;

import org.kynosarges.tektosyne.geometry.GeoUtils;
import org.kynosarges.tektosyne.geometry.LineD;
import org.kynosarges.tektosyne.geometry.PointD;
import org.kynosarges.tektosyne.geometry.PolygonLocation;

public class Triangle {

    private PointD a;
    private PointD b;
    private PointD c;

    public Triangle(PointD a, PointD b, PointD c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean isPointInside(PointD point) {

        PointD[] points = getPointsAsArray();

        return GeoUtils.pointInPolygon(point, points).equals(PolygonLocation.INSIDE);
    }

    public boolean isVertex(PointD vertex) {
        return (this.a == vertex || this.b == vertex || this.c == vertex);
    }

    public boolean isPointInsideOfCircumCircle(PointD point) {
        double ax = a.x - point.x;
        double bx = b.x - point.x;
        double cx = c.x - point.x;

        double ay = a.y - point.y;
        double by = b.y - point.y;
        double cy = c.y - point.y;

        double det =
            (ax * ax + ay * ay) * (bx * cy - cx * by) -
                (bx * bx + by * by) * (ax * cy - cx * ay) +
                (cx * cx + cy * cy) * (ax * by - bx * ay);

        if (isCCW()) {
            return det > 0.;
        }

        return det < 0.;
    }

    public boolean isEdge(LineD edge) {
        return (this.a == edge.start || this.b == edge.start || this.c == edge.start)
            && (this.a == edge.end || this.b == edge.end || this.c == edge.end);
    }

    public PointD getRemovableVertex(LineD edge) {

        if(!checkEdge(this.a, edge)) {
            return getA();
        }

        if(!checkEdge(this.b, edge)) {
            return getB();
        }

        if(!checkEdge(this.c, edge)) {
            return getC();
        }

        return null;
    }

    public PointD getA() {
        return a;
    }

    public PointD getB() {
        return b;
    }

    public PointD getC() {
        return c;
    }

    private PointD[] getPointsAsArray() {
        PointD[] points = new PointD[3];
        points[0] = this.a;
        points[1] = this.b;
        points[2] = this.c;
        return points;
    }

    private boolean isCCW() {
        return (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y) > 0.;
    }


    private boolean checkEdge(PointD vertex, LineD edge) {
        return (vertex == edge.start || vertex == edge.end);
    }

    @Override
    public String toString() {
        return "Triangle{" +
            "a=" + a +
            ", b=" + b +
            ", c=" + c +
            '}';
    }
}
