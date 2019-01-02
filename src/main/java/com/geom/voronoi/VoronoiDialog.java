package com.geom.voronoi;

import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

import org.kynosarges.tektosyne.geometry.*;

import java.util.*;
import java.util.stream.Collectors;



public class VoronoiDialog extends Stage {

    public static final int MENUHEIGHT = 16;
    private final Pane _output = new Pane();
    private PointD[] _points;
    private List<PointD> triangulationPoints = new ArrayList<>();


    public VoronoiDialog() {
        initOwner(Global.primaryStage());
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.DECORATED);

        final Label message = new Label();


        Global.clipChildren(_output);
        _output.setPrefSize(400, 300);

        final VBox root = new VBox(message, _output);
        root.setPadding(new Insets(0));
        root.setSpacing(0);
        VBox.setVgrow(_output, Priority.ALWAYS);
        
        setResizable(true);
        setScene(new Scene(root));
        setTitle("Voronoi & Delaunay Test");
        sizeToScene();
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PointD newPoint = new PointD(mouseEvent.getX(), mouseEvent.getY());
                triangulationPoints.add(newPoint);
                if(triangulationPoints.size() > 1) {
                    draw(triangulationPoints);
                }
            }
        });
        
    }

    private void draw(List<PointD> triangulationPoints) {
        final double diameter = 4;

        // generate new random point set if desired
//        if (points == null) {
//            final double width = _output.getWidth();
//            final double height = _output.getHeight();
//            final RectD bounds = new RectD(0.1 * width, 0.1 * height, 0.9 * width, 0.9 * height);
//
//            final int count = 4 + Global.RANDOM.nextInt(17);
//            points = GeoUtils.randomPoints(count, bounds, new PointDComparatorY(0), diameter);
//        }
//
//        _points = points;
        PointD[] points = triangulationPoints.toArray(new PointD[triangulationPoints.size()]);
        final RectD clip = new RectD(0, 0, _output.getWidth(), _output.getHeight());

        final VoronoiResults results = Voronoi.findAll(points, clip);
        _output.getChildren().clear();

        // draw interior of Voronoi regions
        for (PointD[] region: results.voronoiRegions()) {
            final Polygon polygon = new Polygon(PointD.toDoubles(region));
            polygon.setFill(Color.PALEGOLDENROD);
            polygon.setStroke(Color.WHITE);
            polygon.setStrokeWidth(6);
            _output.getChildren().add(polygon);
        }

        // draw edges of Voronoi diagram
        for (VoronoiEdge edge: results.voronoiEdges) {
            final PointD start = results.voronoiVertices[edge.vertex1];
            final PointD end = results.voronoiVertices[edge.vertex2];

            final Line line = new Line(start.x, start.y, end.x, end.y);
            line.setStroke(Color.RED);
            _output.getChildren().add(line);
        }

        // draw edges of Delaunay triangulation
//        for (LineD edge: results.delaunayEdges()) {
//            final Line line = new Line(edge.start.x, edge.start.y, edge.end.x, edge.end.y);
//            line.getStrokeDashArray().addAll(3.0, 2.0);
//            line.setStroke(Color.BLUE);
//            _output.getChildren().add(line);
//        }

        // draw generator points
        for (PointD point: points) {
            final Circle shape = new Circle(point.x, point.y - MENUHEIGHT, diameter / 2);
            shape.setFill(Color.BLACK);
            shape.setStroke(Color.BLACK);
            _output.getChildren().add(shape);
        }

        PointD[][] regions = results.voronoiRegions();

        Arrays.stream(regions).
            map(element -> Math.abs(GeoUtils.polygonArea(element))).
            collect(Collectors.toList()).stream().
            forEach(area -> System.out.println(area));
    }
}
