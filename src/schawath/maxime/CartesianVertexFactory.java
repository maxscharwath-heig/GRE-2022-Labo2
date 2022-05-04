package schawath.maxime;

import graph.core.VertexFactory;
import graph.data.CartesianVertexData;

public class CartesianVertexFactory implements VertexFactory<CartesianVertex, CartesianVertexData> {
    @Override
    public CartesianVertex makeVertex(int id, CartesianVertexData data) {
        return new CartesianVertex(id, data.x, data.y);
    }
}
