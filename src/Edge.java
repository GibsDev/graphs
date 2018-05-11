public class Edge extends GraphElement {

    private Vertex a;
    private Vertex b;

    public Edge(Vertex a, Vertex b) {
        this.a = a;
        this.b = b;
        a.add(this);
        b.add(this);
    }

    public Vertex getA() {
        return a;
    }

    public void setA(Vertex a) {
        this.a = a;
    }

    public Vertex getB() {
        return b;
    }

    public void setB(Vertex b) {
        this.b = b;
    }

    public Vertex getOpposite(Vertex v) {
        if (a == v) {
            return b;
        } else if (b == v) {
            return a;
        }
        return null;
    }

    public boolean hasVertices(Vertex v1, Vertex v2) {
        return (v1 == a && v2 == b) || (v2 == a && v1 == b);
    }

    public String toString(Graph ctx){
        return a.toString(ctx) + "-(" + this.getValue(ctx) + ")-" + b.toString(ctx);
    }

    @Override
    public String toString() {
        return a + "-" + b;
    }
}
