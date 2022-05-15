package schawath.maxime;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;

import java.util.*;

public class DijkstraBidirectional<V extends CartesianVertex, D extends Digraph<V, SimpleWeightedEdge<V>>> {
    private final D graph;

    private double mu;
    private int steps;
    private int nbVisited;

    public DijkstraBidirectional(D graph) {
        this.graph = graph;
    }

    private boolean step(DijkstraContext ctx, DijkstraContext ctx2) {
        //get the vertex with the smallest distance
        if (ctx.isQueueEmpty()) return false;
        var vi = ctx.pollQueue();
        nbVisited++;
        if (ctx.getDelta(vi.id()) == Double.POSITIVE_INFINITY) {
            return false;
        }
        //for each neighbor of u
        for (SimpleWeightedEdge<V> ej : graph.getSuccessorList(vi.id())) {
            ++steps;
            var vj = ej.to();
            if (ctx.isInQueue(vj.id()) && ctx.getDelta(vj.id()) > ctx.getDelta(vi.id()) + ej.weight()) {
                ctx.setDelta(vj.id(), ctx.getDelta(vi.id()) + ej.weight());
                ctx.setPredecessor(vj.id(), vi.id());
                ctx.updateQueue(vj);
            }
            //mu update
            double m;
            if (!ctx2.isInQueue(vj.id()) && mu > (m = ctx.getDelta(vi.id()) + ctx2.getDelta(vj.id()) + ej.weight())) {
                mu = m;
                ctx.subTo = vi.id();
            }
        }
        if (!ctx2.isInQueue(vi.id())) {
            ctx.subTo = vi.id();
            return false;
        }
        return true;
    }

    List<Integer> merge(DijkstraContext from, DijkstraContext to) {
        LinkedList<Integer> s = new LinkedList<>();
        int cS = from.subTo;
        while (cS != -1) {
            s.add(0, cS);
            cS = from.getPredecessor(cS);
        }
        cS = to.getPredecessor(to.subTo);
        while (cS != -1) {
            s.add(cS);
            cS = to.getPredecessor(cS);
        }
        return s;
    }

    public DijkstraResult run(int from, int to) {
        steps = 0;
        nbVisited = 0;
        mu = Double.POSITIVE_INFINITY;
        DijkstraContext forward = new DijkstraContext(from, to);
        DijkstraContext backward = new DijkstraContext(to, from);
        while (step(forward, backward) && step(backward, forward)) ;
        return new DijkstraResult(steps, nbVisited, mu, merge(forward, backward)) {
            @Override
            void print() {
                System.out.println("From " + from + " to " + to + " : " + getDistance() + " " + getList() + " in " + getSteps() + " steps and " + getNbVisited() + " nodes visited");
            }
        };
    }

    private class DijkstraContext {
        private final int from;
        private final int to;
        private final double[] delta;
        private final int[] predecessors;

        private final boolean[] visited;
        private final Queue<V> queue;
        public int subTo;

        DijkstraContext(int fromVertex, int toVertex) {
            from = fromVertex;
            to = toVertex;
            delta = new double[graph.getNVertices()];
            predecessors = new int[graph.getNVertices()];
            visited = new boolean[graph.getNVertices()];
            Arrays.fill(delta, Double.POSITIVE_INFINITY);
            Arrays.fill(predecessors, -1);
            Arrays.fill(visited, true);
            queue = new PriorityQueue<>(Comparator.comparingDouble(v -> delta[v.id()]));
            delta[from] = 0;
            queue.addAll(graph.getVertices());
        }

        public boolean isInQueue(int vertex) {
            return visited[vertex];
        }

        public boolean isQueueEmpty() {
            return queue.isEmpty();
        }

        public CartesianVertex pollQueue() {
            var v = queue.poll();
            assert v != null;
            visited[v.id()] = false;
            return v;
        }

        public void updateQueue(V vertex) {
            queue.remove(vertex);
            queue.add(vertex);
        }

        public void setDelta(int vertex, double value) {
            delta[vertex] = value;
        }

        public double getDelta(int vertex) {
            return delta[vertex];
        }

        public void setPredecessor(int vertex, int predecessor) {
            predecessors[vertex] = predecessor;
        }

        public int getPredecessor(int vertex) {
            return predecessors[vertex];
        }
    }
}
