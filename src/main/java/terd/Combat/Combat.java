package terd.Combat;

import terd.Map.Pos;
import terd.Player.AbstractMonster;
import terd.Player.OrcWarrior;
import terd.Player.Player;
import terd.Player.Monster;
import terd.controller.GameController;

import java.awt.font.GlyphMetrics;
import java.util.Random;
import java.util.Scanner;

public class Combat {
    private Player joueur;
    private Monster monstre;
    private boolean enCombat = true;
    private GameController gameController;

    public Combat(Player joueur, Monster monstre, GameController gameController){
        this.joueur = joueur;
        this.monstre = monstre;
        this.gameController = gameController;
        combat();
    }

    public void combat() {
        boolean turn = true;
        Scanner scan = new Scanner( System.in );
        String message = "";
        int choix = -1;
        Random random = new Random();
        System.out.println("Vous entrez en combat.");
        System.out.println(joueur.getPV());
        System.out.println(monstre.getPV());
        while (estEnCombat()) {
            affichageApparenceCombat();
            System.out.println(message);
            boolean isInventaire = false;
            if (turn) {
                do{affichageMenuPrincipal();
                choix = scan.nextInt();
                } while (choix != 0 && choix != 1 && choix != 2 && choix != 3);
                if (choix == 0) {
                    message = ("Vous utilisez l'arme " + joueur.getMainWeapon().getNom() + ".\n");
                    message += ("Le monstre subit " + joueur.getMainWeapon().getDegat() + " points de dégat");
                    monstre.takeDamages(joueur.getMainWeapon().getDegat());
                } else if (choix == 1){
                    message = ("Vous utilisez la compétence " + joueur.getMainCompetence().getNom() + ".\n");
                    message += ("Le monstre subit " + joueur.getMainWeapon().getDegat() + " points de dégat");
                    monstre.takeDamages(joueur.getMainCompetence().utiliser());
                } else if (choix == 2){
                    isInventaire = joueur.getInventaire().affiche(scan,gameController, joueur);
                    message = gameController.getMessage();

                } else {
                    if (random.nextInt(1) == 0){
                        System.out.println("Vous prenez la fuite.");
                        gameController.setFreeze(2);
                        break;
                    } else {
                        message = ("Vous n'avez pas réussi à prendre la fuite.");
                    }
                }
                turn = isInventaire;
            } else {
                System.out.println("Prochain Tour : Monstre.");
                System.out.println("Entrez O pour continuer.");
                String entry = scan.next();
                while(!entry.toUpperCase().equals("O")) {
                    entry = scan.next();
                }
                joueur.takeDamages(monstre.getMainWeapon().getDegat());
                message = String.format("%s utilise attaque %s : %d damage", monstre.getName(), monstre.getMainWeapon().getNom(), monstre.getMainWeapon().getDegat());
                monstre.act(joueur);
                turn = true;
            }
        }
        System.out.println("Combat terminé.");
    }


    private void affichageApparenceCombat(){
        System.out.println(
                "//////////////////////////		" + monstre.getName() + "/////////////\n" +
                "//PV : " + monstre.getPV() + "//                           0         \n" +
                "                                      -|-        \n" +
                "                                      / \\        \n" +
                "\n" +
                "\n" +
                "     0 \n" +
                "    -|- \n" +
                "    / \\ \n" +
                "//PV : " + joueur.getPV() + "                                        //\n" +
                "/Player///////////////////////////////////////////"
        );
    }

    private void affichageMenuPrincipal(){
        //affichage des options de base
        System.out.println("Veuillez choisir une action: 0.Attaquer  1.Competence\n" +
                           "                             2.Objet     3.Fuir");
    }

    private boolean estEnCombat(){ //Verification de combat terminé.
        if (joueur.getPV() <= 0){
            System.out.println("Vous avez perdu");
            enCombat = false;
        }
        else if(monstre.getPV() <= 0){
            System.out.println("Monstre vaincu");
            enCombat = false;
        }
        return enCombat;
    }

    public static void main(String[] args) {
        Player player = new Player('@',100);
        Monster monster = new OrcWarrior(new Pos(0,1), 'M');
        GameController gameController = new GameController();
        Combat combat = new Combat(player,monster, gameController);
        combat.combat();
    }
}
