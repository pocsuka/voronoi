package com.geom.voronoi.utils;

import org.kynosarges.tektosyne.geometry.PointD;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Player1InputReader {
    private double height;
    private double width;
    private double RoundsOfPlayer1;
    private double RoundsOfPlayer2;


    private List<PointD> points;

    public Player1InputReader( ) {
        this.points = new ArrayList<>();
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getRoundsOfPlayer1() {
        return RoundsOfPlayer1;
    }

    public double getRoundsOfPlayer2() {
        return RoundsOfPlayer2;
    }

    public List<PointD> getPoints() {
        return points;
    }

    public void readFile(String path) {
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path));

            String[] splitted = bufferedReader.readLine().split(",");

            this.width = Double.parseDouble(splitted[0]);
            this.height = Double.parseDouble(splitted[1]);
            this.RoundsOfPlayer1 = Double.parseDouble(splitted[2]);
            this.RoundsOfPlayer2 = Double.parseDouble(splitted[3]);

            Stream<String> lines = bufferedReader.lines();
            lines.forEach(line -> createPoint(line));
            lines.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(points);
    }
    private void createPoint(String line) {
        String[] splitted = line.split(",");
        PointD point = new PointD(Double.parseDouble(splitted[0]),Double.parseDouble(splitted[1]));
        points.add(point);
    }
}
