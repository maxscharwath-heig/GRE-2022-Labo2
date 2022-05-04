package graph.core;

/**
 * Defines an edge between two vertices of type {@link V}
 * @param <V> Associated vertex type
 *
 * @author Thibaud Franchetti
 */
public interface Edge<V extends Vertex> {

    /**
     * @return Origin vertex
     */
    V from();

    /**
     * @return Destination vertex
     */
    V to();
}
