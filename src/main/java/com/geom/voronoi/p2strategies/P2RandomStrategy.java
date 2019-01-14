package com.geom.voronoi.p2strategies;

import com.geom.voronoi.data.Triangle;
import com.geom.voronoi.data.Vertex;
import javafx.scene.paint.Color;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class P2RandomStrategy implements P2Strategy {
    private final Random random;

    public P2RandomStrategy() {
        random = new Random();
    }

    @Override
    public List<Vertex> getPoints(List<Triangle> triangles, int n, int width, int height) {
        ArrayList<Vertex> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            double x = random.nextDouble() * width;
            double y = random.nextDouble() * height;
            list.add(new Vertex(new PointD(x, y), Color.GREEN));
        }

        return list;
    }
}
