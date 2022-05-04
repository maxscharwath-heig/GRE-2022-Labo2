package schawath.maxime;

import graph.core.Vertex;
import graph.data.CartesianVertexData;

public final class CartesianVertex implements Vertex {
    private final int id;

    private final int x;
    private final int y;

    public CartesianVertex(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int id() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + id + ", x=" + x + ", y=" + y + ")";
    }
}
