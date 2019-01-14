package com.geom.voronoi.utils;

import com.geom.voronoi.data.Triangle;
import com.geom.voronoi.data.Vertex;
import com.geom.voronoi.p2strategies.P2RandomStrategy;
import com.geom.voronoi.p2strategies.P2Strategy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Player2StrategyWriter {
    private final int maxNumPoints;
    private final List<Triangle> triangles;
    private final P2Strategy p2Strategy;
    private final int width;
    private final int height;


    public Player2StrategyWriter(List<Triangle> triangles, int maxNumPoints, int width, int height) {
        this.maxNumPoints = maxNumPoints;
        this.triangles = triangles;
        this.width = width;
        this.height = height;
        this.p2Strategy = new P2RandomStrategy();
    }

    public void writePlayer2Strategy(String path) {
        System.out.println("Writing player 2 strategy to " + path);
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(path));
            bw.write(maxNumPoints + "\n");

            List<Vertex> p2strategyVertices = p2Strategy.getPoints(triangles, maxNumPoints, width, height);
            for (Vertex p2vertex : p2strategyVertices) {
                double x = p2vertex.getLocation().x;
                double y = p2vertex.getLocation().y;
                bw.write(x + ", " + y + "\n");
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
