package com.geom.voronoi;

import org.kynosarges.tektosyne.geometry.PointD;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CoordinatePoint {
    private PointD point;

    public CoordinatePoint(PointD point) {
        this.point = point;
    }

    static CoordinatePoint getCentroid(List<CoordinatePoint> coordinatePoints) {
        int n = coordinatePoints.size();
        double sumX = 0;
        double sumY = 0;
        for (CoordinatePoint point : coordinatePoints) {
            sumX += point.getPointD().x;
            sumY += point.getPointD().y;
        }

        return new CoordinatePoint(new PointD(sumX / n, sumY / n));
    }

    PointD getPointD() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinatePoint that = (CoordinatePoint) o;
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
