package terd.item;

import terd.Player.Player;
import terd.controller.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Inventaire {
    private List<Item> armes;
    private List<Item> competences;
    private List<Item> consommables;
    private List<Item> items;
    private int etat = -1;
    private Item itemUse;

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
                    consommable.addNbrUtilisation(item.getNbrUtilisation());
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
            itemUse = items.get(choix);
            Item oldItem = items.get(choix).useInventaire(joueur);
            if(etat == 0 || etat == 1) {
                items.remove(choix);
                items.add(oldItem);
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


   public boolean affiche(Scanner scanner, GameController controller, Player player) {
       //Affichage Inventaire
           boolean inInventaire = true;
           StringBuilder sb = new StringBuilder();
           menuPrincipal();
           do {
               String entry = scanner.next();
               inInventaire = !entry.toUpperCase().equals("X");
               if(entry.toUpperCase().equals("B")) {
                   inInventaire = retour();
               }
               if(entry.toUpperCase().equals("P")) {
                   inInventaire = quitter();
               }
               if(Character.isDigit(entry.charAt(0))) {
                   inInventaire = affichageItem(player, Integer.parseInt(entry));
                   if(!inInventaire) {
                       controller.setMessage(player.getInventaire().getItemUse().getMessageInventaire());
                       return true;
                   }
               }
           } while (inInventaire);
       controller.setMessage("Vous quitter l'inventaire");
       return true;
   }

    public List<Item> getCompetence(){
        return competences;
    }

    public List<Item> getConsommables(){
        return consommables;
      
    public int getEtat() {
        return etat;
    }

    public Item getItemUse() {
        return itemUse;
    }

    public List<Item> getArmes() {
        return armes;
    }

    public List<Item> getCompetences() {
        return competences;
    }

    public List<Item> getConsommables() {
        return consommables;
    }
}