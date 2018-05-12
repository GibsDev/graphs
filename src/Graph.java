import java.util.HashSet;
import java.util.Iterator;

/**
 * Responsible for managing the context within the given Vertex/Edges
 */
public class Graph {

    // The edges in this graph
    private HashSet<Edge> edges = new HashSet<>();
    // Only used as a cache to generate a vertex iterator
    private HashSet<Vertex> vertices = new HashSet<>();

    public void add(Edge e) {
        edges.add(e);
        vertices.add(e.getA());
        vertices.add(e.getB());
        e.getA().add(this, e);
        e.getB().add(this, e);
    }

    public void add(Vertex v) {
        vertices.add(v);
        v.add(this, null);
    }

    public void remove(Vertex v) {
        vertices.remove(v);
        for (Iterator<Edge> edgeIterator = v.getEdges(this); edgeIterator.hasNext(); ) {
            Edge edge = edgeIterator.next();
            edges.remove(edge);
            Vertex other = edge.getOpposite(v);
            other.remove(this, edge);
        }
        v.remove(this);
    }

    public void remove(Edge e) {
        edges.remove(e);
        e.getA().remove(this, e);
        e.getB().remove(this, e);
    }

    public Iterator<Edge> getEdges() {
        return edges.iterator();
    }

    public Iterator<Vertex> getVertices() {
        return vertices.iterator();
    }

    public Edge getEdgeBetween(Vertex a, Vertex b) {
        for (Iterator<Edge> edgeIterator = edges.iterator(); edgeIterator.hasNext(); ) {
            Edge edge = edgeIterator.next();
            if (edge.hasVertices(a, b)) {
                return edge;
            }
        }
        return null;
    }

    public Vertex getVertexByName(String name) {
        for (Iterator<Vertex> vi = vertices.iterator(); vi.hasNext(); ) {
            Vertex next = vi.next();
            if (next.getLabel().equals(name)) {
                return next;
            }
        }
        return null;
    }

    public Edge connect(Vertex a, Vertex b) {
        Edge edge = this.getEdgeBetween(a, b);
        if (edge == null) {
            edge = new Edge(a, b);
        }
        this.add(edge);
        return edge;
    }

    public Edge connect(Vertex a, Vertex b, int value) {
        Edge e = this.connect(a, b);
        e.setValue(this, value);
        return e;
    }

    /**
     * Search through the points in this graph (assumes that this graph is the search domain)
     *
     * @param start
     * @param end
     * @return
     */
    public Graph depthFirstSearch(Vertex start, Vertex end) {
        Graph unsearched = this.clone();
        Graph search = new Graph();
        boolean found = dfs(unsearched, search, start, end);
        if(found){
            return search;
        }
        return null;
    }

    public boolean dfs(Graph unsearched, Graph search, Vertex start, Vertex end) {
        boolean found = false;
        for (Iterator<Edge> edgeIterator = start.getEdges(unsearched); edgeIterator != null && edgeIterator.hasNext(); ) {
            Edge e = edgeIterator.next();
            Vertex v = e.getOpposite(start);
            search.add(e);
            unsearched.remove(start);
            if (v == end) {
                // found
                return true;
            } else {
                found = dfs(unsearched, search, v, end);
                if(!found){
                    search.remove(v);
                } else {
                    return true;
                }
            }
        }
        return found;
    }

    /**
     * @param domain the graph that contains the domain of edges to subtract from
     * @return a clone of the domain graph with the edges and vertices from this graph context removed
     */
    public Graph inverse(Graph domain) {
        Graph inverse = domain.clone();
        for (Edge e : edges) {
            inverse.edges.remove(e);
        }
        for (Vertex v : vertices) {
            inverse.vertices.remove(v);
        }
        return inverse;
    }

    /**
     * @param other the graph to combine with this one
     * @return a new graph that contains the edges and vertices from this and the other graph
     */
    public Graph union(Graph other) {
        Graph union = this.clone();
        for (Edge e : other.edges) {
            union.edges.add(e);
        }
        for (Vertex v : other.vertices) {
            union.vertices.add(v);
        }
        return union;
    }

    @Override
    protected Graph clone() {
        Graph clone = new Graph();
        for (Edge e : edges) {
            clone.edges.add(e);
        }
        for (Vertex v : vertices) {
            clone.vertices.add(v);
            // transfer over edge tracking to clone
            v.getEdges(this).forEachRemaining(e -> {
                v.add(clone, e);
            });
        }
        return clone;
    }

    @Override
    public String toString() {
        String out = "Graph {\n";
        for (Iterator<Edge> edgeIterator = edges.iterator(); edgeIterator.hasNext(); ) {
            Edge edge = edgeIterator.next();
            out += "\t" + edge.toString(this);
            if (edgeIterator.hasNext()) {
                out += ",";
            }
            out += "\n";
        }
        out += "}";
        return out;
    }

    @Override
    protected void finalize() throws Throwable {
        // Remove tracking of this graph
        for (Edge edge : edges) {
            edge.remove(this);
        }
        for (Vertex vertex : vertices) {
            vertex.remove(this);
        }
        super.finalize();
    }
}
