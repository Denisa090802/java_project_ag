package com.graph_coloring;

import com.graph_coloring.genetic_algo.Individual;
import com.graph_coloring.wrapper.VisualGraph;
import javafx.application.Platform;
import org.graph4j.Graph;

import java.util.Arrays;
import java.util.Iterator;

import static com.graph_coloring.Utils.colors;

public class ColoringGreedy {

    private Graph graph;
    private VisualGraph visualGraph;

    public ColoringGreedy(VisualGraph graph){
        this.visualGraph = graph;
        this.graph = graph.getGraph();
    }
    public void Execute() throws InterruptedException{
        int result[] = new int[graph.numVertices()];

        // Initialize all vertices as unassigned
        Arrays.fill(result, -1);

        // Assign the first color to first vertex
        result[0] = 0;

        // A temporary array to store the available colors. False
        // value of available[cr] would mean that the color cr is
        // assigned to one of its adjacent vertices
        boolean available[] = new boolean[graph.numVertices()];

        // Initially, all colors are available
        Arrays.fill(available, true);

        // Assign colors to remaining V-1 vertices
        for (int u = 1; u < graph.numVertices(); u++)
        {
            // Process all adjacent vertices and flag their colors
            // as unavailable

            for(int j=0;j<graph.adjacencyMatrix()[u].length;j++)
            {
                int i=graph.adjacencyMatrix()[u][j];
                if (result[i] != -1)
                    available[result[i]] = false;
            }

            // Find the first available color
            int cr;
            for (cr = 0; cr < graph.numVertices(); cr++){
                if (available[cr])
                    break;
            }

            result[u] = cr; // Assign the found color

            // Reset the values back to true for the next iteration
            Arrays.fill(available, true);
        }

        // print the result
        for (int u = 0; u < graph.numVertices(); u++)
        {
            final int _c = u;
            final int color = result[u];
            Platform.runLater(()->{
                visualGraph.getVertexes().get(_c).getCircle().setFill(
                        colors[color]
                );
            });
            System.out.println("Vertex " + u + " ---> Color "
                    + result[u]);
        }
    }


}
