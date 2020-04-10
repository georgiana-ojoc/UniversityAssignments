package game.clique;

import java.util.Objects;

public class Edge {
    private int source;
    private int target;

    public Edge(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Edge)) {
            return false;
        }
        Edge edge = (Edge) object;
        return getSource() == edge.getSource() &&
                getTarget() == edge.getTarget();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTarget());
    }

    @Override
    public String toString() {
        return "(" + source + ", " + target + ')';
    }
}
