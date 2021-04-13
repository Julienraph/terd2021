package terd.controller;
import terd.Player.Player;
import terd.Player.Props;
import terd.etage.Etage;
import terd.item.Item;

import java.util.Scanner;

public class GameController {
    private Etage etage;
    private Player player;
    private boolean keepPlaying = true;
    private boolean refresh = true;
    private int etat = 0;
    private Props monster;
    private int monsterListPosition;
    private int tour = 0;
    String entry;
    String message;
    int dureeMessage;

    public GameController(Etage etage, Player player) {
        this.etage = etage;
        this.player = player;
        this.etage.spawnPlayer(player,etage.getSpawnLigne(),etage.getSpawnColonne());
    }

    public void afficher() {
        Scanner scanner = new Scanner(System.in);
        do {
            if(etat == 0) {
                controllerMap(scanner);
            }
            if(etat == 1) {
                controllerCombat(scanner);
            }
        } while (keepPlaying);
    }

    private void controllerMap(Scanner scanner) {
        do {
            if (refresh) {
                afficherMap();
            }
            refresh = false;
            entry = scanner.next();
            deplacement(entry);
            afficherInventaire(scanner,entry);
            afficherCarte(entry);
            tp(scanner, entry);
            retour(entry);
        }  while (etat == 0);
        refresh = true;
    }

    public void controllerCombat(Scanner scanner) {
        dureeMessage = 0;
        afficherMap();
        System.out.println("EN COMBAT");
        do {
            if(dureeMessage > 0) {
                System.out.println(message);
                dureeMessage = (dureeMessage > 1) ? dureeMessage - 1 : 0;
            }
            System.out.println(String.format("Player PV : %d   | Monster PV : %d", player.getPv(), monster.getPv()));
            if(player.getPv() == 0) {
                System.out.println("GAME OVER");
                keepPlaying = false;
                etat = -1;
                break;
            }
            //Si au tour du Joueur
            if (tour == 0) {
                System.out.println("Entrez A pour attaquer");
                entry = scanner.next();
                afficherInventaire(scanner, entry);
                afficherCarte(entry);
                tour = playerAttack(entry);
            }
            //Si au tour du Monstre
            if(tour == 1) {
                tour = 0;
                player.takeDamages(monster.getMainWeapon().getDegat());
                System.out.println(String.format("%s utilise attaque %s : %d damage", monster.getName(), monster.getMainWeapon().getNom(), monster.getMainWeapon().getDegat()));
            }
        } while (etat == 1);
    }

    private int playerAttack(String entry) {
        if (entry.toUpperCase().equals("A")) {
            monster.takeDamages(player.getMainWeapon().getDegat());
            System.out.println(String.format("%s utilise attaque %s : %d damage", player.getName(), player.getMainWeapon().getNom(), player.getMainWeapon().getDegat()));
            tour = 1;
        }
        if (monster.getPv() == 0) {
            etage.getMap(player.getPosEtageY(), player.getPosEtageX()).killMonster(monsterListPosition);
            etat = 0;
            tour = 0;
            dureeMessage = 4;
            message = "Vous avez gagné";
        }
        return tour;
    }

    private void afficherMap() {
        etage.afficherMap(player.getPosEtageY(), player.getPosEtageX(), player);
        if (dureeMessage > 0) {
            System.out.println(message);
            dureeMessage = (dureeMessage > 1) ? dureeMessage - 1 : 0;
        }
    }

    private void deplacementMonster() {
        int i = etage.getMap(player.getPosEtageY(), player.getPosEtageX()).moveMonsters(player.getPos());
        if(i != -1) {
            etat = 1;
            monster =  etage.getMap(player.getPosEtageY(), player.getPosEtageX()).getMonsterList().get(i);
            monsterListPosition = i;
            etage.afficherMap(player.getPosEtageY(), player.getPosEtageX(), player);
        }
    }

    private void tp(Scanner scanner, String stringEntry) {
        //TP du joueur
        int ligne;
        int colonne;
        if(stringEntry.toUpperCase().equals("T")) {
            System.out.println("Entrez la ligne :");
            ligne = scanner.hasNextInt() ? scanner.nextInt() : -1;
            System.out.println("Entrez la colonne :");
            colonne = scanner.hasNextInt() ? scanner.nextInt() : -1;
            if((ligne < etage.getHauteurEtage() && ligne >= 0 && colonne < etage.getLargeurEtage() && colonne >= 0)
                    && etage.getMap(ligne, colonne) != null) {
                etage.getMap(player.getPosEtageY(), player.getPosEtageX()).resetCase(player.getX(),player.getY(), player);
                etage.spawnPlayer(player, ligne , colonne);
                refresh = true;
            } else {
                System.out.println("Coordonnées non-valide");
                refresh = false;
            }
        }
    }

    private void afficherCarte(String stringEntry) {
        //Affichage de la carte
        if(stringEntry.toUpperCase().equals("M")) {
            etage.afficherCarte(0,0);
            System.out.println("\n Entrez B pour quitter la Carte");
        }
    }

    private void afficherInventaire(Scanner scanner, String stringEntry) {
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
                    if(!inInventaire) {
                        message = player.getInventaire().getItemUse().getMessageInventaire();
                        dureeMessage = 1;
                    }
                }
                if(entry.toUpperCase().equals("X")) {
                    keepPlaying = false;
                    break;
                }
            } while (inInventaire);
        }
    }

    private void retour(String stringEntry) {
        //Retour
        if(stringEntry.toUpperCase().equals("B")) {
            refresh = true;
        }
    }

    public void deplacement(String stringEntry) {
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

        //Si le joueur bouge, on bouge les monstres
        if(refresh) {
            deplacementMonster();
        }
    }


}
