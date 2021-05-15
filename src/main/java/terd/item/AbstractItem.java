package terd.item;

import terd.Player.Player;

public abstract class AbstractItem implements Item {
    int prix;
    String nom;
    int nbrUtilisation;
    int rarete;
    int degat;
    String messageInventaire;
    int amelioration;
    // Type type;

    public AbstractItem(int prix, String nom, int nbrUtilisation, int rarete, int degat){
        this.prix = prix;
        this.nom = nom;
        this.nbrUtilisation = nbrUtilisation;
        this.rarete = rarete;
        this.degat = degat;
        amelioration = degat/2;
    }

    public void addNbrUtilisation(int n){
        if (nbrUtilisation + n < 0 ) {
            nbrUtilisation = 0;
        }
        nbrUtilisation += n;
    }

    public boolean ameliorer(Player player) {
        if(player.getCredit() >= prix) {
            player.setCredit(player.getCredit() - prix);
            degat = degat + amelioration;
            amelioration = degat/2;
            prix += 1;
            return true;
        }
        return false;
    }

    public String getMessageInventaire() {
        return messageInventaire;
    }

    public void setMessageInventaire(String messageInventaire) {
        this.messageInventaire = messageInventaire;
    }

    public int utiliser(){
        addNbrUtilisation(-1);
        return degat;}

    public int getPrix() {
        return prix;
    }

    public int getAmelioration() {
        return amelioration;
    }

    public String getNom() {
        return nom;
    }

    public int getNbrUtilisation() {
        return nbrUtilisation;
    }

    public int getRarete(){
        return rarete;
    }

    public int getDegat(){
        return degat;
    }

    public void setDegat(int degat) {
        this.degat = degat;
    }
}
