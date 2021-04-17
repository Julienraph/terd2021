package terd.controller;
import junit.framework.TestCase;
import terd.Map.Pos;
import terd.Player.Player;
import terd.etage.Etage;
import terd.utils.Seed;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest extends TestCase {
    public void testDeplacement() {
        Etage etage = new Etage(3, 3, 20, 20);
        Player player = new Player('@', 100);
        player.setEtageActuel(etage);
        player.setNewMap(1, 0);
        GameController gameController = new GameController(etage, player);
        //On assigne la position (10,10) au Player et on test les déplacements
        player.setPos(new Pos(10, 10));
        gameController.deplacement("z");
        assertEquals(player.getPos(), new Pos(10, 9));
        gameController.deplacement("Z");
        assertEquals(player.getPos(), new Pos(10, 8));
        gameController.deplacement("s");
        assertEquals(player.getPos(), new Pos(10, 9));
        gameController.deplacement("S");
        assertEquals(player.getPos(), new Pos(10, 10));
        gameController.deplacement("q");
        assertEquals(player.getPos(), new Pos(9, 10));
        gameController.deplacement("Q");
        assertEquals(player.getPos(), new Pos(8, 10));
        gameController.deplacement("d");
        assertEquals(player.getPos(), new Pos(9, 10));
        gameController.deplacement("D");
        assertEquals(player.getPos(), new Pos(10, 10));
    }
}
