package com.geom.voronoi.data;

import javafx.scene.paint.Color;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return location.equals(vertex.location) &&
            color.equals(vertex.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, color);
    }
}
