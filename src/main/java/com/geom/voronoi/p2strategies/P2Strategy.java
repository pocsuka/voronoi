package com.geom.voronoi.p2strategies;

import com.geom.voronoi.data.Triangle;
import com.geom.voronoi.data.Vertex;

import java.util.List;

public interface P2Strategy {
    public List<Vertex> getPoints(List<Triangle> triangles, int n, int width, int height);
}
