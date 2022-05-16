package schawath.maxime;

import graph.core.impl.Digraph;
import graph.core.impl.SimpleWeightedEdge;

import java.util.*;

/**
 * Dijkstra algorithm simple version
 *
 * @param <V> Vertex type
 * @param <D> Digraph type
 */
public class Dijkstra<V extends CartesianVertex, D extends Digraph<V, SimpleWeightedEdge<V>>> {
    private final D graph;
    private int steps;
    private int nbVisited;

    public Dijkstra(D graph) {
        this.graph = graph;
    }

    /**
     * Run the algorithm
     *
     * @param from - the source vertex id
     * @param to   - the destination vertex id
     * @return the result of the algorithm
     */
    public DijkstraResult run(int from, int to) {
        // Initialize all variables to their default values
        nbVisited = 0;
        steps = 0;
        double[] distance = new double[graph.getNVertices()];
        int[] p = new int[graph.getNVertices()];
        Arrays.fill(distance, Double.POSITIVE_INFINITY);
        Arrays.fill(p, -1);
        distance[from] = 0;
        boolean[] queueBoolean = new boolean[graph.getNVertices()];
        Queue<CartesianVertex> queue = new PriorityQueue<>(Comparator.comparingDouble(v -> distance[v.id()]));
        Arrays.fill(queueBoolean, true);
        queue.addAll(graph.getVertices());

        while (!queue.isEmpty()) {
            //get the vertex with the smallest distance
            CartesianVertex vi = queue.poll();
            nbVisited++; //increment the number of visited nodes (for statistics)
            queueBoolean[vi.id()] = false;
            if (distance[vi.id()] == Double.POSITIVE_INFINITY) {
                break;
            }
            //for each neighbor of u
            for (SimpleWeightedEdge<V> ej : graph.getSuccessorList(vi.id())) {
                ++steps; //increment the number of steps (for statistics)
                V vj = ej.to();
                if (queueBoolean[vj.id()] && distance[vj.id()] > distance[vi.id()] + ej.weight()) {
                    distance[vj.id()] = distance[vi.id()] + ej.weight();
                    p[vj.id()] = vi.id();
                    queue.remove(vj); // trick to update the queue
                    queue.add(vj);
                }
            }
            if (vi.id() == to) break;
        }
        // create the path
        int cS = to;
        LinkedList<Integer> s = new LinkedList<>();
        while (cS != -1) { // while we are not at the beginning
            s.add(0, cS); // add the current vertex to the path
            cS = p[cS]; // go to the previous vertex
        }
        // return the result as a DijkstraResult object
        return new DijkstraResult(steps, nbVisited, distance[to], s) {
            @Override
            void print() {
                System.out.println("From " + from + " to " + to + " : " + getDistance() + " " + getVerticesList() + " in " + getSteps() + " steps and " + getNbVisited() + " nodes visited");
            }
        };
    }
}
