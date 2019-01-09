package com.geom.voronoi;

import javafx.util.Pair;
import org.kynosarges.tektosyne.geometry.PolygonLocation;

import java.util.Optional;

public class LocationQueryMatch {
    PolygonLocation locationResult;
    Optional<DelaunayTriangle> triangle;
    Optional<Pair<MutablePoint, MutablePoint>> edge;

    public LocationQueryMatch(PolygonLocation locationResult, Optional<DelaunayTriangle> triangle, Optional<Pair<MutablePoint, MutablePoint>> edge) {
        this.locationResult = locationResult;
        this.triangle = triangle;
        this.edge = edge;
    }
}
