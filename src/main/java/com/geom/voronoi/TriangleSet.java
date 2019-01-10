package com.geom.voronoi;

import org.kynosarges.tektosyne.geometry.LineD;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;
import java.util.List;

class TriangleSet {

    private List<Triangle> triangleSet;
    
    public TriangleSet() {
        this.triangleSet = new ArrayList<>();
    }
    
    public void add(Triangle triangle) {
        this.triangleSet.add(triangle);
    }

    public void remove(Triangle triangle) {
        this.triangleSet.remove(triangle);
    }

    public List<Triangle> getAll() {
        return this.triangleSet;
    }

    public Triangle getTriangleThatContainsPoint(PointD point) {
        return triangleSet.stream()
            .filter(triangle -> triangle.isPointInside(point))
            .findFirst()
            .orElse(null);
    }

    public Triangle findNeighbour(Triangle triangle, LineD edge) {
        return triangleSet.stream()
            .filter(element -> element.isEdge(edge) && element!= triangle)
            .findFirst()
            .orElse(null);
    }

    public void removeByVertex(PointD vertex) {
        triangleSet.removeIf(triangle -> triangle.isVertex(vertex));
    }
}