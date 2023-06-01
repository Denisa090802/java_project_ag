package com.graph_coloring.genetic_algo;

import org.graph4j.Graph;
import org.javatuples.Pair;

import java.util.Random;

/**
 * @author Virna Stefan Alexandru
 */
public class Utils {
    public static int MaximumNumberOfColors(Graph graph)
    {
        int max_num_colors = 1;
        for(int i = 0; i < graph.numVertices(); i++)
        {
            int sum = 0;
            for(int j = 0; j < graph.adjacencyMatrix()[i].length; j++) {
                sum += graph.adjacencyMatrix()[i][j];
            }
            if(sum > max_num_colors) {
                max_num_colors = sum + 1;
            }
        }
        return max_num_colors;
    }

    public static Pair<Individual, Individual> Crossover(Individual parent1, Individual parent2) {
        int[] child1 = new int[parent1.getColors().length]; int child1Index = 0;
        int[] child2 = new int[parent2.getColors().length]; int child2Index = 0;

        Random rand = new Random();
        int position = rand.nextInt(2, child1.length-2);
        for (int i = 0; i < position + 1; i++) {
            child1[child1Index++] = parent1.getColors()[i];
            child2[child2Index++] = parent2.getColors()[i];
        }
        for(int i = position + 1; i < child1.length; i++) {
            child1[child1Index++] = parent2.getColors()[i];
            child2[child2Index++] = parent1.getColors()[i];
        }

        return new Pair(new Individual(child1), new Individual(child2));
    }

    // If gen < 200 => probability = 0.4 | else probability = 0.2
    public static Individual Mutation(Individual parent, double probability, int number_of_colors) {
        int[] genome = parent.getColors();
        Random rand = new Random();
        double check = rand.nextDouble(0, 1);
        if(check <= probability) {
            int position = rand.nextInt(0, genome.length-1);
            if(number_of_colors > 1) {
                genome[position] = rand.nextInt(1, number_of_colors);
            }
        }
        return new Individual(genome);
    }
}
