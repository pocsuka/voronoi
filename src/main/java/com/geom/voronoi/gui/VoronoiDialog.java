package com.geom.voronoi.gui;

import com.geom.voronoi.data.Triangle;
import com.geom.voronoi.triangulation.DelaunayTriangulator;
import com.geom.voronoi.utils.InputReader;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

import org.kynosarges.tektosyne.geometry.*;

import java.util.*;


public class VoronoiDialog extends Stage {

    private final Pane _output = new Pane();
    private PointD[] _points;
    List<PointD> input = new ArrayList<>();
    InputReader inputReader;

    public VoronoiDialog() {
        inputReader = new InputReader();
        inputReader.readFile("c:/git/voronoi/src/main/resources/circle10.txt");

        initOwner(Global.primaryStage());
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.DECORATED);

       Global.clipChildren(_output);
        _output.setPrefSize(600, 400);

        final VBox root = new VBox( _output);
        root.setPadding(new Insets(8));
        root.setSpacing(8);
        VBox.setVgrow(_output, Priority.ALWAYS);

        setResizable(true);
        setScene(new Scene(root));
        setTitle("Voronoi & Delaunay Test");
        sizeToScene();
        input = inputReader.getPoints();
        setOnShown(t -> draw());
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PointD newPoint = new PointD(mouseEvent.getX(), mouseEvent.getY());
//                triangulationPoints.add(newPoint);
                input.add(newPoint);
                System.out.println("##########");
                if(input.size() > 3) {
                    draw();
                }
            }
        });
    }


    private void draw() {
        final double diameter = 4;

        _points = input.toArray(new PointD[input.size()]);

        List<Triangle> triangles = null;

        final RectD clip = new RectD(0, 0, _output.getWidth(), _output.getHeight());
        final VoronoiResults results = Voronoi.findAll(input.toArray(new PointD[input.size()]), clip);
        _output.getChildren().clear();


        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(input, inputReader.getWidth(), inputReader.getHeight());
        delaunayTriangulator.triangulate();

        triangles = delaunayTriangulator.getTriangleSet().getAll();



        HashSet<PointD> visited = new HashSet<>();

        PointD p1 = new PointD(-10000, -10000);
        PointD p2 = new PointD(10000 , -10000);
        PointD p3 = new PointD(0, 10000);

        visited.add(p1);
        visited.add(p2);
        visited.add(p3);

        for (Triangle triangle : triangles) {
            for (PointD vertex: triangle.getPointsAsList()) {
                if (visited.contains(vertex)) continue;
                visited.add(vertex);
                List<PointD> vertices = delaunayTriangulator.getTriangleSet().getAllNeighbouringTriangleCenter(vertex);

                Collections.sort(vertices, new PointDComparatorY());

                System.out.println(vertex + ":" + (vertices));

                Polygon polygon = new Polygon();
                polygon.getPoints().addAll(toDoubleArray(vertices));
                polygon.setFill(Color.YELLOW);
                polygon.setStroke(Color.BLACK);
                _output.getChildren().add(polygon);
            }

//            triangulation lines
            final Line ab = new Line (triangle.getA().x, triangle.getA().y -16, triangle.getB().x, triangle.getB().y -16 );

            ab.setStroke(Color.RED);
            _output.getChildren().add(ab);

            final Line ac = new Line (triangle.getA().x, triangle.getA().y -16, triangle.getC().x, triangle.getC().y -16 );

            ac.setStroke(Color.RED);
            _output.getChildren().add(ac);

            final Line bc = new Line (triangle.getB().x, triangle.getB().y -16, triangle.getC().x, triangle.getC().y -16 );

            bc.setStroke(Color.RED);
            _output.getChildren().add(bc);

            //       draw input points


            //voronoi verticies
//            final Circle shape = new Circle(triangle.getCenter().x, triangle.getCenter().y-16, diameter / 2);
//            shape.setFill(Color.GREEN);
//            shape.setStroke(Color.GREEN);
//            _output.getChildren().add(shape);
        }

        for (PointD point: input) {
            final Circle shape = new Circle(point.x, point.y-16, diameter / 2);
            shape.setFill(Color.BLACK);
            shape.setStroke(Color.BLACK);
            _output.getChildren().add(shape);
        }

    }

    private List<Double> toDoubleArray(List<PointD> vertices) {
        List<Double> coords = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            coords.add(vertices.get(i).x-16);
            coords.add(vertices.get(i).y-16);
        }
        return coords;
    }
}
