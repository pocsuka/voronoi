package com.geom.voronoi.data;

import javafx.scene.paint.Color;
import org.kynosarges.tektosyne.geometry.PointD;

public class Vertex {
    PointD location;
    Color color;

    public Vertex(PointD location, Color color) {
        this.location = location;
        this.color = color;
    }

    public PointD getLocation() {
        return location;
    }

    public Color getColor() {
        return color;
    }
}
