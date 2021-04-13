package terd.item;

import terd.Player.Player;

public interface Item {

    int getPrix();
    String getNom();
    int getNbrUtilisation();
    int getRarete();
    int getDegat();
    void  setNbrUtilisation(int minusNbr);
    int utiliser();
    Item useInventaire(Player player);
    void messageInventaire();
    default String description() {
        return(String.format("%s | dégat : %d",this.getNom(),this.getDegat()));
    }
    String getMessageInventaire();
}
