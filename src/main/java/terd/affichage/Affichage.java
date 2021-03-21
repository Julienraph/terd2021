package terd.affichage;

import terd.Player.Player;


public class Affichage {
    private Player player;

    public Affichage(Player player) {
        this.player = player;
    }

    public void afficher(){
        boolean jouer;
        do {
            player.getEtageActuel().afficher(player.getPosEtageY(), player.getPosEtageX());
            jouer = player.deplacement();
        } while (jouer);
    }

    public static void main(String[] args) {
        Player player = new Player();
       // player.getEtageActuel().afficher(0,1);
        Affichage affichage = new Affichage(player);
        affichage.afficher();
    }
}
