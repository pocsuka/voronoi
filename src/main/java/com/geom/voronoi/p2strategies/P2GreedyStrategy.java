package com.geom.voronoi.p2strategies;

import com.geom.voronoi.data.Triangle;
import com.geom.voronoi.data.Vertex;
import javafx.scene.paint.Color;
import org.kynosarges.tektosyne.geometry.LineD;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class P2GreedyStrategy implements P2Strategy {
    @Override
    public List<Vertex> getPoints(List<Triangle> triangles, int n, int width, int height) {
        List<Vertex> outputList = new ArrayList<>();
        ArrayList<LineD> edgeList = new ArrayList<>();
        for (Triangle triangle : triangles) {
            // Dont include outer triangles
            if (triangle.getA().x > 0 && triangle.getA().x < width) {
                if (triangle.getB().x > 0 && triangle.getB().x < width) {
                    edgeList.add(new LineD(triangle.getA(), triangle.getB()));
                }
                if (triangle.getC().x > 0 && triangle.getC().x < width) {
                    edgeList.add(new LineD(triangle.getC(), triangle.getA()));
                }
            }
            if (triangle.getC().x > 0 && triangle.getC().x < width) {
                if (triangle.getB().x > 0 && triangle.getB().x < width) {
                    edgeList.add(new LineD(triangle.getB(), triangle.getC()));
                }
            }
        }


        // TODO remove duplicates

        edgeList.sort(Comparator.comparingDouble(LineD::length).reversed());

        int i = 0;
        while (outputList.size() < n) {
            LineD edge = edgeList.get(i);
            PointD p = new PointD(
                0.5 * edge.start.x + 0.5 * edge.end.x,
                0.5 * edge.start.y + 0.5 * edge.end.y);
            Vertex v = new Vertex(p, Color.BLACK);
            if (!outputList.contains(v)) {
                outputList.add(v);
            }
            i++;
        }

        return outputList;
    }
}
