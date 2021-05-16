package terd.controller;
import terd.Combat.Combat;
import terd.Player.Monster;
import terd.Player.Player;
import terd.Player.Props;
import terd.etage.Etage;
import terd.utils.Seed;

import java.util.Scanner;

public class GameController {
    private Etage etage;
    private Player player;
    private boolean keepPlaying = true;
    private boolean refresh = true;
    private int etat = 0;
    private Monster monster;
    private int monsterListPosition;
    private int tour = 0;
    String entry;
    String message;
    int dureeMessage;
    int freeze;
    Seed partieSeed;

    public GameController(Etage etage, Player player) {
        this.etage = etage;
        this.player = player;
        this.etage.spawnPlayer(player,etage.getSpawnLigne(),etage.getSpawnColonne());
        this.partieSeed = etage.getSeed();
    }

    public GameController() {
    }



    public boolean afficher() {
        Scanner scanner = new Scanner(System.in);
        do {
            if(etat == 0) {
                controllerMap(scanner);
            }
            if(etat == 1) {
                //Classe Combat qui se lance si le joueur se trouve à coté d'un Monstre
                controllerCombat(scanner);
            }
            if(!keepPlaying) {
                //Menu Principal si vous arrêtez le jeu, si vous mourrez ou si vous gagnez
                controllerMenu(scanner);
            }
        } while (keepPlaying);
        return true;
    }

