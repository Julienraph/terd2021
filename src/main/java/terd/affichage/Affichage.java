package terd.affichage;
import terd.Player.Player;
import terd.controller.GameController;
import terd.etage.Etage;
import terd.utils.Seed;


public class Affichage {
    private GameController gameController;

    public Affichage(int hauteurEtage, int largeurEtage, int coefHauteurMap, int coefLargeurMap, char skin, Seed seed) {
        Player player = new Player(skin, 100);
        Etage etage = new Etage(hauteurEtage, largeurEtage, coefHauteurMap, coefLargeurMap, 0, 0, seed);
        this.gameController = new GameController(etage, player);
    }

    public static void main(String[] args) {
        //Seed seed = new Seed("16377c2b9ddd5039383fc08e4506619479560e1d3784aa46f051a461d54391675eb5a029052ad24f90bf365b0ea234cb3749a898488ca82ee99af61d1e11d0a862fe4e47a390cc24875cf19be6a3c7321c746ccdce26eddc532b032e0b3b596f61119cb3e784cf9679beb3bcdab47bb778207fd7ce683dabb29f0112ce9abffac34191a9b739397a3e884854acdd9233fe30bb34015b990b5f4093b1ee830a0707e7ebb7cab359f548ba8d48a42e8fe7ff6af491e552e8d57f4d54fac164965f6f0ce9bdd37e9c7864eb7ac6d4c8a74a35c345d1bda4a507c3a75d2e52270501db1f12b4a89caf9912772b2686ef200c43c8554c7c767d0e60ecab");
        Seed seed=new Seed();
        System.out.println(seed.getSeed());

        Affichage affichage = new Affichage(8,4, 5,12, '@', seed);
        affichage.gameController.afficher();
    }
}
