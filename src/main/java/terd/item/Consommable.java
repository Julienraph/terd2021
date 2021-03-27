package terd.item;

public class Consommable extends Item{

    public Consommable(int prix, int nbrUtilisation, int rarete, int degat){
        super(prix, NomItem.CONSOMMABLE, nbrUtilisation, rarete, degat);
    }


}
