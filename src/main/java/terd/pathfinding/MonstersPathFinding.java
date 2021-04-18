package terd.pathfinding;

import terd.Map.Pos;
import terd.Player.Monster;
import terd.Player.Player;

import java.util.*;

public class MonstersPathFinding {

    public static int REACH = 5;

    private Player player;
    private Monster monster;
    private terd.Map.Map map;

    public MonstersPathFinding(Player player, Monster monster, terd.Map.Map map) {
        this.player = player;
        this.monster = monster;
        this.map = map;
    }

    /**
     * Test if the monster is in reach of the pos
     *
     * @return boolean
     */
    public boolean isWithinReach(Pos target) {
        return ((target.getX() > monster.getX() - REACH && target.getX() < monster.getX() + REACH) &&
                (target.getY() > monster.getY() - REACH && target.getY() < monster.getY() + REACH));
    }

    /**
     * Get a list of location, the monster has to go through to reach the player
     *
     * @return List<Location>
     */
    public List<Pos> getPathToPlayer() {
        Pos toGo = null;

        int MinX = monster.getX() - REACH;
        int MaxX = monster.getX() + REACH;
        int MinY = monster.getY() - REACH;
        int MaxY = monster.getY() + REACH;

        if (isWithinReach(player.getPos())) {
            toGo = player.getPos();
        } else {

            toGo = new Pos(
                    MinX + (int) (Math.random() * ((MaxX - MinX) + 1)),
                    MinY + (int) (Math.random() * ((MaxY - MinY) + 1))
            );
        }

        Node start = new Node(1);
        start.pos = monster.getPos();
        start.g = 0;

        Node target = new Node(1);
        target.pos = toGo;

        start = createMesh(start, target);

        Node res = aStar(start, target);
        List<Pos> positions = new ArrayList<>();

        if (res == null) {
            return positions;
        }

        while (res.parent != null) {
            positions.add(res.pos);
            res = res.parent;
        }

        positions.add(res.pos);
        Collections.reverse(positions);

        return positions;
    }


    // https://www.stackabuse.com/graphs-in-java-a-star-algorithm/
    private Node createMesh(Node start, Node target) {
        HashMap<Pos, Node> nodes = new HashMap<>();
        for (int x = (start.pos.getX() - REACH); x < (start.pos.getX() + REACH); x++) {
            for (int y = (start.pos.getY() - REACH); y < (start.pos.getY() + REACH); y++) {
                if (!map.isValide(y, x)) {
                    continue;
                }
                
                if (target.pos.getX() == x && target.pos.getY() == y) {
                    nodes.put(target.pos, target);
                } else {
                    Node node = new Node(2);
                    node.pos = new Pos(x, y);
                    nodes.put(node.pos, node);
                }
            }
        }

        start = link(start, nodes);

        return start;

    }

    public Node link(Node middle, HashMap<Pos, Node> nodes) {
        Node n1 = nodes.get(new Pos(middle.pos.getX() + 1, middle.pos.getY()));
        if (n1 != null) {
            middle.addBranch(1, n1);
            link(n1, nodes);
        }

        Node n2 = nodes.get(new Pos(middle.pos.getX() - 1, middle.pos.getY()));
        if (n2 != null) {
            middle.addBranch(1, n2);
            link(n2, nodes);
        }

        Node n3 = nodes.get(new Pos(middle.pos.getX(), middle.pos.getY() + 1));
        if (n3 != null) {
            middle.addBranch(1, n3);
            link(n3, nodes);
        }

        Node n4 = nodes.get(new Pos(middle.pos.getX(), middle.pos.getY() - 1));
        if (n4 != null) {
            middle.addBranch(1, n4);
            link(n4, nodes);
        }

        return middle;
    }

    private Node aStar(Node start, Node target) {
        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();

        start.f = start.g + start.calculateHeuristic(target);
        openList.add(start);

        while (!openList.isEmpty()) {
            Node n = openList.peek();
            if (n == target) {
                return n;
            }

            for (Node.Edge edge : n.neighbors) {
                Node m = edge.node;
                double totalWeight = n.g + edge.weight;

                if (!openList.contains(m) && !closedList.contains(m)) {
                    m.parent = n;
                    m.g = totalWeight;
                    m.f = m.g + m.calculateHeuristic(target);
                    openList.add(m);
                } else {
                    if (totalWeight < m.g) {
                        m.parent = n;
                        m.g = totalWeight;
                        m.f = m.g + m.calculateHeuristic(target);

                        if (closedList.contains(m)) {
                            closedList.remove(m);
                            openList.add(m);
                        }
                    }
                }
            }

            openList.remove(n);
            closedList.add(n);
        }
        return null;
    }
}
