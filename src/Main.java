import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;
import io.jbotsim.gen.basic.TopologyGenerators;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;

// Main for a ring or Main for a binary tree
// Comment out the main that you do not want to execute

public class Main {
    public final static int n = 11;
    public static void main(String[] args) {

    Topology tp = new Topology();
    tp.setDefaultNodeModel(ColoringNode.class); // node algorithm = ColoringNode
    TopologyGenerators.generateRing(tp, n); // topology = cycle with n nodes
    tp.disableWireless(); // to have a true ring for any n
    tp.shuffleNodeIds(); // permutation of IDs in [0,n[

    for (int i = 0; i<n; i++){ // for each node
        ColoringNode u = (ColoringNode) tp.getNodes().get(i); // u = node i
        u.setLocation(u.getX()+250, u.getY()+100); // shift its position
        u.parent = tp.getNodes().get((i+1) % n); // its parent
    }

    JTopology jtp = new JTopology(tp);
    jtp.addLinkPainter(new JParentLinkPainter()); // add orientation
    new JViewer(jtp); // draw the topology
    tp.start(); // start the algorithm
    tp.pause(); // step-by-step mode
    }
}

/* public class Main {
    public final static int n = 11;
    public static void main(String[] args) {

    Topology tp = new Topology();
    tp.setDefaultNodeModel(ColoringNode.class); // node algorithm = ColoringNode
    for (int i = 0; i < n; i++){
        int h = (int) (Math.log(i+1) / Math.log(2));
        int j = i - (int) Math.pow(2, h);
        int y = 100 + 60 * h;
        int x = 300 + 60 * j;
        tp.addNode(x, y, new ColoringNode());
    }
    for (int i = 1; i < n; i++){
        ColoringNode u = (ColoringNode) tp.getNodes().get(i);
        ColoringNode v = (ColoringNode) tp.getNodes().get((int) (i/2));
        Link l = new Link(u, v);
        tp.addLink(l);
    }
    tp.disableWireless(); // to have a true ring for any n
    tp.shuffleNodeIds(); // permutation of IDs in [0,n[

    for (int i = 0; i<n; i++){ // for each node
        ColoringNode u = (ColoringNode) tp.getNodes().get(i); // u = node i
        u.parent = (i == 0) ? null : tp.getNodes().get((int) (i / 2));
    }

    JTopology jtp = new JTopology(tp);
    jtp.addLinkPainter(new JParentLinkPainter()); // add orientation
    new JViewer(jtp); // draw the topology
    tp.start(); // start the algorithm
    tp.pause(); // step-by-step mode
    }
} */