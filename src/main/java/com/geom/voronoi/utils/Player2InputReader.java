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

public class Player2InputReader {
    private double RoundsOfPlayer2;

    private List<PointD> points;

    public Player2InputReader( ) {
        this.points = new ArrayList<>();
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
            this.RoundsOfPlayer2 = Double.parseDouble(bufferedReader.readLine());

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
