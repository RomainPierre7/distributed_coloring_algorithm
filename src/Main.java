import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;
import io.jbotsim.gen.basic.TopologyGenerators;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;

// Main pour un anneau ou Main pour un arbre binaire
// Commenter le main que l'on ne veut pas exécuter

public class Main {
    public final static int n = 11;
    public static void main(String[] args) {

    Topology tp = new Topology();
    tp.setDefaultNodeModel(ColoringNode.class); // algo des noeuds = ColoringNode
    TopologyGenerators.generateRing(tp, n); // topologie = cycle à n noeuds
    tp.disableWireless(); // pour avoir un vrai anneau pour tout n
    tp.shuffleNodeIds(); // permutation des IDs dans [0,n[

    for (int i = 0; i<n; i++){ // pour chaque noeuds
        ColoringNode u = (ColoringNode) tp.getNodes().get(i); // u = noeuds i
        u.setLocation(u.getX()+250, u.getY()+100); // décaler sa position
        u.parent = tp.getNodes().get((i+1) % n); // son parent
    }

    JTopology jtp = new JTopology(tp);
    jtp.addLinkPainter(new JParentLinkPainter()); // ajoute l'orientation
    new JViewer(jtp); // dessine la topologie
    tp.start(); // démarre l'aglorithme
    tp.pause(); // mode pas-à-pas
    }
}

/* public class Main {
    public final static int n = 11;
    public static void main(String[] args) {

    Topology tp = new Topology();
    tp.setDefaultNodeModel(ColoringNode.class); // algo des noeuds = ColoringNode
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
    tp.disableWireless(); // pour avoir un vrai anneau pour tout n
    tp.shuffleNodeIds(); // permutation des IDs dans [0,n[

    for (int i = 0; i<n; i++){ // pour chaque noeuds
        ColoringNode u = (ColoringNode) tp.getNodes().get(i); // u = noeuds i
        u.parent = (i == 0) ? null : tp.getNodes().get((int) (i / 2));
    }

    JTopology jtp = new JTopology(tp);
    jtp.addLinkPainter(new JParentLinkPainter()); // ajoute l'orientation
    new JViewer(jtp); // dessine la topologie
    tp.start(); // démarre l'aglorithme
    tp.pause(); // mode pas-à-pas
    }
} */