package graph.core.impl;

import graph.core.EdgeFactory;
import graph.core.EdgeWeighter;
import graph.core.Vertex;

/**
 * Implementation of {@link EdgeFactory} for building {@link SimpleWeightedEdge}
 * @param <V> Vertex type associated to the edge
 *
 * @author Thibaud Franchetti
 */
public final class SimpleWeightedEdgeFactory<V extends Vertex> implements EdgeFactory<V, SimpleWeightedEdge<V>, Void> {
    /** Weighting function */
    private final EdgeWeighter<V> weighter;

    /**
     * Build a factory
     * @param weighter Weighting function
     */
    public SimpleWeightedEdgeFactory(EdgeWeighter<V> weighter) {
        this.weighter = weighter;
    }

    @Override
    public SimpleWeightedEdge<V> makeEdge(V from, V to, Void additionalData) {
        var weight = weighter.weight(from, to);
        return new SimpleWeightedEdge<>(from, to, weight);
    }
}
