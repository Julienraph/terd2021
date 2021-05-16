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
        this.setCredit(3);
        this.setMainWeapon(new Arme(1, "Epée", 10, 10, 30));
        this.setMainCompetence(new Competence(1, "Eau", 10, 10, 20));
        this.getInventaire().ajoutItem(new Arme(1, "Hache", 10, 10, 10));
        this.getInventaire().ajoutItem(new Arme(1, "Massue", 10, 10, 25));
        this.getInventaire().ajoutItem(new Competence(1, "Feu", 10, 10, 5));
        this.getInventaire().ajoutItem(new Competence(1, "Eclair", 10, 10, 10));
        this.getInventaire().ajoutItem(new Consommable(0, "Cerise", 1, 10, 20));
        this.getInventaire().ajoutItem(new Consommable(0, "Cerise", 1, 10, 20));
        this.getInventaire().ajoutItem(new Consommable(0, "Cerise", 1, 10, 20));
        this.getInventaire().ajoutItem(new Consommable(0, "Abricot", 1, 10, 20));
    }

    public Etage getEtageActuel() {
        return etageActuel;
    }

    public void setEtageActuel(Etage etageActuel) {
        this.etageActuel = etageActuel;
    }
}


