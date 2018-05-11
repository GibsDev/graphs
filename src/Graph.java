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
}
