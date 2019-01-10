package com.geom.voronoi;

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

        PointD p1 = new PointD(0, 15 * getMaxAxisValue());
        PointD p2 = new PointD(15 * getMaxAxisValue(), 0);
        PointD p3 = new PointD(-15 * getMaxAxisValue(), -15 * getMaxAxisValue());

        Triangle veryBigTriangle = new Triangle(p1, p2, p3);

        triangleSet.add(veryBigTriangle);

//        Collections.sort(pointSet, new PointDComparatorY());

        for (int i = 0; i < pointSet.size(); i++) {
            Triangle triangle = triangleSet.getTriangleThatContainsPoint(pointSet.get(i));

            PointD a = triangle.getA();
            PointD b = triangle.getB();
            PointD c = triangle.getC();

            triangleSet.remove(triangle);

            Triangle first = new Triangle(a, b, pointSet.get(i));
            Triangle second = new Triangle(b, c, pointSet.get(i));
            Triangle third = new Triangle(c, a, pointSet.get(i));

            triangleSet.add(first);
            triangleSet.add(second);
            triangleSet.add(third);

            legalizeEdge(first, new LineD(a, b), pointSet.get(i));
            legalizeEdge(second, new LineD(b, c), pointSet.get(i));
            legalizeEdge(third, new LineD(c, a), pointSet.get(i));
        }

        triangleSet.removeByVertex(veryBigTriangle.getA());
        triangleSet.removeByVertex(veryBigTriangle.getB());
        triangleSet.removeByVertex(veryBigTriangle.getC());
    }


    private void legalizeEdge(Triangle triangle, LineD edge, PointD newVertex) {
        Triangle neighbourTriangle = triangleSet.findNeighbour(triangle, edge);

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

    public List<Triangle> getTriangleSet() {
        return triangleSet.getAll();
    }

    private double getMaxAxisValue() {
        return (this.areaHeight > this.areaWidth) ? areaHeight : areaWidth;
    }

}
