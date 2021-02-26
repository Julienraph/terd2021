package terd.Player;

import java.awt.event.*;
import java.util.Scanner;

public class Player extends Props {
    private int niveau;
    private Seed seedGame;
    private Etage etageActuel;
    private Inventaire stuff;
    private int posXetage;
    private int posYetage;
    private int speed;


    public Player(){
        // Initialisation à faire
        this.niveau = 0;
        this.seedGame  = null;
        this.etageActuel = null;
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
        if (boutonDeplacement.equals("z")) {
            addPosY(-speed);
        }
        else if (boutonDeplacement.equals("s")){
            addPosY(speed);
        }
        else if (boutonDeplacement.equals("q")){
            addPosX(-speed);
        }
        else if (boutonDeplacement.equals("d")){
            addPosX(speed);
        }



        }


}
