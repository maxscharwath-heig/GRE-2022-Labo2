package schawath.maxime;

import graph.core.EdgeWeighter;

public class CartesianEdgeWeighter implements EdgeWeighter<CartesianVertex> {
    public long weight(CartesianVertex from, CartesianVertex to) {
        return Math.round(Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2)));
    }
}
