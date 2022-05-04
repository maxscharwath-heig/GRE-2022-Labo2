package graph.core;

/**
 * Describes a weighting function to compute a weight (type long)
 * given two vertices
 * @param <V>
 *
 * @author Thibaud Franchetti
 */
@FunctionalInterface
public interface EdgeWeighter<V extends Vertex> {

    /**
     * Computes a weight given two vertices
     * @param from Origin vertex
     * @param to Destination vertex
     * @return A weight
     */
    long weight(V from, V to);
}
