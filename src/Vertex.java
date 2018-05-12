import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Vertex extends GraphElement {

    // The name for this vertex
    private String label;

    // All of the edges connected to this vertex
    private HashSet<Edge> edges = new HashSet<>();

    // All of the edges connected to this vertex within particular Graph contexts
    // The graph class is responsible for managing these contexts
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

    /**
     * @return every edge that is connected to this vertex from any graph context
     */
    public Iterator<Edge> getAllEdges() {
        return edges.iterator();
    }

    /**
     * @param ctx the graph context you want the edges from
     * @return the edges connected to this vertex in the given graph context
     */
    public Iterator<Edge> getEdges(Graph ctx) {
        if (contextEdges.get(ctx) == null) {
            return null;
        }
        return contextEdges.get(ctx).iterator();
    }

    /**
     * Add an edge to the general list of edges (cross graph domain)
     * @param e
     */
    public void add(Edge e) {
        edges.add(e);
    }

    /**
     * Remove an edge from the cross domain list of edges
     * @param e
     */
    public void remove(Edge e) {
        edges.remove(e);
    }

    /**
     * Add an edge to a specific graph instance
     * @param ctx the graph context to add to
     * @param e the edge to add
     */
    public void add(Graph ctx, Edge e) {
        if (!contextEdges.containsKey(ctx)) {
            contextEdges.put(ctx, new HashSet<>());
        }
        if (e != null) {
            contextEdges.get(ctx).add(e);
        }
        // Make sure to add to the global edges
        this.add(e);
    }

    /**
     * Remove an edge from a given graph context
     * @param ctx the graph context to remove the edge from
     * @param e the edge to remove
     */
    public void remove(Graph ctx, Edge e) {
        if (contextEdges.containsKey(ctx)) {
            contextEdges.get(ctx).remove(e);
        }
    }

    /**
     * Remove all of the stateful information for a given graph.
     * This disconnects stateful information stored by a graph
     * @param ctx
     */
    public void remove(Graph ctx) {
        contextEdges.remove(ctx);
        super.remove(ctx);
    }

    /**
     * @return all of the neighboring vertices from any graph
     */
    public Iterator<Vertex> getAllNeightbors() {
        return getNeighbors(null);
    }

    /**
     * @param ctx the graph context to find neighbors
     * @return all of the neighboring vertices within the given graph context
     */
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

    /**
     * @param other
     * @return if an edge exists across any graph domain
     */
    public boolean hasEdgeTo(Vertex other) {
        return hasEdgeTo(null, other);
    }

    /**
     *
     * @param ctx the context to be checked. If null it will check all contexts
     * @param other
     * @return if an edge ecists within the given context
     */
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
