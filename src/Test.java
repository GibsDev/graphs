public class Test {
    public static void main(String[] args) {
        Vertex a = new Vertex("A");
        Vertex b = new Vertex("B");
        Vertex c = new Vertex("C");
        Vertex d = new Vertex("D");

        Graph domain = new Graph();

        Edge ab = domain.connect(a, b);
        Edge bc = domain.connect(b, c);
        Edge ad = domain.connect(a, d);

        System.out.println(domain);

        domain.remove(ad);

        System.out.println(domain);

        Graph path = new Graph();
        path.add(ab);
        System.out.println(path);

        Graph inverse = path.inverse(domain);
        System.out.println(inverse);
    }
}
