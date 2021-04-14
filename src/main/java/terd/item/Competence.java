package terd.item;

import terd.Player.Player;

public class Competence extends AbstractItem {

    public Competence(int prix, String nom, int nbrUtilisation, int rarete, int degat){
        super(prix, nom, nbrUtilisation, rarete, degat);

    }

    @Override
    public Competence useInventaire(Player player) {
        Competence oldCompetence = player.getMainCompetence();
        player.setMainCompetence(this);
        this.setMessageInventaire(String.format("Vous avez remplacé la compétence %s par %s.", oldCompetence.getNom(),nom));
        return oldCompetence;
    }

    @Override
    public void messageInventaire() {
        System.out.println("Choisissez votre compétence");
    }
}
