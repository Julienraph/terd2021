package terd.controller;
import terd.Player.Player;
import terd.etage.Etage;

import java.util.Scanner;

public class GameController {
    private Etage etage;
    private Player player;
    private boolean keepPlaying = true;
    private boolean refresh = true;

    public GameController(Etage etage, Player player) {
        this.etage = etage;
        this.player = player;
    }

    public void afficher(){
        Scanner scanner = new Scanner(System.in);
        do {
            if(refresh) {
                etage.afficherMap(player.getPosEtageY(), player.getPosEtageX(), player);
            }
            String entry = scanner.next();
            controller(scanner, entry);
        } while (keepPlaying);
    }

    public void controller(Scanner scanner, String stringEntry){
        refresh = false;

        //Déplacement du joueur
        char boutonDeplacement = stringEntry.charAt(0);
        int nextX = player.getX();
        int nextY = player.getY();
        if (boutonDeplacement == 'z' || boutonDeplacement == 'Z') {
            nextY = nextY - player.getSpeed();
            player.getEtageActuel().moveProps(player, nextX, nextY, player.getSkin());
            refresh = true;
        } else if (boutonDeplacement == ('s') || boutonDeplacement == ('S')) {
            nextY = nextY + player.getSpeed();
            player.getEtageActuel().moveProps(player, nextX, nextY, player.getSkin());
            refresh = true;
        } else if (boutonDeplacement == ('q') || boutonDeplacement == ('Q')) {
            nextX = nextX - player.getSpeed();
            player.getEtageActuel().moveProps(player, nextX, nextY, player.getSkin());
            refresh = true;
        } else if (boutonDeplacement == ('d') || boutonDeplacement == ('D')) {
            nextX = nextX + player.getSpeed();
            player.getEtageActuel().moveProps(player, nextX, nextY, player.getSkin());
            refresh = true;
        } else if (boutonDeplacement == ('x') || boutonDeplacement == ('X')) {
            keepPlaying = false;
        }



        //TP du joueur
        int ligne;
        int colonne;
        if(stringEntry.toUpperCase().equals("T")) {
            System.out.println("Entrez la ligne :");
            ligne = scanner.hasNextInt() ? scanner.nextInt() : -1;
            System.out.println("Entrez la colonne :");
            colonne = scanner.hasNextInt() ? scanner.nextInt() : -1;
            if(ligne < etage.getHauteurEtage() && ligne >= 0 && colonne < etage.getLargeurEtage() && colonne >= 0) {
                etage.getMap(player.getPosEtageY(), player.getPosEtageX()).resetCase(player.getX(),player.getY());
                etage.spawnPlayer(player, ligne , colonne);
                refresh = true;
            } else {
                System.out.println("Coordonnées non-valide");
                refresh = false;
            }
        }

        //Affichage de la carte
        if(stringEntry.toUpperCase().equals("M")) {
            etage.afficherCarte(0,0);
        }

        //Affichage Inventaire
        if(stringEntry.toUpperCase().equals("P")) {
            refresh = true;
            boolean inInventaire = true;
            player.getInventaire().menuPrincipal();
            do {
                String entry = scanner.next();
                if(entry.toUpperCase().equals("B")) {
                    inInventaire = player.getInventaire().retour();
                }
                if(entry.toUpperCase().equals("P")) {
                    inInventaire = player.getInventaire().quitter();
                }
                if(Character.isDigit(entry.charAt(0))) {
                    inInventaire = player.getInventaire().affichageItem(player, Integer.parseInt(entry));
                }
            } while (inInventaire);
        }

        //Retour
        if(stringEntry.toUpperCase().equals("B")) {
            refresh = true;
        }
    }


}
