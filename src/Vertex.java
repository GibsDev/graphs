import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Vertex extends GraphElement {

    // The name for this vertex
    private String label;

    // All of the edges connected to this vertex
    private HashSet<Edge> edges = new HashSet<>();

    // All of the edges connected to this vertex within particular Graph states
    private HashMap<Graph, HashSet<Edge>> contextEdges = new HashMap<>();

    public Vertex(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Iterator<Edge> getAllEdges() {
        return edges.iterator();
    }

    public Iterator<Edge> getEdges(Graph ctx) {
        if (contextEdges.get(ctx) == null) {
            return null;
        }
        return contextEdges.get(ctx).iterator();
    }

    public void add(Edge e) {
        edges.add(e);
    }

    public void remove(Edge e) {
        edges.remove(e);
    }

    public void add(Graph ctx, Edge e) {
        if (!contextEdges.containsKey(ctx)) {
            contextEdges.put(ctx, new HashSet<>());
        }
        if (e != null) {
            contextEdges.get(ctx).add(e);
        }
    }

    public void remove(Graph ctx, Edge e) {
        if (contextEdges.containsKey(ctx)) {
            contextEdges.get(ctx).remove(e);
        }
    }

    public void remove(Graph ctx) {
        contextEdges.remove(ctx);
        super.remove(ctx);
    }

    public Iterator<Vertex> getAllNeightbors() {
        return getNeighbors(null);
    }

    public Iterator<Vertex> getNeighbors(Graph ctx) {
        final Vertex self = this;
        final Iterator<Edge> ei;
        if (ctx == null) {
            ei = this.getAllEdges();
        } else {
            ei = this.getEdges(ctx);
        }
        return new Iterator<Vertex>() {
            @Override
            public boolean hasNext() {
                return ei.hasNext();
            }

            @Override
            public Vertex next() {
                return ei.next().getOpposite(self);
            }
        };
    }

    public boolean hasEdgeTo(Vertex other) {
        return hasEdgeTo(null, other);
    }

    public boolean hasEdgeTo(Graph ctx, Vertex other) {
        Iterator<Vertex> vi;
        if (ctx == null) {
            vi = this.getAllNeightbors();
        } else {
            vi = this.getNeighbors(ctx);
        }
        while (vi.hasNext()) {
            Vertex v = vi.next();
            if (v == other) {
                return true;
            }
        }
        return false;
    }

    public String toString(Graph ctx) {
        return this.label + "(" + this.getValue(ctx) + ")";
    }

    @Override
    public String toString() {
        return this.label + "(" + this.getDefaultValue() + ")";
    }
}
