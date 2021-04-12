package terd.item;

import terd.Player.Player;
import java.util.ArrayList;
import java.util.List;

public  class Inventaire {
    private List<Item> armes;
    private List<Item> competences;
    private List<Item> consommables;
    private List<Item> items;
    private int etat = -1;

    public Inventaire() {
        armes = new ArrayList<>();
        competences = new ArrayList<>();
        consommables = new ArrayList<>();
    }

    public void ajoutItem(Item item) {
        if (item instanceof Arme) {
            armes.add((Arme) item);
        } else if (item instanceof Competence) {
            competences.add((Competence) item);
        } else if (item instanceof Consommable) {
            for(Item consommable : consommables) {
                if (consommable.getNom().equals(item.getNom())) {
                    consommable.setNbrUtilisation(-1);
                    return;
                }
            }
            consommables.add((Consommable) item);
        }
    }

    public boolean quitter() {
        etat = -1;
        System.out.println("Fermeture de l'inventaire");
        return false;
    }

    public boolean retour() {
        if(etat == -1) {
            return quitter();
        } else {
            menuPrincipal();
            etat = -1;
            return true;
        }
    }

    public void menuPrincipal() {
        System.out.println("B : Retour - P : Quitter");
        System.out.println("0. Armes");
        System.out.println("1. Compétences");
        System.out.println("2. Consommables");
    }

    public boolean affichageItem(Player joueur, int choix) {
        //Si on est dans une catégorie et le joueur choisit un item
        if (etat != -1 && (choix >= 0 && choix < items.size())) {
            Item item = items.get(choix).useInventaire(joueur);
            if(etat == 0 || etat == 1) {
                items.remove(choix);
                items.add(item);
            }
            if(items.get(choix).getNbrUtilisation() == 0) {
                items.remove(choix);
            }
            etat = -1;
            return false;
        }
        //Si on est dans le Menu Principal et le joueur choisit une catégorie
        if (etat == -1 && (choix == 0 || choix == 1 || choix == 2)) {
            System.out.println("B : Retour - P : Quitter");
            items = (choix == 0) ? armes : (choix == 1) ? competences : consommables;
            for (int i = 0; i < items.size(); i++) {
                items.get(0).messageInventaire();
                System.out.println(i + ". " + items.get(i).description());
            }
            etat = choix;
        }
        //Si le joueur donne un nombre non-valide
        return true;
    }
}