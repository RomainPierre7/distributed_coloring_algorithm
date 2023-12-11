import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import static java.lang.Math.*;

import java.util.List;

public class ColoringNode extends Node {
    public Node parent; // défini dans Main()
    private boolean isRunning;
    private int x;
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
        isRunning = true;
        x = getID();
        l = log2ceil(Main.n);

        neighbors = getNeighbors();
        neighbors.remove(parent);

        for (Node n : neighbors){
            send(n, new Message(x));
        }
    }

    @Override
    public void onClock() {
        if (isRunning){
            // RECEIVE
            int y = x;
            for (Message m : getMailbox()){
                y = (int) m.getContent();
            }
            
            // COMPUTE
            if (x != y){
                x = posDiff(x, y);
                l2 = l;
                l = 1 + log2ceil(l);
                if (l == l2){
                    setColor(Color.getColorAt(x));
                    isRunning = false;
                    System.out.println("Node " + getID() + " is finished.");
                }
            }

            //SEND
            for (Node n : neighbors){
                send(n, new Message(x));
            }
        }
    }
}