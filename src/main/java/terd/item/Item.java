package terd.item;

import terd.Player.Player;

public interface Item {

    int getPrix();
    String getNom();
    int getNbrUtilisation();
    int getRarete();
    int getDegat();
    void addNbrUtilisation(int minusNbr);
    int utiliser();
    Item useInventaire(Player player);
    int getAmelioration();
    void messageInventaire();
    default String description() {
        return(String.format("%s | dégat : %d | crédit : %d",this.getNom(),this.getDegat(), this.getPrix()));
    }
    String getMessageInventaire();
    void setDegat(int degat);
    boolean ameliorer(Player player);
}
