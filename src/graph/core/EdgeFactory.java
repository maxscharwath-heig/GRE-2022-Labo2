package graph.core;

/**
 * Describes a factory to build any type of user-defined edge
 * @param <V> Type of edge endpoints
 * @param <E> Edge type
 * @param <Data> Additional data type
 *
 * @author Thibaud Franchetti
 */
@FunctionalInterface
public interface EdgeFactory<V extends Vertex, E extends Edge<? super V>, Data> {

    /**
     * Builds an edge of type {@link E} given two vertices (origin and destination)
     * of type {@link V}.
     * @param from Origin vertex
     * @param to Destination vertex
     * @param additionalData Additional data that can be delegated to the factory,
     *                       if needed to build an instance of {@link E}
     * @return A new edge
     */
    E makeEdge(V from, V to, Data additionalData);
}
