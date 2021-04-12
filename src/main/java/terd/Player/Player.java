package terd.Player;

import terd.etage.Etage;
import terd.item.Competence;
import terd.item.Consommable;
import terd.utils.Seed;
import terd.item.Arme;
import terd.item.Inventaire;


public class Player extends AbstractProps {
    private Seed seedGame;
    private Etage etageActuel;
    private Inventaire inventaire;
    private Competence competenceActuel;


    public Player(char skin, int pv){
        super(skin, pv);
        this.setLevelProps(1);
        this.setMainWeapon(new Arme(0, "épée", 10, 10, 10));
        this.competenceActuel = new Competence(0, "eau", 10, 10, 10);
        this.inventaire = new Inventaire();
        Arme arme = new Arme(0, "hache", 10, 10, 10);
        Competence competence = new Competence(0, "feu", 10, 10, 10);
        Consommable consommable = new Consommable(0, "cerise", 1, 10, 20);
        inventaire.ajoutItem(arme);
        inventaire.ajoutItem(competence);
        inventaire.ajoutItem(consommable);
        inventaire.ajoutItem(consommable);
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

    public Inventaire getInventaire() {
        return inventaire;
    }

    public void setCompetenceActuel(Competence competence) {
        this.competenceActuel = competence;
    }

    public Competence getCompetenceActuel() {
        return competenceActuel;
    }
}


