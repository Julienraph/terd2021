package terd.item;

public abstract class Item {
    int prix;
    NomItem nom;
    int nbrUtilisation;
    int rarete;
    int degat;
    // Type type;

    public Item(int prix, NomItem nom, int nbrUtilisation, int rarete, int degat){
        this.prix = prix;
        this.nom = nom;
        this.nbrUtilisation = nbrUtilisation;
        this.rarete = rarete;
        this.degat = degat;
    }

    public int getPrix() {
        return prix;
    }

    public NomItem getNom() {
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

    public void  setNbrUtilisation(int minusNbr){
        nbrUtilisation -= minusNbr;
    }

    public int utiliser(){return degat;}

    //private calculDegat(Type type){

}
