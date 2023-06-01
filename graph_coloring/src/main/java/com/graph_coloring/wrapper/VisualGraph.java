package com.graph_coloring.wrapper;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.graph4j.Edge;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Virna Stefan Alexandru
 */
public class VisualGraph {
    private Graph Graph;
    private List<VisualVertex> Vertexes;
    private List<VisualEdge> Edges;
    private Pane UIContainer;

    public VisualGraph(Pane UIContainer) {
        this.UIContainer = UIContainer;

        Graph = GraphBuilder.numVertices(0).buildGraph();
        Vertexes = new ArrayList<>();
        Edges = new ArrayList<>();
    }

    public void addVertex(VisualVertex vertex) {
        Graph.addVertex();
        Vertexes.add(vertex);
    }

    public void removeVertex(VisualVertex vertex) {
        int index = Vertexes.indexOf(vertex);
        Vertexes.remove(vertex);
        Graph.removeVertex(index);
    }

    public VisualVertex findClosestVertex(double x, double y) {
        return findVertex(new VisualVertex(x, y));
    }

    public VisualVertex findVertex(VisualVertex fakeVertex) {
        VisualVertex target = null;
        for(VisualVertex v : Vertexes)
        {
            if(areIntersecting(v.getCircle(), fakeVertex.getCircle()))
            {
                target = v;
                return target;
            }
        }
        return target;
    }

    public boolean hasEdge(VisualVertex vertex1, VisualVertex vertex2) {
        for(VisualEdge edge : Edges) {
            if(edge.getVertex1() == vertex1 && edge.getVertex2() == vertex2) {
                return true;
            }
        }
        return false;
    }

    public VisualEdge getEdge(VisualVertex vertex1, VisualVertex vertex2) {
        for(VisualEdge edge : Edges) {
            if(edge.getVertex1() == vertex1 && edge.getVertex2() == vertex2) {
                return edge;
            }
        }
        return null;
    }

    public List<VisualVertex> getVertexes() {
         return Vertexes;
    }

    public Graph getGraph() {
        return Graph;
    }

    public void addEdge(VisualEdge edge) {
        Edges.add(edge);
        Graph.addEdge(
                Vertexes.indexOf(edge.getVertex1()),
                Vertexes.indexOf(edge.getVertex2())
        );
    }

    public void removeEdge(VisualEdge edge) {
        Edges.remove(edge);
        try {
            Graph.removeEdge(
                    Vertexes.indexOf(edge.getVertex1()),
                    Vertexes.indexOf(edge.getVertex2())
            );
        } catch (Exception e) {
            try {
                Graph.removeEdge(
                        Vertexes.indexOf(edge.getVertex2()),
                        Vertexes.indexOf(edge.getVertex1())
                );
            }
            catch (Exception e2) {

            }
        }
    }

    public void removeAll()
    {
        Vertexes.clear();
        Edges.clear();
    }

    public boolean areIntersecting(Circle existingCircle, Circle clickedCircle)
    {
        double d = Math.sqrt((Math.abs((clickedCircle.getCenterX()-existingCircle.getCenterX()))
                * Math.abs((clickedCircle.getCenterX()-existingCircle.getCenterX())))
                + (Math.abs(clickedCircle.getCenterY()-existingCircle.getCenterY()))
                * (Math.abs(clickedCircle.getCenterY()-existingCircle.getCenterY())));
        return (d <= VisualVertex.getRadius() && d >= 0);
    }
}
