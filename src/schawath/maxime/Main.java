
/*
 * Le code rendu se situe uniquement dans ce package (event. sous-packages)
 */
package schawath.maxime;

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
        var dijkstra1 = new Dijkstra<>(graph);
        var r1 = dijkstra1.run(0, 14);
        r1.print();

        var dijkstra2 = new DijkstraBidirectional<>(graph);
        var r2 = dijkstra2.run(0, 14);
        r2.print();
    }
}