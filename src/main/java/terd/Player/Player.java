package terd.Player;

import terd.utils.Seed;

import java.awt.event.*;
import java.util.Scanner;

public class Player extends Props {
    private int niveauPlayer;
    private Seed seedGame;
    private Etage etageActuel;
    private Inventaire stuff;
    private int posXetage;
    private int posYetage;
    private int speed;


    public Player(){
        // Initialisation à faire
        this.niveauPlayer = 1 ;
        this.seedGame  = new Seed();
        this.etageActuel = new Etage(0,0,1,1,seedGame;
        this.stuff = null;
        this.posXetage = 0;
        this.posYetage = 0;
        this.speed = 1;


    }

    public void interaction(){
        //TODO
    }

    public void deplacement(){
        System.out.println("Appuyez sur une touche");
        Scanner scanner = new Scanner(System.in);
        char boutonDeplacement = scanner.next().charAt(0);
        int nextX = getX();
        int nextY = getY();
        if (boutonDeplacement.equals("z")) {
            nextY -= speed;
        }
        else if (boutonDeplacement.equals("s")){
            nextY += speed;
        }
        else if (boutonDeplacement.equals("q")){
            nextX -= speed;
        }
        else if (boutonDeplacement.equals("d")){
            nextX += speed;
        }

        if(etageActuel.movePropsOnMap(posXetage,posYetage,getX(),getY(),nextX,nextY)){
            setPosX(nextX);
            setPosY(nextY);

        }



        }


}
