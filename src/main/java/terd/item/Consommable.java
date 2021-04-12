package terd.item;

import terd.Player.Player;

public class Consommable extends AbstractItem {

    public Consommable(int prix, String nom, int nbrUtilisation, int rarete, int degat){
        super(prix, nom, nbrUtilisation, rarete, degat);
    }


    @Override
    public Consommable useInventaire(Player player) {
        player.addPV(this.degat);
        this.setNbrUtilisation(1);
        return new Consommable(0, "cerise", 1, 10, 20);
    }

    @Override
    public void messageInventaire() {
        System.out.println("Quel consommable voulez-vous utiliser ?");
    }

    @Override
    public String description() {
        return(String.format("%s | PV : %d | nombre : %d",nom,degat,nbrUtilisation));
    }
}
