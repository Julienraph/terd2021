package terd.pathfinding;

import terd.Map.Pos;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    private static int idCounter = 0;
    public int id;

    public Node parent = null;

    public List<Edge> neighbors;

    public double f = Double.MIN_VALUE;
    public double g = Double.MAX_VALUE;

    public double h;

    public Pos pos;

    Node(double h) {
        this.h = h;
        this.id = idCounter++;
        this.neighbors = new ArrayList<Edge>();
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(f, other.f);
    }

    public void addBranch(int weight, Node node) {
        Edge edge = new Edge(weight, node);
        neighbors.add(edge);
    }

    public double calculateHeuristic(Node target) {
        return h;
    }

    public static class Edge {
        public int weight;
        public Node node;

        public Edge(int weight, Node node) {
            this.weight = weight;
            this.node = node;
        }
    }
}
