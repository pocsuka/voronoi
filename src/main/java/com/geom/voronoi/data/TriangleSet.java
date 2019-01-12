package com.geom.voronoi.data;

import org.kynosarges.tektosyne.geometry.LineD;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TriangleSet {

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

    public Triangle findNeighbourTriangle(Triangle triangle, LineD edge) {
        return triangleSet.stream()
            .filter(element -> element.isEdge(edge) && element!= triangle)
            .findFirst()
            .orElse(null);
    }
//TODO: crossing lines and buggy voronoi, maintain order cw/ccw
    public List<Triangle> findAllNeighbourTriangle(PointD point) {
        return triangleSet.stream()
            .filter(element -> element.isVertex(point) )
            .collect(Collectors.toList());
    }

    public List<PointD> getAllNeighbouringTriangleCenter(PointD point) {
        return findAllNeighbourTriangle(point).stream()
            .map(triangle -> triangle.getCenter())
            .collect(Collectors.toList());
    }

    public Triangle findSharingTriangle(LineD edge) {
        return triangleSet.stream()
            .filter(element -> element.isEdge(edge) )
            .findFirst()
            .orElse(null);
    }

    public LineD findNearestEdge(PointD point) {
        List<LinePointDistanceWrapper> closeEdges = new ArrayList<>();

        triangleSet.stream().forEach(triangle -> closeEdges.add(triangle.findTriangleNearestEdge(point)));

        Collections.sort(closeEdges);
        return closeEdges.get(0).getEdge();
    }

    public void removeByVertex(PointD vertex) {
        triangleSet.removeIf(triangle -> triangle.isVertex(vertex));
    }
}
