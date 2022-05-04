package graph.core.impl;

import graph.core.Edge;
import graph.core.Vertex;

/**
 * {@link Edge} implementation describing a weighted edge with a long weight
 * @param <V> Associated vertex type
 *
 * @author Thibaud Franchetti
 */
public final class SimpleWeightedEdge<V extends Vertex> implements Edge<V> {
    /** Source vertex */
    private final V from;

    /** Destination vertex */
    private final V to;

    /** Edge weight */
    private final long weight;

    /**
     * Builds an edge
     * @param from Origin vertex
     * @param to Destination vertex
     * @param weight Edge weight
     */
    public SimpleWeightedEdge(final V from, final V to, final long weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public V from() {
        return from;
    }

    @Override
    public V to() {
        return to;
    }

    /**
     * @return Edge weight
     */
    public long weight() {
        return weight;
    }
}
