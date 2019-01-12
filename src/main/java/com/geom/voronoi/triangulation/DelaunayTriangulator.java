package com.geom.voronoi.triangulation;

import com.geom.voronoi.data.Triangle;

import com.geom.voronoi.data.TriangleSet;
import org.kynosarges.tektosyne.geometry.LineD;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.List;


public class DelaunayTriangulator implements Triangulator {

    private List<PointD> pointSet;
    private TriangleSet triangleSet;
    private double areaWidth;
    private double areaHeight;

    public DelaunayTriangulator(List<PointD> pointSet, double areaWidth, double areaHeight) {
        this.pointSet = pointSet;
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
    }

    @Override
    public void triangulate() {
        triangleSet = new TriangleSet();

        PointD p1 = new PointD(-10000, -10000);
        PointD p2 = new PointD(10000 , -10000);
        PointD p3 = new PointD(0, 10000);

        Triangle veryBigTriangle = new Triangle(p1, p2, p3);

        triangleSet.add(veryBigTriangle);

        for (int i = 0; i < pointSet.size(); i++) {
            PointD currentPoint = pointSet.get(i);

            Triangle triangle = triangleSet.getTriangleThatContainsPoint(currentPoint);

            if (triangle == null) {

                LineD edge = triangleSet.findNearestEdge(currentPoint);

                Triangle first = triangleSet.findSharingTriangle(edge);
                Triangle second = triangleSet.findNeighbourTriangle(first, edge);

                PointD firstRemovableVertex = first.getRemovableVertex(edge);
                PointD secondRemovableVertex = second.getRemovableVertex(edge);

                triangleSet.remove(first);
                triangleSet.remove(second);

                Triangle triangle1 = new Triangle(edge.start, firstRemovableVertex, currentPoint);
                Triangle triangle2 = new Triangle(edge.end, firstRemovableVertex, currentPoint);
                Triangle triangle3 = new Triangle(edge.start, secondRemovableVertex, currentPoint);
                Triangle triangle4 = new Triangle(edge.end, secondRemovableVertex, currentPoint);

                triangleSet.add(triangle1);
                triangleSet.add(triangle2);
                triangleSet.add(triangle3);
                triangleSet.add(triangle4);

                legalizeEdge(triangle1, new LineD(edge.start, firstRemovableVertex), currentPoint);
                legalizeEdge(triangle2, new LineD(edge.end, firstRemovableVertex), currentPoint);
                legalizeEdge(triangle3, new LineD(edge.start, secondRemovableVertex), currentPoint);
                legalizeEdge(triangle4, new LineD(edge.end, secondRemovableVertex), currentPoint);

            } else {

                PointD a = triangle.getA();
                PointD b = triangle.getB();
                PointD c = triangle.getC();

                triangleSet.remove(triangle);

                Triangle first = new Triangle(a, b, currentPoint);
                Triangle second = new Triangle(b, c, currentPoint);
                Triangle third = new Triangle(c, a, currentPoint);

                triangleSet.add(first);
                triangleSet.add(second);
                triangleSet.add(third);

                legalizeEdge(first, new LineD(a, b), currentPoint);
                legalizeEdge(second, new LineD(b, c), currentPoint);
                legalizeEdge(third, new LineD(c, a), currentPoint);
            }

        }
          //uncomment for good triangulation, needed for outer triangles to draw voronoi properly
//        triangleSet.removeByVertex(veryBigTriangle.getA());
//        triangleSet.removeByVertex(veryBigTriangle.getB());
//        triangleSet.removeByVertex(veryBigTriangle.getC());
    }


    private void legalizeEdge(Triangle triangle, LineD edge, PointD newVertex) {
        Triangle neighbourTriangle = triangleSet.findNeighbourTriangle(triangle, edge);

        if (neighbourTriangle != null) {
            if (neighbourTriangle.isPointInsideOfCircumCircle(newVertex)) {
                triangleSet.remove(triangle);
                triangleSet.remove(neighbourTriangle);

                PointD noneEdgeVertex = neighbourTriangle.getRemovableVertex(edge);

                Triangle firstTriangle = new Triangle(noneEdgeVertex, edge.start, newVertex);
                Triangle secondTriangle = new Triangle(noneEdgeVertex, edge.end, newVertex);

                triangleSet.add(firstTriangle);
                triangleSet.add(secondTriangle);

                legalizeEdge(firstTriangle, new LineD(noneEdgeVertex, edge.start), newVertex);
                legalizeEdge(secondTriangle, new LineD(noneEdgeVertex, edge.end), newVertex);
            }
        }
    }

    public TriangleSet getTriangleSet() {
        return this.triangleSet;
    }

    private double getMaxAxisValue() {
        return (this.areaHeight > this.areaWidth) ? areaHeight : areaWidth;
    }

}
