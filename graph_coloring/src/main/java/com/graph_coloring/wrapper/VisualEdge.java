package com.graph_coloring.wrapper;

import javafx.scene.shape.Line;

/**
 * @author Virna Stefan Alexandru
 */
public class VisualEdge {
    private Line line;
    private VisualVertex vertex1;
    private VisualVertex vertex2;

    public VisualEdge(VisualVertex vertex1, VisualVertex vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        line = new Line(vertex1.getCircle().getCenterX(), vertex1.getCircle().getCenterY(),
                vertex2.getCircle().getCenterX(), vertex2.getCircle().getCenterY());
        line.setStrokeWidth(1);
    }

    public VisualVertex getVertex1()
    {
        return vertex1;
    }

    public VisualVertex getVertex2()
    {
        return vertex2;
    }

    public Line getLine()
    {
        return line;
    }

    private double calculate(VisualVertex vertex1, VisualVertex vertex2)
    {
        return Math.sqrt((Math.abs((vertex1.getCircle().getCenterX()-vertex2.getCircle().getCenterX()))
                * Math.abs((vertex1.getCircle().getCenterX()-vertex2.getCircle().getCenterX())))
                + (Math.abs(vertex1.getCircle().getCenterY()-vertex2.getCircle().getCenterY()))
                * (Math.abs(vertex1.getCircle().getCenterY()-vertex2.getCircle().getCenterY())));
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(!(o instanceof VisualEdge)) return false;
        VisualEdge e = (VisualEdge) o;

        int tx1 = (int)getLine().getStartX();
        int ty1 = (int)getLine().getStartY();
        int tx2 = (int)getLine().getEndX();
        int ty2 = (int)getLine().getEndY();

        int ox1 = (int)e.getLine().getStartX();
        int oy1 = (int)e.getLine().getStartY();
        int ox2 = (int)e.getLine().getEndX();
        int oy2 = (int)e.getLine().getEndY();

        if((tx1==ox1) && (ty1==oy1) && (tx2==ox2) && (ty2==oy2)) return true;
        else if((tx1==ox2) && (ty1==oy2) && (tx2==ox1) && (ty2==oy1)) return true;
        return false;
    }
}
