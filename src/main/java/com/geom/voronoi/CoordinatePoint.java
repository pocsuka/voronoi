package com.geom.voronoi;

import org.kynosarges.tektosyne.geometry.PointD;

import java.util.Locale;
import java.util.Objects;

// TODO check if CoordinatePoint must actually be mutable
public class CoordinatePoint {
    private PointD point;

    public CoordinatePoint(PointD point) {
        this.point = point;
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
