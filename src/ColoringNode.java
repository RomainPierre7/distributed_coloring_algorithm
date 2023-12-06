import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import static java.lang.Math.*;

import java.io.Console;
import java.util.List;

public class ColoringNode extends Node {
    public Node parent; // défini dans Main()
    private int x = getID();
    private int l;
    private int l2;
    private List<Node> neighbors;

    private static int posDiff(int x, int y){
        int p = 0;
        while (x % 2 == y % 2){
            x = x / 2;
            y = y / 2;
            p++;
        }
        return 2 * p + (x % 2);
    }

    private static int log2ceil(int k){
        double logN = Math.log(k) / Math.log(2);
        return (int) ceil(logN);
    }

    @Override
    public void onStart() {
        setColor(Color.getColorAt(getID())); // couleur = ID
        l = log2ceil(Main.n);

        neighbors = getNeighbors();
        neighbors.remove(parent);

        for (Node n : neighbors){
            send(n, new Message(getID()));
        }
    }

    @Override
    public void onClock() {
        int y = x;
        // RECEIVE
        for (Message m : getMailbox()){
            y = (int) m.getContent();
        }
        // COMPUTE
        x = posDiff(x, y);


        //SEND
        for (Node n : neighbors){
            send(n, new Message(getID()));
        }
    }
}