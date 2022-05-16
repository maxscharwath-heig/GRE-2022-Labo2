package schawath.maxime;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;

import java.util.*;

/**
 * Dijkstra algorithm bidirectional version
 *
 * @param <V> Vertex type
 * @param <D> Digraph type
 */
public class DijkstraBidirectional<V extends CartesianVertex, D extends Digraph<V, SimpleWeightedEdge<V>>> {
    private final D graph;

    private double mu;
    private int steps;
    private int nbVisited;

    /**
     * Create a new DijkstraBidirectional instance
     *
     * @param graph - the graph to use
     */
    public DijkstraBidirectional(D graph) {
        this.graph = graph;
    }

    /**
     * Run the algorithm for one step
     *
     * @param ctx  - the context to use
     * @param ctx2 - the inverse context to use
     * @return false if the algorithm is finished, true otherwise
     */
    private boolean step(DijkstraContext ctx, DijkstraContext ctx2) {
        //get the vertex with the smallest distance
        if (ctx.isQueueEmpty()) return false;
        CartesianVertex vi = ctx.pollQueue();
        nbVisited++; //increment the number of visited nodes for statistics
        if (ctx.getDelta(vi.id()) == Double.POSITIVE_INFINITY) {
            return false;
        }
        //for each neighbor of u
        for (SimpleWeightedEdge<V> ej : graph.getSuccessorList(vi.id())) {
            ++steps; //increment the number of steps for statistics
            V vj = ej.to();
            if (ctx.isInQueue(vj.id()) && ctx.getDelta(vj.id()) > ctx.getDelta(vi.id()) + ej.weight()) {
                ctx.setDelta(vj.id(), ctx.getDelta(vi.id()) + ej.weight());
                ctx.setPredecessor(vj.id(), vi.id());
                ctx.updateQueue(vj);
            }
            //mu update
            double m;
            if (!ctx2.isInQueue(vj.id()) && mu > (m = ctx.getDelta(vi.id()) + ctx2.getDelta(vj.id()) + ej.weight())) {
                mu = m;
                ctx.setSubTo(vi.id()); // update the sub-to node used to construct the path
            }
        }
        if (!ctx2.isInQueue(vi.id())) {
            ctx.setSubTo(vi.id()); // update the sub-to node used to construct the path
            return false;
        }
        return true;
    }

    /**
     * Merge the two contexts to get the final path
     *
     * @param forwardCtx  - forward context
     * @param backwardCtx - backward context
     * @return the merged context
     */
    List<Integer> merge(DijkstraContext forwardCtx, DijkstraContext backwardCtx) {
        LinkedList<Integer> s = new LinkedList<>();
        int cS = forwardCtx.getSubTo();
        while (cS != -1) {
            s.add(0, cS); // add to the beginning of the list
            cS = forwardCtx.getPredecessor(cS);
        }
        cS = backwardCtx.getPredecessor(backwardCtx.getSubTo());
        while (cS != -1) {
            s.add(cS); // add to the end of the list ( because it's inverted )
            cS = backwardCtx.getPredecessor(cS);
        }
        return s;
    }

    /**
     * Run the algorithm
     *
     * @param from - the source vertex id
     * @param to   - the destination vertex id
     * @return the result of the algorithm
     */
    public DijkstraResult run(int from, int to) {
        //initialize the main variables
        steps = 0;
        nbVisited = 0;
        mu = Double.POSITIVE_INFINITY;
        DijkstraContext forward = new DijkstraContext(from, to); // forward context
        DijkstraContext backward = new DijkstraContext(to, from); // backward context
        // used to alternate between the two contexts ( if one is finished, the other is cancelled )
        while (step(forward, backward) && step(backward, forward)) ;
        //create the result object
        return new DijkstraResult(steps, nbVisited, mu, merge(forward, backward)) {
            @Override
            void print() {
                System.out.println("From " + from + " to " + to + " : " + getDistance() + " " + getVerticesList() + " in " + getSteps() + " steps and " + getNbVisited() + " nodes visited");
            }
        };
    }

    /**
     * Context used to store the algorithm's variables.
     * Used for backward and forward contexts
     */
    private class DijkstraContext {
        private final int from;
        private final int to;
        private final double[] delta;
        private final int[] predecessors;

        private final boolean[] visited;
        private final Queue<V> queue;
        private int subTo;

        /**
         * Create a new DijkstraContext instance
         *
         * @param fromVertex - the source vertex id
         * @param toVertex   - the destination vertex id
         */
        DijkstraContext(int fromVertex, int toVertex) {
            from = fromVertex;
            to = toVertex;
            delta = new double[graph.getNVertices()];
            predecessors = new int[graph.getNVertices()];
            visited = new boolean[graph.getNVertices()];
            Arrays.fill(delta, Double.POSITIVE_INFINITY); // set all delta to infinity
            Arrays.fill(predecessors, -1); // set all predecessors to -1
            Arrays.fill(visited, true); // set all visited to true
            queue = new PriorityQueue<>(Comparator.comparingDouble(v -> delta[v.id()])); // create the queue
            delta[from] = 0; // set the delta of the source to 0
            queue.addAll(graph.getVertices()); // add all vertices to the queue
        }

        /**
         * Check if the vertex is in the queue
         *
         * @param vertex - the vertex id to check
         * @return true if the vertex is in the queue, false otherwise
         */
        public boolean isInQueue(int vertex) {
            return visited[vertex];
        }

        /**
         * Check if the queue is empty
         *
         * @return true if the queue is empty, false otherwise
         */
        public boolean isQueueEmpty() {
            return queue.isEmpty();
        }

        /**
         * Poll the queue
         *
         * @return the vertex which is the head of the queue
         */
        public CartesianVertex pollQueue() {
            V v = queue.poll();
            if (v != null) {
                // mark the vertex as is not in the queue anymore
                visited[v.id()] = false;
            }
            return v;
        }

        /**
         * Fix the queue after a vertex has been changed
         *
         * @param vertex - the vertex which has been changed
         */
        public void updateQueue(V vertex) {
            queue.remove(vertex);
            queue.add(vertex);
        }

        /**
         * Set the delta of a vertex
         *
         * @param vertex - the vertex id
         * @param value  - the new delta value
         */
        public void setDelta(int vertex, double value) {
            delta[vertex] = value;
        }

        /**
         * Get the delta value of a vertex
         *
         * @param vertex - the vertex id
         * @return the delta value
         */
        public double getDelta(int vertex) {
            return delta[vertex];
        }

        /**
         * Set the predecessor of a vertex
         *
         * @param vertex      - the vertex id
         * @param predecessor - the predecessor id
         */
        public void setPredecessor(int vertex, int predecessor) {
            predecessors[vertex] = predecessor;
        }

        /**
         * Get the predecessor of a vertex
         *
         * @param vertex - the vertex id
         * @return the predecessor of the vertex
         */
        public int getPredecessor(int vertex) {
            return predecessors[vertex];
        }

        /**
         * Set the sub-to node used to construct the path
         *
         * @return the sub-to vertex id
         */
        public int getSubTo() {
            return subTo;
        }

        /**
         * Set the sub-to node used to construct the path
         *
         * @param subTo - the sub-to vertex id
         */
        public void setSubTo(int subTo) {
            this.subTo = subTo;
        }
    }
}
