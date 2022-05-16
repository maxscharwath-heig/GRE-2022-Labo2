package schawath.maxime;

import java.util.Collections;
import java.util.List;

/**
 * DijkstraResult is an abstract class that contains the result of the Dijkstra algorithm.
 * it gives to the user some useful information about the result of the algorithm.
 *
 * @author Maxime Scharwath
 */
public abstract class DijkstraResult {
    final int steps;
    final double distance;
    final List<Integer> vertsList;
    private final int nbVisited;

    DijkstraResult(int steps, int nbVisited, double distance, List<Integer> list) {
        this.vertsList = list;
        this.distance = distance;
        this.steps = steps;
        this.nbVisited = nbVisited;
    }

    public int getSteps() {
        return steps;
    }

    public int getNbVisited() {
        return nbVisited;
    }

    /**
     * @return the distance between the source and the destination
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return the list of the vertices in the shortest path
     */
    public List<Integer> getVerticesList() {
        return Collections.unmodifiableList(vertsList);
    }

    /**
     * print the result of the algorithm in the console
     */
    abstract void print();
}
