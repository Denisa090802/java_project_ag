package com.graph_coloring;

import com.graph_coloring.wrapper.VisualGraph;
import javafx.application.Platform;
import org.graph4j.Graph;

import static com.graph_coloring.Utils.colors;

public class ColoringBkt {

    private Graph graph;
    private VisualGraph visualGraph;

    private int[] coloring;
    private int minColorsUsed;
    private int numColors;

    public ColoringBkt(VisualGraph graph){

        this.visualGraph = graph;
        this.graph = graph.getGraph();
        this.coloring = new int[this.graph.numVertices()] ;
    }

    public void Execute() throws InterruptedException {
        minColorsUsed = graph.numVertices();
        coloring = new int[graph.numVertices()];

        backtrack(0, 0);

        numColors = minColorsUsed;

        backtrack(0);

        printSolution();
    }

    private void backtrack(int vertex, int colorsUsed) {
        if (vertex == graph.numVertices()) {
            minColorsUsed = Math.min(minColorsUsed, colorsUsed);
            return;
        }

        for (int color = 1; color <= colorsUsed + 1; color++) {
            if (isSafe(vertex, color)) {
                coloring[vertex] = color;
                backtrack(vertex + 1, Math.max(colorsUsed, color));
                coloring[vertex] = 0; // Backtracking
            }
        }
    }

    private boolean isSafe(int vertex, int color) {
        for (int i = 0; i < graph.numVertices(); i++) {
            if (graph.adjacencyMatrix()[vertex][i] == 1 && coloring[i] == color) {
                return false; // Vertex has an adjacent vertex with the same color
            }
        }
        return true;
    }

    private boolean backtrack(int vertex) {
        if (vertex == this.graph.numVertices()) {
            return true; // All vertices have been colored
        }

        for (int color = 1; color <= numColors; color++) {
            if (isSafe(vertex, color)) {
                coloring[vertex] = color;
                if (backtrack(vertex + 1)) {
                    return true;
                }
                coloring[vertex] = 0; // Backtracking
            }
        }

        return false; // No valid color found for the current vertex
    }

    private void printSolution() {
        System.out.println("Vertex\tColor");
        for (int i = 0; i < this.graph.numVertices(); i++) {

        }

        for (int u = 0; u < graph.numVertices(); u++)
        {
            final int _c = u;
            final int color = coloring[u] - 1;
            Platform.runLater(()->{
                visualGraph.getVertexes().get(_c).getCircle().setFill(
                        colors[color]
                );
            });
            System.out.println(u + "\t" + coloring[u]);
        }
    }

}
