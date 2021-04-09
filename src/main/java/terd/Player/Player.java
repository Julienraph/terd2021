package terd.Player;

import terd.Map.Coordonne;
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


    public Player(char skin){
        super(skin);
        this.niveauPlayer = 1 ;
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

    public void setEtageActuel(Etage etageActuel) {
        this.etageActuel = etageActuel;
    }
}
