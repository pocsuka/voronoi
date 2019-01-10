package com.geom.voronoi;

import org.kynosarges.tektosyne.geometry.PolygonLocation;

import java.util.Optional;

public class LocationQueryMatch {
    PolygonLocation locationResult;
    Optional<DelaunayTriangle> triangle;
    Optional<Edge> edge;

    public LocationQueryMatch(PolygonLocation locationResult, Optional<DelaunayTriangle> triangle, Optional<Edge> edge) {
        this.locationResult = locationResult;
        this.triangle = triangle;
        this.edge = edge;
    }
}
