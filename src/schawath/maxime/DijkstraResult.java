package schawath.maxime;

import java.util.List;

public abstract class DijkstraResult {
    final int steps;
    final double weight;
    final List<Integer> list;

    DijkstraResult(int steps, double weight, List<Integer> list) {
        this.list = list;
        this.weight = weight;
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }

    public double getWeight() {
        return weight;
    }

    public List<Integer> getList() {
        return list;
    }

    abstract void print();
}
