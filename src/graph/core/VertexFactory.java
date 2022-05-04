package graph.core;

/**
 * Describes a factory to build any type of user-defined vertex
 * @param <V> Vertex type
 * @param <Data> Additional data type
 *
 * @author Thibaud Franchetti
 */
@FunctionalInterface
public interface VertexFactory<V extends Vertex, Data> {

    /**
     * Builds a vertex of type {@link V} given its id
     * @param id Vertex id
     * @param additionalData Additional data that can be delegated to the factory,
     *                       if needed to build an instance of {@link V}
     * @return A new vertex
     */
    V makeVertex(int id, Data additionalData);
}
