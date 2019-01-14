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
    private int height;
    private int width;
    private int roundsOfPlayer1;
    private int roundsOfPlayer2;


    private List<PointD> points;

    public Player1InputReader( ) {
        this.points = new ArrayList<>();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getRoundsOfPlayer1() {
        return roundsOfPlayer1;
    }

    public int getRoundsOfPlayer2() {
        return roundsOfPlayer2;
    }

    public List<PointD> getPoints() {
        return points;
    }

    public void readFile(String path) {
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path));

            String[] splitted = bufferedReader.readLine().split(",");

            this.width = Integer.parseInt(splitted[0].trim());
            this.height = Integer.parseInt(splitted[1].trim());
            this.roundsOfPlayer1 = Integer.parseInt(splitted[2].trim());
            this.roundsOfPlayer2 = Integer.parseInt(splitted[3].trim());

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
