package terd.item;

import terd.Player.Player;

public class Arme extends AbstractItem {

    public Arme (int prix, String nom, int nbrUtilisation, int rarete, int degat) {
        super(prix, nom, nbrUtilisation, rarete, degat);
    }

    @Override
    public Arme useInventaire(Player player) {
        Arme oldArme = player.getMainWeapon();
        player.setMainWeapon(this);
        this.setMessageInventaire(String.format("Vous avez remplacé l'arme %s par %s.", oldArme.getNom(),nom));
        return oldArme;
    }

    @Override
    public void messageInventaire() {
        System.out.println("Choisissez l'arme à équiper: ");
    }

}
