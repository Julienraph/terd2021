package terd.Player;

import terd.etage.Etage;
import terd.item.Competence;
import terd.item.Consommable;
import terd.utils.Seed;
import terd.item.Arme;
import terd.item.Inventaire;


public class Player extends AbstractProps {
    private Etage etageActuel;


    public Player(char skin, int pv){
        super("Player",skin, pv);
        this.setLevelProps(1);
        this.setXP(0);
        this.setMainWeapon(new Arme(0, "Epée", 10, 10, 10));
        this.setMainCompetence(new Competence(0, "Eau", 10, 10, 10));
        this.getInventaire().ajoutItem(new Arme(0, "Hache", 10, 10, 10));
        this.getInventaire().ajoutItem(new Competence(0, "Feu", 10, 10, 10));
        this.getInventaire().ajoutItem(new Consommable(0, "Cerise", 1, 10, 20));
        this.getInventaire().ajoutItem(new Consommable(0, "Cerise", 1, 10, 20));
    }

    public void interaction(){
        //TODO
    }

    public Etage getEtageActuel() {
        return etageActuel;
    }

    public void setEtageActuel(Etage etageActuel) {
        this.etageActuel = etageActuel;
    }
}


