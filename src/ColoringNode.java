import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColoringNode extends Node {
    public Node parent; // défini dans Main()
    private boolean isRunning;
    private int step; // 1 = Color 6 | 2 = Color6to3
    private int state; // Utile pour Color6to3 | 1 = ShiftDown | 2 = ReducePalette
    private int k; // Utile pour Color6to3 | k appartient à {3, 4, 5}
    private int x;
    private int l;
    private int l2;
    private List<Node> neighbors;
    private List<Integer> neighbors_color = new ArrayList<>(); // Utile pour stocker les couleurs des voisins dans ReducePalette

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

    private static int firstFree(List<Integer> liste) {
        Set<Integer> ensemble = new HashSet<>(liste);
        int i = 0;
        while (ensemble.contains(i)) {
            i++;
        }
        return i;
    }

    @Override
    public void onStart() {
        setColor(Color.getColorAt(getID())); // couleur = ID
        isRunning = true;
        step = 1;
        state = 1;
        k = 3;
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
        int y;
        if (isRunning){
            if (step == 1){ // Color6
                // RECEIVE
                if (parent != null){
                    y = x;
                    for (Message m : getMailbox()){
                        y = (int) m.getContent();
                    }
                } else {
                    y = firstFree(List.of(x)); // FirstFree({x}) | Pas utile pour l'anneau mais sert à la généralité de l'algo (arbres)
                }
                
                // COMPUTE
                if (x != y){
                    x = posDiff(x, y);
                    l2 = l;
                    l = 1 + log2ceil(l);
                    if (l == l2){
                        setColor(Color.getColorAt(x));
                        System.out.println("Node " + getID() + "| Color6 is finished."); 
                        step++;                  
                    }
                }

                //SEND | Premier SEND du premier ShiftDown de Color6to3 quand step = 2
                for (Node n : neighbors){
                    send(n, new Message(x));
                }
            } else if (step == 2){ // Color6to3
                if (k != 6){
                    if (state == 1){ //ShiftDown | Pas utile pour l'anneau mais sert à la généralité de l'algo (arbres)
                        System.out.println("Node " + getID() + "| ShiftDown " + (k - 2) + "/3.");
                        //RECEIVE
                        if (parent != null){
                            y = x;
                            for (Message m : getMailbox()){
                                y = (int) m.getContent();
                            }
                        } else {
                            y = firstFree(List.of(x)); // FirstFree({x}) | Pas utile pour l'anneau mais sert à la généralité de l'algo (arbres)
                        }

                        //COMPUTE
                        setColor(Color.getColorAt(y));

                        //SEND | Premier SEND du ReducePalette suivant
                        x = Color.indexOf(getColor());
                        for (Node n : neighbors){
                            send(n, new Message(x));
                        }
                        send(parent, new Message(x));
                        state = 2;
                    } else if (state == 2){ // ReducePalette
                        System.out.println("Node " + getID() + "| ReducePalette " + (k - 2) + "/3.");
                        //RECEIVE
                        neighbors_color.clear();
                        for (Message m : getMailbox()){
                            neighbors_color.add((int) m.getContent());
                            }

                        //COMPUTE
                        if (x == k){ // Application de ReducePalette uniquement sur les noeuds de couleurs k
                            x = firstFree(neighbors_color);
                            setColor(Color.getColorAt(x));
                        }

                        //SEND | Premier SEND du ShiftDown suivant
                        for (Node n : neighbors){
                            send(n, new Message(x));
                        }

                        k++;
                        state = 1;
                    }
                } else if (k == 6){ // Extinction du processeur
                    isRunning = false;
                    System.out.println("Node " + getID() + " is finished.");
                }
            }
        }
    }
}