package terd.item;

import terd.Player.Player;

public class Competence extends AbstractItem {

    public Competence(int prix, String nom, int nbrUtilisation, int rarete, int degat){
        super(prix, nom, nbrUtilisation, rarete, degat);

    }

    @Override
    public Competence useInventaire(Player player) {
        Competence competence = player.getCompetenceActuel();
        player.setCompetenceActuel(this);
        return competence;
    }

    @Override
    public void messageInventaire() {
        System.out.println("Choisissez votre compétence");
    }
}
