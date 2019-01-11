package com.geom.voronoi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player1StrategyReader {
    private boolean could_parse_file = false;
    private int p1_max_moves;
    private int p2_max_moves;
    private List<Vertex> p1_vertices;
    private int x_max;
    private int y_max;

    public Player1StrategyReader(String filename) {
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            parse_config_line(br.readLine());

            String line;
            while ((line = br.readLine()) != null) {
                parse_vertex_line(line);
            }
            System.out.println("Done reading file");
        } catch (IOException e) {
            could_parse_file = false;
            e.printStackTrace();
        }
    }

    // Only basic validation
    boolean validate() {
        return could_parse_file && p1_vertices.size() == p1_max_moves;
    }

    private void parse_vertex_line(String line) {
        String[] values = line.split(",");
        int x = Integer.parseInt(values[0]);
        int y = Integer.parseInt(values[1]);

        p1_vertices.add(Vertex.fromCoordinate(x, y));
    }

    private void parse_config_line(String configLine) {
        String[] values = configLine.split(",");
        x_max = Integer.parseInt(values[0]);
        y_max = Integer.parseInt(values[1]);
        p1_max_moves = Integer.parseInt(values[2]);
        p2_max_moves = Integer.parseInt(values[3]);

        p1_vertices = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Player1StrategyReader{" +
            "p1_max_moves=" + p1_max_moves +
            ", p2_max_moves=" + p2_max_moves +
            ", p1_vertices=" + p1_vertices +
            ", x_max=" + x_max +
            ", y_max=" + y_max +
            ", valid=" + could_parse_file +
            '}';
    }

    public static void main(String[] args) {
        String filename = args[0];
        System.out.println("Reading " + filename);

        Player1StrategyReader p1sr = new Player1StrategyReader(filename);
        System.out.println(p1sr);
    }
}
