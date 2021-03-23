package terd.affichage;
import terd.Player.Player;
import terd.controller.GameController;
import terd.etage.Etage;
import terd.utils.Seed;


public class Affichage {
    private GameController gameController;

    public Affichage(int hauteurEtage, int largeurEtage, int coefHauteurMap, int coefLargeurMap, char skin, Seed seed) {
        Player player = new Player(skin);
        Etage etage = new Etage(hauteurEtage, largeurEtage, coefHauteurMap, coefLargeurMap, 0, 0, seed);
        etage.spawnPlayer(player,0,0);
        this.gameController = new GameController(etage, player);
    }

    public static void main(String[] args) {
        Seed seed = new Seed();
        Affichage affichage = new Affichage(3,4, 4,15, '@', seed);
        affichage.gameController.afficher();
    }
}
