package terd.Combat;

import terd.Player.AbstractMonster;
import terd.Player.Player;
import terd.Player.Monster;

import java.util.Random;
import java.util.Scanner;

public class Combat {
    private Player joueur;
    private Monster monstre;
    private boolean enCombat;

    public Combat(Player joueur, Monster monstre){
        this.joueur = joueur;
        this.monstre = monstre;
    }

    public void combat() {
        boolean turn = true;
        Scanner scan = new Scanner( System.in );
        int choix = -1;
        Random random = new Random();
        System.out.println("Vous entrez en combat.");
        while (estEnCombat()) {
            affichageApparenceCombat();
            if (turn) {
                do{affichageMenuPrincipal();
                choix = scan.nextInt();
                } while (choix != 0 && choix != 1 && choix != 2 && choix != 3);
                if (choix == 0) {
                    System.out.println("Vous attaquez le monstre il subit " + joueur.getMainWeapon().getDegat() + " points de dégat");
                    monstre.takeDamages(joueur.getMainWeapon().getDegat());
                } else if (choix == 1){
                    for(int i=0; i < joueur.getInventaire().getCompetence().size(); i++){
                        System.out.println(i + ". " + joueur.getInventaire().getCompetence().get(i).getNom()+ " Nbr util: " + joueur.getInventaire().getCompetence().get(i).getNbrUtilisation());
                    }
                    System.out.println(joueur.getInventaire().getCompetence().size() + ". Retour");
                    int choixCompetence = -1;
                    do {
                        System.out.println("Choisissez votre competence");
                        choixCompetence = scan.nextInt();
                    }
                    while (choixCompetence < 0 && choixCompetence > joueur.getInventaire().getCompetence().size()) ;
                    if (choixCompetence == joueur.getInventaire().getCompetence().size()) {
                        continue;
                    } else {
                        System.out.println("Vous utilisez la compétence " + joueur.getInventaire().getCompetence().get(choixCompetence).getNom() + ".");
                        joueur.getInventaire().getCompetence().get(choixCompetence).utiliser();
                    }
                } else if (choix == 2){
                    for(int i=0; i < joueur.getInventaire().getConsommables().size(); i++){
                        System.out.println(i + ". " + joueur.getInventaire().getConsommables().get(i).getNom()+ " Nbr util: " + joueur.getInventaire().getConsommables().get(i).getNbrUtilisation());
                    }
                    System.out.println(joueur.getInventaire().getConsommables().size() + ". Retour");
                    int choixConso = -1;
                    do {
                        System.out.println("Choisissez votre objet");
                        choixConso = scan.nextInt();
                    }
                    while (choixConso < 0 && choixConso > joueur.getInventaire().getConsommables().size()) ;
                    if (choixConso == joueur.getInventaire().getConsommables().size()) {
                        continue;
                    } else {
                        System.out.println("Vous utilisez l'objet " + joueur.getInventaire().getConsommables().get(choixConso).getNom() + ".");
                        joueur.getInventaire().getConsommables().get(choixConso).utiliser();
                    }
                } else {
                    if (random.nextInt(1) == 0){
                        System.out.println("Vous prenez la fuite.");
                        break;
                    } else {
                        System.out.println("Vous n'avez pas réussi à prendre la fuite.");
                    }
                }
                turn = false;
            } else {
                System.out.println("Tour du monstre.");
                monstre.act(joueur);
                turn = true;
            }
        }
        System.out.println("Combat terminé.");
    }


    private void affichageApparenceCombat(){
        System.out.println(
                "//////////////////////////		" + monstre.getName() + "/////////////\n" +
                "//PV : " + monstre.getPv() + "//                           0         \n" +
                "                                      -|-        \n" +
                "                                      / \\        \n" +
                "\n" +
                "\n" +
                "     0 \n" +
                "    -|- \n" +
                "    / \\ \n" +
                "//PV : " + joueur.getPv() + "                                        //\n" +
                "/Player///////////////////////////////////////////"
        );
    }

    private void affichageMenuPrincipal(){
        //affichage des options de base
        System.out.println("Veuillez choisir une action: 0.Attaquer  1.Competence\n" +
                           "                             2.Objet     3.Fuir");
    }

    private boolean estEnCombat(){ //Verification de combat terminé.
        if (joueur.getPv() <= 0){
            System.out.println("Vous avez perdu");
            enCombat = false;
        }
        else if(monstre.getPv() <= 0){
            System.out.println("Monstre vaincu");
            enCombat = false;
        }
        return enCombat;
    }
}
