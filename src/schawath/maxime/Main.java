
/*
 * Le code rendu se situe uniquement dans ce package (event. sous-packages)
 */
package schawath.maxime;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;
import graph.core.impl.SimpleWeightedEdgeFactory;
import graph.reader.CartesianGraphReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    /*
     * NE PAS MODIFIER
     * Les fichiers de données sont à placer à la racine de ce répertoire
     */
    private static final String DATA_FOLDER = "data/";

    public static void main(String[] args) throws IOException {
        Digraph<CartesianVertex, SimpleWeightedEdge<CartesianVertex>> graph = new CartesianGraphReader<>(
                new CartesianVertexFactory(),
                new SimpleWeightedEdgeFactory<>(new CartesianEdgeWeighter()),
                DATA_FOLDER + "R10000_1.txt"
        ).graph();

        //Create some stats about the two Dijkstra algorithms versions.

        int seed = 20220404;
        Random random = new Random(seed);
        int nbIterations = 1000;
        long nbVisitedSimple = 0;
        long nbVisitedBidirectional = 0;

        //print some info before the run
        System.out.println("====== Run with seed " + seed + " ======");
        System.out.println("Graph size: " + graph.getNVertices());
        System.out.println("Number of iterations: " + nbIterations);


        FileWriter csvFile = new FileWriter("results.csv"); //Create a csv file to store the results for statistical analysis.

        //write CSV header
        csvFile.write("distanceBidirectional, distanceSimple, nbVisitedBidirectional, nbVisitedSimple, nbNodesBidirectional, nbNodesSimple\n");
        for (int i = 0; i < nbIterations; i++) {
            //print the progress every 1/10 iterations
            if (i % (nbIterations*0.1) == 0) {
                System.out.println("Progress: " + (i*100/nbIterations) + "%");
            }
            int start = random.nextInt(graph.getNVertices()); //randomly choose a start vertex
            int end = random.nextInt(graph.getNVertices()); //randomly choose an end vertex
            DijkstraResult resultSimple = new Dijkstra<>(graph).run(start, end); //run the simple Dijkstra algorithm
            DijkstraResult resultBidirectional = new DijkstraBidirectional<>(graph).run(start, end); //run the bidirectional Dijkstra algorithm

            //if the distance is not the same, something went wrong (hopefully not found this case)
            if (resultBidirectional.getDistance() != resultSimple.getDistance()) {
                System.out.println("ERROR with seed " + seed + " on iteration " + i + ", from " + start + " to " + end);
            } else {
                int simple = resultSimple.getNbVisited(); //get the number of visited nodes for the simple version
                int bidirectional = resultBidirectional.getNbVisited(); //get the number of visited nodes for the bidirectional version

                //add the number of visited nodes to the total number of visited nodes
                nbVisitedSimple += simple;
                nbVisitedBidirectional += bidirectional;

                //if bidirectional is slower, print it
                if (resultBidirectional.getNbVisited() > resultSimple.getNbVisited()) {
                    double percent = simple * 100.0 / bidirectional;
                    System.out.println("Bidirectional is " + percent + "% slower than simple with seed " + seed + " on iteration " + i + ", from " + start + " to " + end + " (simple: " + simple + ", bidirectional: " + bidirectional + ")");
                }

                //write the results to the csv file
                csvFile.write(resultBidirectional.getDistance() + "," + resultSimple.getDistance() + "," + bidirectional + "," + simple + "," + resultSimple.getVerticesList().size() + "," + resultBidirectional.getVerticesList().size() + "\n");
            }
        }
        csvFile.close();
        System.out.println("=========== Results ===========");
        System.out.println("Simple : " + nbVisitedSimple + " bidirectional : " + nbVisitedBidirectional);
        System.out.println("bidirectional is " + (nbVisitedBidirectional * 100.0 / nbVisitedSimple) + "% better than simple");
        System.out.println("Results stored in results.csv in the current folder");
    }
}