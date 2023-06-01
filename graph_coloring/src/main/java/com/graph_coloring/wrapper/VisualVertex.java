package com.graph_coloring.wrapper;

import javafx.scene.shape.Circle;

/**
 * @author Virna Stefan Alexandru
 */
public class VisualVertex {
    private Circle circle;
    private static final int radius = 5;

    public VisualVertex(double x, double y) {
        circle = new Circle(x, y, radius);
    }

    public Circle getCircle() { return circle; }
    public static int getRadius () { return  radius; }

    @Override
    public boolean equals(Object o)
    {
        if(o == null) return false;
        if(!(o instanceof VisualVertex)) return false;
        if(this == o) return true;
        VisualVertex thatVertex = (VisualVertex) o;
        return ((getCircle().getCenterX() == thatVertex.getCircle().getCenterX()) &&
                (getCircle().getCenterY() == thatVertex.getCircle().getCenterY()));
    }
}