    private void controllerMenu(Scanner scanner) {
        boolean inRetryMenu = true;
        do{
            if(player.getPV() > 0) {
                System.out.println("0. Reprendre la partie");
            }
            System.out.println("1. Relancer la même Seed");
            System.out.println("2. Relancer une nouvelle Seed");
            System.out.println("3. Obtenir le nom de la Seed");
            System.out.println("4. Donner le nom d'une Seed");
            System.out.println("5. Arrêter le jeu");
            int entry = scanner.hasNextInt() ? scanner.nextInt() : -1;
            if(entry == 1 || entry == 2) {
                this.player = new Player(player.getSkin(), player.getMaxPV());
                Seed seed = partieSeed;
                if(entry == 2) {
                    seed = new Seed();
                }
                this.etage = new Etage(etage.getHauteurEtage(), etage.getLargeurEtage(), etage.getHauteurMinMap(), etage.getLargeurMinMap(), etage.getBiome(), etage.getNiveau(), seed);
                this.etage.spawnPlayer(player,etage.getSpawnLigne(),etage.getSpawnColonne());
                this.tour = 0;
                this.etat = 0;
                this.refresh = true;
                this.keepPlaying = true;
                inRetryMenu = false;
            }
            if(entry == 3) {
                System.out.println("\nNom de la Seed :");
                System.out.println(partieSeed.getSeed());
                System.out.println("\n");
            }
            if(entry == 4) {
                System.out.println("Entrez le nom de la Seed :");
                String seedString = scanner.next();
                if(seedString.length() >= 500) {
                    Seed seed = new Seed(seedString);
                    this.etage = new Etage(etage.getHauteurEtage(), etage.getLargeurEtage(), etage.getHauteurMinMap(), etage.getLargeurMinMap(), etage.getBiome(), etage.getNiveau(), seed);
                    this.etage.spawnPlayer(player, etage.getSpawnLigne(), etage.getSpawnColonne());
                    this.tour = 0;
                    this.etat = 0;
                    this.refresh = true;
                    this.keepPlaying = true;
                    inRetryMenu = false;
                } else {
                    System.out.println("Seed non-valide");
                }
            }
            if(entry == 5) {
                inRetryMenu = false;
            }
            if(entry == 0 && player.getPV() > 0) {
                keepPlaying = true;
                etat = 0;
                return;
            }
            if(entry == -1) {
                scanner.next();
            }
        } while(inRetryMenu);
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
            afficherCarte(scanner, entry);
            tp(scanner, entry);
            retour(entry);
            arreterJeu(entry);
        }  while (etat == 0);
        refresh = true;
    }

    public void controllerCombat(Scanner scanner) {
        Combat combat = new Combat(player, monster, this);
        dureeMessage = 0;
        etat = 0;
        if(player.getPV() <= 0) {
            System.out.println("Vous avez perdu");
            System.out.println("GAME OVER");
            keepPlaying = false;
            etat = -1;
        }
        if (monster.getPV() <= 0) {
            etage.getMap(player.getPosEtageY(), player.getPosEtageX()).killMonster(monsterListPosition);
            etat = 0;
            tour = 0;
            dureeMessage = 4;
            message = monster.recompensePlayer(player) + player.addXP(monster.getXP());
        }
    }

    private void afficherMap() {
        etage.afficherMap(player.getPosEtageY(), player.getPosEtageX(), player);
        if (dureeMessage > 0) {
            System.out.println(message);
            dureeMessage = (dureeMessage > 1) ? dureeMessage - 1 : 0;
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

    private void afficherCarte(Scanner scanner, String stringEntry) {
        //Affichage de la carte
        boolean inMap = true;
        if (stringEntry.toUpperCase().equals("M")) {
            etage.afficherCarte(0, 0);
            do {
                System.out.println("\n Entrez B pour quitter la Carte");
                entry = scanner.next();
                inMap = (!entry.toUpperCase().equals("B") && !entry.toUpperCase().equals("M") && !arreterJeu(entry));
            } while (inMap);
        }
    }

    private void afficherInventaire(Scanner scanner, String stringEntry) {
        //Affichage Inventaire
        if(stringEntry.toUpperCase().equals("P")) {
            refresh = true;
            player.getInventaire().affiche(scanner,this,player);
            dureeMessage = 1;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDureeMessage(int dureeMessage) {
        this.dureeMessage = dureeMessage;
    }

    private void retour(String stringEntry) {
        //Retour
        if(stringEntry.toUpperCase().equals("B")) {
            refresh = true;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setFreeze(int freeze) {
        this.freeze = freeze;
    }

    public boolean arreterJeu(String stringEntry) {
        //Retour
        if(stringEntry.toUpperCase().equals("X")) {
            keepPlaying = false;
            etat = -1;
        }
        return !keepPlaying;
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

        if(refresh) {
            freeze -= 1;
        }

        //Si le joueur bouge, on bouge les monstres
        if(refresh && freeze <= 0) {
            int i = etage.getMap(player.getPosEtageY(), player.getPosEtageX()).moveMonsters(player.getPos());
            if(i != -1) {
                etat = 1;
                monster =  etage.getMap(player.getPosEtageY(), player.getPosEtageX()).getMonsterList().get(i);
                monsterListPosition = i;
                etage.afficherMap(player.getPosEtageY(), player.getPosEtageX(), player);
            }
        }

        //Si le joueur est sur une porte, on créé un nouvel Etage
        if(player.getCache() == 'D') {
            if(tour < 2) {
                tour += 1;
                Seed seed = new Seed(etage.getSeed(), tour);
                this.etage = new Etage(etage.getHauteurEtage(), etage.getLargeurEtage(), etage.getHauteurMinMap(), etage.getLargeurMinMap(), etage.getBiome(), etage.getNiveau(), seed);
                this.etage.spawnPlayer(player, etage.getSpawnLigne(), etage.getSpawnColonne());
                this.etat = 0;
                this.refresh = true;
                this.keepPlaying = true;
                dureeMessage = 3;
                message = "Vous êtes passé par une porte\nNouveau lieu découvert";
            } else {
                afficherMap();
                this.keepPlaying = false;
                this.etat = -1;
                System.out.println("VOUS AVEZ GAGNE, FIN DE LA PARTIE");
            }
        }

        if(player.getCache() == 'P' || player.getCache() == 'O') {
            message =  etage.getMap(player.getPosEtageY(), player.getPosEtageX()).recupererTresor(player);
            dureeMessage = 1;
        }

    }


}
