package com.geom.voronoi.gui;

import com.geom.voronoi.data.Triangle;
import com.geom.voronoi.triangulation.DelaunayTriangulator;
import com.geom.voronoi.utils.InputReader;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

import org.kynosarges.tektosyne.geometry.*;

import java.util.List;


public class VoronoiDialog extends Stage {

    private final Pane _output = new Pane();
    private PointD[] _points;

    public VoronoiDialog() {
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

        setOnShown(t -> draw(null));
    }

    /**
     * Draws a Voronoi diagram for the specified {@link PointD} array.
     * Creates a new {@link PointD} array if {@code points} is {@code null}.
     *
     * @param points the {@link PointD} array whose Voronoi diagram to draw
     */
    private void draw(PointD[] points) {
        final double diameter = 4;

    InputReader inputReader = new InputReader();
    inputReader.readFile("c:/git/voronoi/src/main/resources/circle10.txt");
//        List<PointD> input = new ArrayList<>(Arrays.asList(
//             (new PointD(253.0, 252.1))
//            , (new PointD(53.17, 89.81))
//            , (new PointD(321.7, 18.91))
//            , (new PointD(103.0, 131.5))
//            , (new PointD(134.0, 32.5))
//            , (new PointD(15.0, 98.3))
//            , (new PointD(3.0, 20.1))
//            , (new PointD(104, 200.5))
//        ));

    List<PointD> input;
    input = inputReader.getPoints();

        _points = input.toArray(new PointD[input.size()]);

        List<Triangle> triangles = null;

        final RectD clip = new RectD(0, 0, _output.getWidth(), _output.getHeight());
        final VoronoiResults results = Voronoi.findAll(input.toArray(new PointD[input.size()]), clip);
        _output.getChildren().clear();


        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(input, inputReader.getWidth(), inputReader.getHeight());
        delaunayTriangulator.triangulate();

        triangles = delaunayTriangulator.getTriangleSet().getAll();




//        for (PointD point: input) {
//            final Circle shape = new Circle(point.x, point.y, diameter / 2);
//            shape.setFill(Color.BLACK);
//            shape.setStroke(Color.BLACK);
//            _output.getChildren().add(shape);
//        }

        // draw interior of Voronoi regions
//        for (PointD[] region: results.voronoiRegions()) {
//            final Polygon polygon = new Polygon(PointD.toDoubles(region));
//            polygon.setFill(Color.PALEGOLDENROD);
//            polygon.setStroke(Color.WHITE);
//            polygon.setStrokeWidth(6);
//            _output.getChildren().add(polygon);
//        }
//
//        // draw edges of Voronoi diagram
        for (VoronoiEdge edge: results.voronoiEdges) {
            final PointD start = results.voronoiVertices[edge.vertex1];
            final PointD end = results.voronoiVertices[edge.vertex2];

            final Line line = new Line(start.x, start.y, end.x, end.y);
            line.setStroke(Color.RED);
            _output.getChildren().add(line);
        }
        LineD[] edges = results.delaunayEdges();
//         draw edges of Delaunay triangulation

        for (Triangle triangle : triangles) {

//            final Line ab = new Line (triangle.getA().x, triangle.getA().y, triangle.getB().x, triangle.getB().y );
//
//            ab.setStroke(Color.RED);
//            _output.getChildren().add(ab);
//
//            final Line ac = new Line (triangle.getA().x, triangle.getA().y, triangle.getC().x, triangle.getC().y );
//
//            ac.setStroke(Color.RED);
//            _output.getChildren().add(ac);
//
//            final Line bc = new Line (triangle.getB().x, triangle.getB().y, triangle.getC().x, triangle.getC().y );
//
//            bc.setStroke(Color.RED);
//            _output.getChildren().add(bc);

            final Circle shape = new Circle(triangle.getCenter().x, triangle.getCenter().y, diameter / 2);
            shape.setFill(Color.GREEN);
            shape.setStroke(Color.GREEN);
            _output.getChildren().add(shape);
        }

//        for (LineD edge: results.delaunayEdges()) {
//            final Line line = new Line(edge.start.x, edge.start.y, edge.end.x, edge.end.y);
//            line.getStrokeDashArray().addAll(3.0, 2.0);
//            line.setStroke(Color.BLUE);
//            _output.getChildren().add(line);
//        }

        // draw generator points


    }
}
