package terd.item;


import terd.Player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public  class Inventaire {
    private List<Arme> armes;
    private List<Competence> competences;
    private List<Consommable> consommables;
    private Player joueur;

    public Inventaire(Player joueur) {
        armes = new ArrayList<>();
        competences = new ArrayList<>();
        consommables = new ArrayList<>();
        this.joueur = joueur;

    }

    public void ajoutItem(Item item) {
        if (item instanceof Arme) {
            armes.add((Arme) item);
        } else if (item instanceof Competence) {
            competences.add((Competence) item);
        } else if (item instanceof Consommable) {
            consommables.add((Consommable) item);
        }
    }

    public void affichage() {
        System.out.println("0. Armes");               //Affichage du menu principal
        System.out.println("1. Compétences");
        System.out.println("2. Consommables");
        System.out.println("3. Quitter");
        try (Scanner scanner = new Scanner(System.in)) {
            int numeroChoix = -1;
            do {
                System.out.print("Veuillez saisir le numéro de l'inventaire: "); //Choix du menu
                numeroChoix = scanner.nextInt();
                if (numeroChoix == 1) {                                            // début Arme
                    for (int i = 0; i < armes.size(); i++) {
                        System.out.println(i + ". " + armes.get(i).getNom());   //affichage des armes
                    }
                    System.out.println(armes.size() + ". Quitter");
                    int numeroArme = -1;
                    do {
                        System.out.println("Choisissez l'arme à équiper: ");
                        numeroArme = scanner.nextInt();
                    } while (numeroArme < 0 || numeroArme > armes.size());
                    if(numeroArme == armes.size()){
                        System.out.println("Fermeture de l'inventaire");
                    } else {
                        joueur.setArmeActuel(armes.get(numeroArme));
                    }                                                           // fin Arme
                } else if (numeroChoix == 2) {                                  //debut competence
                    for (int i = 0; i < competences.size(); i++) {               //affichage des competences
                        System.out.println(i + ". " + competences.get(i).getNom());
                    }
                    System.out.println(competences.size() + ". Quitter");
                    int numeroCompetence = -1;
                    do {
                        System.out.println("Choisissez votre compétence");
                        numeroCompetence = scanner.nextInt();
                    } while (numeroCompetence < 0 || numeroCompetence > competences.size());
                    if(numeroCompetence == competences.size()){
                        System.out.println("Fermeture de l'inventaire");
                    } else {
                        //TODO UTILISATION COMPETENCE
                    }
                                                                                 //fin competence
                } else if (numeroChoix == 3) {                                  //debut consommable
                    for (int i = 0; i < consommables.size(); i++) {             //affichage des consommables
                        System.out.println(i + ". " + consommables.get(i).getNom() + "nbr utilisation: " + consommables.get(i).nbrUtilisation);
                    }
                    System.out.println(competences.size() + ". Quitter");
                    int numeroConsommable = -1;
                    do {
                        System.out.println("Quel consommable voulez-vous utiliser ?");
                        numeroConsommable = scanner.nextInt(); //choix consommable
                    } while ( numeroConsommable < 0 || numeroConsommable > consommables.size());
                    if(numeroConsommable == consommables.size()){
                        System.out.println("Fermeture de l'inventaire");
                    } else {
                        if(consommables.get(numeroConsommable).getNbrUtilisation() > 0){ //s'il y a assez de nbr utilisation
                            joueur.addPV(consommables.get(numeroConsommable).degat);
                            consommables.get(numeroConsommable).setNbrUtilisation(1);
                            if (consommables.get(numeroConsommable).getNbrUtilisation() <= 0){ //suppr de l'inventaire si nbr utilisation <= 0
                                consommables.remove(numeroConsommable);
                            }
                        }
                    }                                                                  //fin consommable
                } else if (numeroChoix == 4) {
                    System.out.println("Fermeture de l'inventaire");
                }
            } while (numeroChoix != 0 && numeroChoix != 1 && numeroChoix != 2 && numeroChoix != 3);
        }
    }
}