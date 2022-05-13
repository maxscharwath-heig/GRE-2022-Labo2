package schawath.maxime;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;

import java.util.*;

public class Dijkstra<V extends CartesianVertex, D extends Digraph<V, SimpleWeightedEdge<V>>> {
    private final D graph;
    private int step = 0;

    public Dijkstra(D graph) {
        this.graph = graph;
    }

    public DijkstraResult run(int from, int to) {
        double[] distance = new double[graph.getNVertices()];
        int[] p = new int[graph.getNVertices()];
        Arrays.fill(distance, Double.POSITIVE_INFINITY);
        Arrays.fill(p, -1);
        distance[from] = 0;
        var queueBoolean = new boolean[graph.getNVertices()];
        Queue<CartesianVertex> queue = new PriorityQueue<>(Comparator.comparingDouble(v -> distance[v.id()]));
        Arrays.fill(queueBoolean, true);
        queue.addAll(graph.getVertices());

        while (!queue.isEmpty()) {
            //get the vertex with the smallest distance
            var vi = queue.poll();
            queueBoolean[vi.id()] = false;
            if (distance[vi.id()] == Double.POSITIVE_INFINITY) {
                break;
            }
            //for each neighbor of u
            for (SimpleWeightedEdge<V> ej : graph.getSuccessorList(vi.id())) {
                ++step;
                var vj = ej.to();
                if (queueBoolean[vj.id()] && distance[vj.id()] > distance[vi.id()] + ej.weight()) {
                    distance[vj.id()] = distance[vi.id()] + ej.weight();
                    p[vj.id()] = vi.id();
                    queue.remove(vj);
                    queue.add(vj);
                }
            }
            if (vi.id() == to) break;
        }
        int cS = to;
        LinkedList<Integer> s = new LinkedList<>();
        while (cS != -1) {
            s.add(0, cS);
            cS = p[cS];
        }
        return new DijkstraResult(step, distance[to], s) {
            @Override
            void print() {
                System.out.println("From " + from + " to " + to + " : " + getWeight() + " " + getList() + " in " + getSteps() + " steps");
            }
        };
    }
}
