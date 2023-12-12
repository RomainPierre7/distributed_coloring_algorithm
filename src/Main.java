import io.jbotsim.core.Topology;
import io.jbotsim.gen.basic.TopologyGenerators;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;

public class Main {
    public final static int n = 25;
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