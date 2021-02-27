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
            player.getEtageActuel().afficher(player.getPosXetage(), player.getPosYetage());
            jouer = player.deplacement();
        } while (jouer);
    }

    public static void main(String[] args) {
        Player player = new Player();
        Affichage affichage = new Affichage(player);
        affichage.afficher();
    }
}
