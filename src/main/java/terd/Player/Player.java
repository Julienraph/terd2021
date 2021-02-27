package terd.Player;

import terd.etage.Etage;
import terd.utils.Seed;

import java.awt.event.*;
import java.util.Scanner;

public class Player extends Props {
    private int niveauPlayer;
    private Seed seedGame;
    private Etage etageActuel;
    //private Inventaire stuff;
    private int speed;


    public Player(){
        super(0,0,0,0,'@');
        this.niveauPlayer = 1 ;
        this.seedGame  = new Seed();
        this.etageActuel = new Etage(5,5, 8,10, 1,1, seedGame);
   //     this.stuff = null;
        this.speed = 1;
    }

    public void interaction(){
        //TODO
    }

    public Etage getEtageActuel() {
        return etageActuel;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean deplacement(){
        Scanner scanner = new Scanner(System.in);
        boolean jouer = true;
        char boutonDeplacement = scanner.next().charAt(0);
        int nextX = this.getX();
        int nextY = this.getY();
        if (boutonDeplacement == 'q' || boutonDeplacement == 'Q') {
            nextY = nextY - this.getSpeed();
        } else if (boutonDeplacement == ('d') || boutonDeplacement == ('D')) {
            nextY = nextY + this.getSpeed();
        } else if (boutonDeplacement == ('z') || boutonDeplacement == ('Z')) {
            nextX = nextX - this.getSpeed();
        } else if (boutonDeplacement == ('s') || boutonDeplacement == ('S')) {
            nextX = nextX + this.getSpeed();
        } else if (boutonDeplacement == ('x') || boutonDeplacement == ('X')) {
            jouer = false;
        }
        this.getEtageActuel().moveProps(this, nextX, nextY, this.getSkin());
        return jouer;

    }


}
