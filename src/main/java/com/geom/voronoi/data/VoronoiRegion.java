package com.geom.voronoi.data;

import com.esri.core.geometry.*;
import javafx.scene.paint.Color;
import org.kynosarges.tektosyne.geometry.GeoUtils;

import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoronoiRegion {

    private Color color;
    private List<PointD> vertices;
    private PointD maxCoord;

    public VoronoiRegion(Color color, List<PointD> vertices, PointD maxCoord) {
        this.color = color;
        this.maxCoord = maxCoord;

        PointD[] convexHull = GeoUtils.convexHull(vertices.toArray(new PointD[vertices.size()]));
        this.vertices = Arrays.asList(convexHull);
        clip();
    }

    public void clip() {
        OperatorFactoryLocal engine = OperatorFactoryLocal.getInstance();

        OperatorClip clipOp = (OperatorClip) engine
            .getOperator(Operator.Type.Clip);

        Polygon polygon = convertToPolygon(vertices);
        SimpleGeometryCursor polygonCurs = new SimpleGeometryCursor(polygon);

        Envelope2D envelope = new Envelope2D();
        envelope.xmin = 0;
        envelope.xmax = this.maxCoord.x;
        envelope.ymin = 0;
        envelope.ymax = this.maxCoord.y;

        SpatialReference spatialRef = SpatialReference.create(1);

        GeometryCursor clipPolygonCurs = clipOp.execute(polygonCurs, envelope,
            spatialRef, null);

        Polygon clippedPolygon = (Polygon) clipPolygonCurs.next();

        vertices = convertToPointList(clippedPolygon);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<PointD> getVertices() {
        return vertices;
    }

    public void setVertices(List<PointD> vertices) {
        this.vertices = vertices;
    }

    public double getArea() {
        return Math.abs(GeoUtils.polygonArea(vertices.toArray(new PointD[vertices.size()])));
    }

    private Polygon convertToPolygon(List<PointD> vertices) {
        Polygon polygon = new Polygon();

        polygon.startPath(vertices.get(0).x, vertices.get(0).y);

        for (int i = 1; i < vertices.size(); i++) {
            polygon.lineTo(vertices.get(i).x, vertices.get(i).y);
        }

        return  polygon;
    }

    private List<PointD> convertToPointList(Polygon polygon) {
        List<PointD> points = new ArrayList<>();

        Point2D[] polyPoints = polygon.getCoordinates2D();

        for (int i = 0; i < polyPoints.length; i++) {
            PointD p = new PointD(polyPoints[i].x, polyPoints[i].y);
            points.add(p);
        }

        return  points;
    }

}
