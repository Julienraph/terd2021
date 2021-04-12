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
        super(skin, pv);
        this.setLevelProps(1);
        this.setMainWeapon(new Arme(0, "épée", 10, 10, 10));
        this.setMainCompetence(new Competence(0, "eau", 10, 10, 10));
        this.getInventaire().ajoutItem(new Arme(0, "hache", 10, 10, 10));
        this.getInventaire().ajoutItem(new Competence(0, "feu", 10, 10, 10));
        this.getInventaire().ajoutItem(new Consommable(0, "cerise", 1, 10, 20));
        this.getInventaire().ajoutItem(new Consommable(0, "cerise", 1, 10, 20));
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


