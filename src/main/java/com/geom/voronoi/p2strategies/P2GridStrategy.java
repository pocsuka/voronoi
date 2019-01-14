package com.geom.voronoi.p2strategies;

import com.geom.voronoi.data.Triangle;
import com.geom.voronoi.data.Vertex;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.BLACK;

public class P2GridStrategy implements P2Strategy {
    @Override
    public List<Vertex> getPoints(List<Triangle> triangles, int n, int width, int height) {
        double ratio = 1.0 * width / (width + height);
        int cols = (int) ((1.0 * ratio * n) + 1);
        int rows = (int) ((1.0 * (1-ratio) * n) + 1);


        ArrayList<Vertex> list = new ArrayList<Vertex>();
        for (int i = 0; i < n; i++) {
            int col = i % rows;
            int row = i / rows;

            double cur_x = 1.0 * col * width / cols;
            double cur_y = 1.0 * row * height / rows;

            list.add(new Vertex(new PointD(cur_x, cur_y), BLACK));
        }

        return list;
    }
}
