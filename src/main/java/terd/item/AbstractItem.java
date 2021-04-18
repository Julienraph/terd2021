package terd.item;

public abstract class AbstractItem implements Item {
    int prix;
    String nom;
    int nbrUtilisation;
    int rarete;
    int degat;
    String messageInventaire;
    // Type type;

    public AbstractItem(int prix, String nom, int nbrUtilisation, int rarete, int degat){
        this.prix = prix;
        this.nom = nom;
        this.nbrUtilisation = nbrUtilisation;
        this.rarete = rarete;
        this.degat = degat;
    }

    public void  setNbrUtilisation(int minusNbr){
        if (minusNbr > nbrUtilisation) {
            nbrUtilisation = 0;
        }
        nbrUtilisation -= minusNbr;
    }

    public String getMessageInventaire() {
        return messageInventaire;
    }

    public void setMessageInventaire(String messageInventaire) {
        this.messageInventaire = messageInventaire;
    }

    public int utiliser(){
        setNbrUtilisation(1);
        return degat;}

    public int getPrix() {
        return prix;
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

}
