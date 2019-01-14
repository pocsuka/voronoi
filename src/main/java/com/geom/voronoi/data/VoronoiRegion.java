package com.geom.voronoi.data;

import com.esri.core.geometry.*;
import com.geom.voronoi.gui.VoronoiApplication;
import com.geom.voronoi.gui.VoronoiDialog;
import javafx.scene.paint.Color;
import org.kynosarges.tektosyne.geometry.GeoUtils;

import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VoronoiRegion {

    private Color color;
    private List<Vertex> vertices;
    private PointD maxCoord;
    private Vertex playerPoint;

    public VoronoiRegion(Color color, List<PointD> vertices, PointD maxCoord) {
        this.color = color;
        this.maxCoord = maxCoord;
        try  {
            PointD[] convexHull = GeoUtils.convexHull(vertices.toArray(new PointD[vertices.size()]));
            this.vertices = convertPointDArrayToVoronoiVertexList(convexHull);
            clip();
        } catch (NullPointerException e) {

        }
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

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public double getArea() {

        double area = 0;

        try {
            area = Math.abs(GeoUtils.polygonArea(VoronoiDialog.toPointDList(vertices).toArray(new PointD[vertices.size()])));
        }catch (IllegalArgumentException e) {
            System.out.println("##################################");
        }

        return area;
    }

    private Polygon convertToPolygon(List<Vertex> vertices) {
        Polygon polygon = new Polygon();

        polygon.startPath(vertices.get(0).getLocation().x, vertices.get(0).getLocation().y);

        for (int i = 1; i < vertices.size(); i++) {
            polygon.lineTo(vertices.get(i).getLocation().x, vertices.get(i).getLocation().y);
        }

        return  polygon;
    }

    private List<Vertex> convertToPointList(Polygon polygon) {
        List<Vertex> points = new ArrayList<>();

        Point2D[] polyPoints = polygon.getCoordinates2D();

        for (int i = 0; i < polyPoints.length; i++) {
            PointD p = new PointD(polyPoints[i].x, polyPoints[i].y);
            points.add(new Vertex(p, this.color));
        }

        return  points;
    }

    private List<Vertex> convertPointDArrayToVoronoiVertexList(PointD[] vertices) {
        return Arrays.stream(vertices)
            .map(point -> createVoronoiVertexInstance(point))
            .collect(Collectors.toList());
    }

    private Vertex createVoronoiVertexInstance(PointD p) {
        return new Vertex(p, this.color);
    }

}
