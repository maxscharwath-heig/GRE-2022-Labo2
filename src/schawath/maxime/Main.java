
/*
 * Le code rendu se situe uniquement dans ce package (event. sous-packages)
 */
package schawath.maxime;

import graph.core.impl.SimpleWeightedEdge;
import graph.core.impl.SimpleWeightedEdgeFactory;
import graph.reader.CartesianGraphReader;

import java.io.IOException;

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
        var dijkstra2 = new DijkstraBidirectional<>(graph);
        dijkstra2.run(234,8940);
    }
}