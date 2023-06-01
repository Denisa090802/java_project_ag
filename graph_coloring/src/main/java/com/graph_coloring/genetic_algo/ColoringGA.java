package com.graph_coloring.genetic_algo;

import com.graph_coloring.wrapper.VisualGraph;
import com.graph_coloring.wrapper.VisualVertex;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.graph4j.Graph;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collections;

import static com.graph_coloring.Utils.colors;

/**
 * @author Virna Stefan Alexandru
 */
public class ColoringGA {
    private int maximumGenerations;
    private int populationSize;
    private Graph graph;
    private VisualGraph visualGraph;
    private int number_of_colors;

    private ArrayList<Individual> population;

    public ColoringGA(
            VisualGraph graph,
            int populationSize,
            int maximumGenerations
    ) {
        this.graph = graph.getGraph();
        this.visualGraph = graph;
        this.populationSize = populationSize;
        this.maximumGenerations = maximumGenerations;
        this.number_of_colors = Utils.MaximumNumberOfColors(this.graph);

        population = new ArrayList<>();
    }

    public void Execute() throws InterruptedException {
        Individual previous_fittest_individual = null;
        while(number_of_colors > 0) {
            population.clear();
            for(int i = 0; i < populationSize; i++) {
                population.add(Individual.RandomIndividual(graph, number_of_colors));
            }

            int best_fitness = population.get(0).GetFitness(graph);
            Individual fittest_individual = population.get(0);
            int gen = 0;
            while(best_fitness != 0 && gen < maximumGenerations) {
                gen++;
                population = Selection();
                ArrayList<Individual> new_population = new ArrayList<>();
                Collections.shuffle(population);
                for(int i = 0; i < populationSize - 1; i += 2) {
                    Pair<Individual, Individual> children = Utils.Crossover(population.get(i), population.get(i+1));
                    new_population.add(children.getValue0());
                    new_population.add(children.getValue1());
                }
                for(int i = 0; i < new_population.size(); i++) {
                    if(gen < maximumGenerations / 50) {
                        new_population.set(i, Utils.Mutation(new_population.get(i), 0.4, number_of_colors));
                    } else {
                        new_population.set(i, Utils.Mutation(new_population.get(i), 0.2, number_of_colors));
                    }
                }
                population = new_population;
                best_fitness = population.get(0).GetFitness(graph);
                fittest_individual = population.get(0);
                for(int i = 0; i < population.size(); i++) {
                    if(population.get(i).GetFitness(graph) <  best_fitness) {
                        best_fitness = population.get(i).GetFitness(graph);
                        fittest_individual = population.get(i);
                    }
                }
                if(gen % 10 == 0) {
                    System.out.println("Generation: "+ gen + " Best_Fitness: " + best_fitness + " Individual " + fittest_individual);
                    for(int c = 0; c < fittest_individual.getColors().length; c++)
                    {
                        final int _c = c;
                        final Individual _fittest_individual = fittest_individual;
                        Platform.runLater(()->{
                            visualGraph.getVertexes().get(_c).getCircle().setFill(
                                    colors[_fittest_individual.getColors()[_c] - 1]
                            );
                        });
                    }
                }
            }
            System.out.println("Using " + number_of_colors + ":\nBest fitness " + best_fitness + " Generation " + gen + " Individual " + fittest_individual );
            if(best_fitness != 0) {
                System.out.println("Graph is " + (number_of_colors) + " colorable");
                final Individual _fittest_individual = previous_fittest_individual;
                for(int c = 0; c < previous_fittest_individual.getColors().length; c++)
                {
                    final int _c = c;
                    Platform.runLater(()->{
                        visualGraph.getVertexes().get(_c).getCircle().setFill(
                                colors[_fittest_individual.getColors()[_c] - 1]
                        );
                    });
                }
                break;
            } else {
                number_of_colors -= 1;
            }
            previous_fittest_individual = fittest_individual;
        }
    }

    public ArrayList<Individual> Selection()
    {
        ArrayList<Individual> new_population = new ArrayList<>();
        for(int j = 0; j < 2; j++) {
            Collections.shuffle(population);
            for(int i = 0; i < populationSize-1; i+=2) {
                if(population.get(i).GetFitness(graph) <  population.get(i+1).GetFitness(graph)) {
                    new_population.add(population.get(i));
                } else {
                    new_population.add(population.get(i+1));
                }
            }
        }
        return new_population;
    }
}
