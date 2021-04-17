package terd.item;

import terd.Player.Player;

public class Consommable extends AbstractItem {

    public Consommable(int prix, String nom, int nbrUtilisation, int rarete, int degat){
        super(prix, nom, nbrUtilisation, rarete, degat);
    }


    @Override
    public Consommable useInventaire(Player player) {
        if (player.getPV() == player.getMaxPV()) {
            this.setMessageInventaire(String.format("Impossible d'utiliser %s : Vos PV sont déjà au maximum.", nom));
        } else {
            player.takeDamages(-degat);
            this.addNbrUtilisation(-1);
            this.setMessageInventaire(String.format("Vous avez utilisé %s : Augmente vos PV de %d.", nom,degat));
        }
        //Renvoie un consommable pour cause d'implémentation de code, on ne l'utilise pas
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
