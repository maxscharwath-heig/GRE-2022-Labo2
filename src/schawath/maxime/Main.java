
/*
 * Le code rendu se situe uniquement dans ce package (event. sous-packages)
 */
package schawath.maxime;

import graph.core.impl.SimpleWeightedEdgeFactory;
import graph.reader.CartesianGraphReader;

import java.io.IOException;
import java.util.Random;

public class Main {
    /*
     * NE PAS MODIFIER
     * Les fichiers de données sont à placer à la racine de ce répertoire
     */
    private static final String DATA_FOLDER = "data/";

    public static void main(String[] args) throws IOException {
        var graph = new CartesianGraphReader<>(
                new CartesianVertexFactory(),
                new SimpleWeightedEdgeFactory<>(new CartesianEdgeWeighter()),
                DATA_FOLDER + "R10000_1.txt"
        ).graph();
        //init random seed
        var seed = 20220404;
        var random = new Random(seed);
        long nbVisitedSimple = 0;
        long nbVisitedBidirectional = 0;
        for (int i = 0; i < 1000; i++) {
            var start = random.nextInt(graph.getNVertices());
            var end = random.nextInt(graph.getNVertices());
            var resultSimple = new Dijkstra<>(graph).run(start, end);
            var resultBidirectional = new DijkstraBidirectional<>(graph).run(start, end);
            if(resultBidirectional.getDistance() != resultSimple.getDistance()){
                System.out.println("ERROR with seed " + seed + " on iteration " + i + ", from " + start + " to " + end);
            }else{
                int simple = resultSimple.getNbVisited();
                int bidirectional = resultBidirectional.getNbVisited();
                nbVisitedSimple += simple;
                nbVisitedBidirectional += bidirectional;
                //if bidirectional is slower, print it
                if(resultBidirectional.getNbVisited() > resultSimple.getNbVisited()){
                    double percent = simple * 100.0 / bidirectional;
                    System.out.println("Bidirectional is " + percent + "% slower than simple with seed " + seed + " on iteration " + i + ", from " + start + " to " + end + " (simple: " + simple + ", bidirectional: " + bidirectional + ")");
                }
            }
        }
        //print results to show difference between simple and bidirectional
        System.out.println("=========== Results ===========");
        System.out.println("Simple : " + nbVisitedSimple + " bidirectional : " + nbVisitedBidirectional);
        System.out.println("bidirectional is " + (nbVisitedBidirectional * 100.0 / nbVisitedSimple) + "% better than simple");
    }
}