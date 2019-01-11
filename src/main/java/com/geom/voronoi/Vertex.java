package com.geom.voronoi;

import org.kynosarges.tektosyne.geometry.PointD;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Vertex {
    private PointD point;

    public Vertex(PointD point) {
        this.point = point;
    }

    public static Vertex fromCoordinate(double x, double y) {
        return new Vertex(new PointD(x, y));
    }

    static Vertex getCentroid(List<Vertex> vertices) {
        int n = vertices.size();
        double sumX = 0;
        double sumY = 0;
        for (Vertex point : vertices) {
            sumX += point.getPointD().x;
            sumY += point.getPointD().y;
        }

        return new Vertex(new PointD(sumX / n, sumY / n));
    }

    PointD getPointD() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex that = (Vertex) o;
        return Double.compare(that.getPointD().x, getPointD().x) == 0 &&
            Double.compare(that.getPointD().y, getPointD().y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPointD().x, getPointD().y);
    }

    @Override
    public String toString() {
        PointD p = getPointD();
        return String.format(Locale.ROOT, "(%7.2f,%7.2f)", p.x, p.y);
    }
}
