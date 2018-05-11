public class Test {
    public static void main(String[] args) {
        Vertex a = new Vertex("A");
        a.setDefaultValue(10);
        Vertex b = new Vertex("B");
        Vertex c = new Vertex("C");
        Vertex d = new Vertex("D");

        Graph domain = new Graph();

        Edge ab = domain.connect(a, b);
        ab.setDefaultValue(5);
        Edge bc = domain.connect(b, c);
        bc.setDefaultValue(10);
        Edge ad = domain.connect(a, d);
        ad.setDefaultValue(15);

        System.out.println(domain);

        domain.remove(b);

        System.out.println(domain);
    }
}
