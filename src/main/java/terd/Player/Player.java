package terd.Player;

import terd.Map.Coordonne;
import terd.etage.Etage;
import terd.utils.Seed;
import terd.item.Arme;
import terd.item.Inventaire;

import java.awt.event.*;
import java.util.Scanner;

public class Player extends Props {
    private int niveauPlayer;
    private Seed seedGame;
    private Etage etageActuel;
    private Inventaire inventaire;
    private Arme armeActuel;
    private int pv;
    private int speed;


    public Player(char skin){
        super(skin);
        this.niveauPlayer = 1 ;
        this.pv = 20;
        this.speed = 1;
        this.armeActuel = null;
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
    public void addPV(int pv){
        this.pv += pv;
    }
    public void removePV(int pv){
        if(pv > this.pv){ //si perte de PV supérieur aux pv du joueur pv = 0
            this.pv = 0;
        } else {
            this.pv -= pv;
        }
    }

    public void setArmeActuel(Arme arme){
        this.armeActuel = arme;
    }
}
