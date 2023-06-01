package com.graph_coloring.genetic_algo;

import org.graph4j.Graph;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Virna Stefan Alexandru
 */
public class Individual {
    private int[] colors;

    public int[] getColors() { return colors; }

    public Individual(int[] colors) {
        this.colors = colors;
    }
    public static Individual RandomIndividual(Graph graph, int number_of_colors) {
        int[] randomColors = new int[graph.numVertices()];
        Random rand = new Random();
        for(int i = 0; i < randomColors.length; i++) {
            randomColors[i] = rand.nextInt(1, number_of_colors);
        }
        return new Individual(randomColors);
    }

    public int GetFitness(Graph graph) {
        int fitness = 0;
        for (int i = 0; i < graph.numVertices(); i++) {
            for(int j = i; j < graph.numVertices(); j++) {
                if(i != j)
                {
                    int[][] matrix = graph.adjacencyMatrix();
                    if(colors[i] == colors[j] && graph.adjacencyMatrix()[i][j] == 1) {
                        fitness += 1;
                    }
                }
            }
        }
        return fitness;
    }

    @Override
    public String toString() {
        return Arrays.toString(colors);
    }
}
