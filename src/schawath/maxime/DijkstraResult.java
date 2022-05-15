package schawath.maxime;

import java.util.List;

public abstract class DijkstraResult {
    final int steps;
    final double distance;
    final List<Integer> list;
    private final int nbVisited;

    DijkstraResult(int steps, int nbVisited, double distance, List<Integer> list) {
        this.list = list;
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

    public double getDistance() {
        return distance;
    }

    public List<Integer> getList() {
        return list;
    }

    abstract void print();
}
