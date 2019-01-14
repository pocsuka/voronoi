package com.geom.voronoi.gui;

import com.geom.voronoi.data.Triangle;
import com.geom.voronoi.data.VoronoiRegion;
import com.geom.voronoi.data.Vertex;
import com.geom.voronoi.triangulation.DelaunayTriangulator;
import com.geom.voronoi.utils.Player1InputReader;
import com.geom.voronoi.utils.Player2InputReader;
import com.geom.voronoi.state.GameState;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

import org.kynosarges.tektosyne.geometry.*;

import java.util.*;


public class VoronoiDialog extends Stage {

    private final Pane _output = new Pane();
    private final Pane infos = new Pane();
    private final Player2InputReader player2InputReader;
    private final Player1InputReader player1InputReader;
    List<Double> redAreas = new ArrayList<>();
    List<Double> blueAreas = new ArrayList<>();
    List<Vertex> input = new ArrayList<>();
    private GameState gameState;
    Label standing;

    int counter = 0;

    public VoronoiDialog(String p1StrategyFileString, String p2StrategyFileString) {

        standing = new Label("Red: % vs Blue: % ");
        standing.setPrefSize(300,30);

        player1InputReader = new Player1InputReader();
        player2InputReader = new Player2InputReader();
        //TODO: fix path stuff
        player1InputReader.readFile(p1StrategyFileString);
        player2InputReader.readFile(p2StrategyFileString);

        gameState = new GameState(player1InputReader.getRoundsOfPlayer1(), player1InputReader.getRoundsOfPlayer2());
        gameState.setRedPlayer(true);
        initOwner(Global.primaryStage());
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.DECORATED);

//       Global.clipChildren(_output);
        _output.setPrefSize(player1InputReader.getWidth(), player1InputReader.getHeight());

        final VBox root = new VBox(infos, _output);
//        VBox.setVgrow(_output, Priority.ALWAYS);

        setResizable(true);
        setScene(new Scene(root));
        setTitle("Voronoi & Delaunay Test");
        sizeToScene();

        infos.getChildren().addAll(standing);

        input = convertPointDListToVertexList(player1InputReader.getPoints(),Color.RED);
        List<Vertex> inputp2 = convertPointDListToVertexList(player2InputReader.getPoints(),Color.BLUE);
        input.addAll(inputp2);
        setOnShown(t -> draw());
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PointD newPoint = new PointD(mouseEvent.getX(), mouseEvent.getY());
//                PointD newPoint = new PointD(mouseEvent.getX(), mouseEvent.getY() + 16);
//                triangulationPoints.add(newPoint);
                Color color = gameState.isRedPlayer() ? Color.RED : Color.BLUE;

                if (counter == gameState.getRedPlayerRounds()) {
                    gameState.setRedPlayer(false);
                }
                counter++;

                input.add(new Vertex(newPoint, color));
                System.out.println(newPoint);
                if(input.size() > 3) {
//                    draw();
                }
            }
        });
    }


    private void draw() {
        final double diameter = 8;

        List<Triangle> triangles;

        _output.getChildren().clear();

        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(input, player1InputReader.getWidth(), player1InputReader.getHeight());
        delaunayTriangulator.triangulate();

        triangles = delaunayTriangulator.getTriangleSet().getAll();

        HashSet<PointD> visited = new HashSet<>();

        PointD p1 = new PointD(-10000, -10000);
        PointD p2 = new PointD(10000 , -10000);
        PointD p3 = new PointD(0, 10000);

        visited.add(p1);
        visited.add(p2);
        visited.add(p3);

        PointD maxCoord = new PointD(player1InputReader.getWidth() + 16, player1InputReader.getHeight() + 16);

        for (Triangle triangle : triangles) {
            for (PointD vertex: triangle.getPointsAsList()) {
                if (visited.contains(vertex)) continue;
                visited.add(vertex);
                List<PointD> vertices = delaunayTriangulator.getTriangleSet().getAllNeighbouringTriangleCenter(vertex);

                int index = (GeoUtils.nearestPoint(toPointDList(input), vertex));

                Vertex point = input.get(index);

                Color color = point.getColor().equals(Color.RED) ? Color.SALMON : Color.STEELBLUE;

                VoronoiRegion voronoiRegion = new VoronoiRegion(color, vertices, maxCoord);

                if (point.getColor().equals(Color.RED)) {

                    redAreas.add(voronoiRegion.getArea());
                } else {
                    blueAreas.add(voronoiRegion.getArea());

                }


                Polygon polygon = new Polygon();
                polygon.getPoints().addAll(toDoubleArray(voronoiRegion.getVertices()));
                polygon.setFill(voronoiRegion.getColor());
                polygon.setStroke(Color.BLACK);
                _output.getChildren().add(polygon);

            }

//            triangulation lines
//            final Line ab = new Line (triangle.getA().x, triangle.getA().y -16, triangle.getB().x, triangle.getB().y -16 );
//
//            ab.setStroke(Color.RED);
//            _output.getChildren().add(ab);
//
//            final Line ac = new Line (triangle.getA().x, triangle.getA().y -16, triangle.getC().x, triangle.getC().y -16 );
//
//            ac.setStroke(Color.RED);
//            _output.getChildren().add(ac);
//
//            final Line bc = new Line (triangle.getB().x, triangle.getB().y -16, triangle.getC().x, triangle.getC().y -16 );
//
//            bc.setStroke(Color.RED);
//            _output.getChildren().add(bc);

            //       draw input points


            //voronoi verticies
//            final Circle shape = new Circle(triangle.getCenter().x, triangle.getCenter().y, diameter / 2);
//            shape.setFill(Color.GREEN);
//            shape.setStroke(Color.GREEN);
//            _output.getChildren().add(shape);

        }

        double redAreaSum = redAreas.stream().mapToDouble(area->area).sum();
        double blueAreaSum = blueAreas.stream().mapToDouble(area->area).sum();

        double total = redAreaSum + blueAreaSum;

        System.out.println("red area: " + (redAreaSum/total) * 100);
        System.out.println("blue area: " + (blueAreaSum/total)* 100);

        String red = String.format(String.format("Red: %g", (redAreaSum/total) * 100));
        red = red.concat("%");
        String blue = String.format(String.format("        Blue: %g", (blueAreaSum/total) * 100));
        blue = blue.concat("%");

        standing.setText(red + blue);

        for (Vertex point: input) {
            final Circle shape = new Circle(point.getLocation().x, point.getLocation().y, diameter / 2);
            shape.setFill(point.getColor());
            _output.getChildren().add(shape);
        }

    }

    private List<Double> toDoubleArray(List<PointD> vertices) {
        List<Double> coords = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            coords.add(vertices.get(i).x);
            coords.add(vertices.get(i).y);
        }
        return coords;
    }

    public static List<PointD> toPointDList(List<Vertex> vertices) {
        List<PointD> coords = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            coords.add(new PointD(vertices.get(i).getLocation().x, vertices.get(i).getLocation().y));
        }
        return coords;
    }

    private List<Vertex> convertPointDListToVertexList(List<PointD> vertices, Color color) {

        List<Vertex> converted = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++){
            converted.add(new Vertex(vertices.get(i), color));
        }
        return  converted;
    }

}
