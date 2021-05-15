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
    private int oldChoice;
    private int oldEtat;
    String message;

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
        }
        if(etat == 4) {
            etat = oldEtat;
            afficherCategorie(etat);
            return true;
        }
        menuPrincipal();
        etat = -1;
        return true;
    }

    public void menuPrincipal() {
        System.out.println("B : Retour - P : Quitter");
        System.out.println("0. Armes");
        System.out.println("1. Compétences");
        System.out.println("2. Consommables");
    }

    public boolean affichageItem(Player joueur, int choix) {
        //Si le joueur choisit d'équiper ou améliorer une Arme ou une Compétence
        if(etat == 4 && (choix == 0 || choix == 1)) {
            if(choix == 0) {
                Item oldItem = itemUse.useInventaire(joueur);
                items.remove(oldChoice);
                items.add(oldItem);
                message = joueur.getInventaire().getItemUse().getMessageInventaire();
            }
            if(choix == 1) {
                int pastDamage = itemUse.getDegat();
                boolean enoughMoney = itemUse.ameliorer(joueur);
                if(!enoughMoney) {
                    System.out.println("Vous n'avez pas assez de crédit");
                    return true;
                }
                message = String.format("Vous avez payé %d crédit\nVous avez amélioré %s : +%d dégats", itemUse.getPrix()-1, itemUse.getNom(), itemUse.getDegat() - pastDamage);
            }
            etat = -1;
            return false;
        }
        //Si on est dans une catégorie et le joueur choisit un item (Arme,Compétence ou Consommable)
        if (etat != -1 && (choix >= 0 && choix < items.size())) {
            itemUse = items.get(choix);
            if(etat == 0 || etat == 1) {
                oldEtat = etat; //Sauvegarde l'état pour le retour
                oldChoice = choix; //Sauvegarde le choix pour supprimer l'item de l'inventaire si on l'équipe
                etat = 4;
                System.out.println("B : Retour - P : Quitter");
                System.out.println(itemUse.description());
                System.out.println("0. Equiper");
                System.out.println(String.format("1. Améliorer : +%d dégat (requière %d crédit)", itemUse.getAmelioration(), itemUse.getPrix()));
            }
            if(etat == 2) {
                Item oldItem = items.get(choix).useInventaire(joueur);
                if((items.get(choix).getNbrUtilisation() == 0)) {
                    items.remove(choix);
                }
                message = joueur.getInventaire().getItemUse().getMessageInventaire();
                etat = -1;
                return false;
            }
        }
        //Si on est dans le Menu Principal et le joueur choisit une catégorie
        if (etat == -1 && (choix == 0 || choix == 1 || choix == 2)) {
            afficherCategorie(choix);
        }
        //Si le joueur donne un nombre non-valide
        return true;
    }

    private void afficherCategorie(int choix) {
        System.out.println("B : Retour - P : Quitter");
        items = (choix == 0) ? armes : (choix == 1) ? competences : consommables;
        if(items.size() != 0) {
            items.get(0).messageInventaire();
        }
        for (int i = 0; i < items.size(); i++) {
            System.out.println(i + ". " + items.get(i).description());
        }
        etat = choix;
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
               if(Character.isDigit(entry.charAt(0)) && entry.length() == 1) {
                   inInventaire = affichageItem(player, Integer.parseInt(entry));
                   if(!inInventaire) {
                       controller.setMessage(message);
                       return true;
                   }
               }
           } while (inInventaire);
       controller.setMessage("Vous avez quitté l'inventaire");
       return true;
   }

    public List<Item> getCompetence(){
        return competences;
    }

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