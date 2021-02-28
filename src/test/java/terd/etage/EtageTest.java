package terd.etage;

import junit.framework.TestCase;
import terd.Player.Player;

import static org.junit.jupiter.api.Assertions.*;

public class EtageTest extends TestCase {
    public void testmoveProps()
    {//
        Player player = new Player();
        player.setNewMap(4,4);
        player.setPosition(7,9);
        assertFalse(player.getEtageActuel().moveProps(player,8,10,'@')); // Test pour bord droit bas de l'etage, valide si retourne = false

        player.setNewMap(0,4);
        player.setPosition(0,9);
        assertFalse(player.getEtageActuel().moveProps(player,0,10,'@')); // Test pour bord droit haut de l'etage, valide si retourne = false

        player.setNewMap(0,0);
        player.setPosition(0,0);
        assertFalse(player.getEtageActuel().moveProps(player,-1,0,'@')); // Test pour bord gauche haut de l'etage, valide si retourne = false

        player.setNewMap(4,0);
        player.setPosition(7,0);
        assertFalse(player.getEtageActuel().moveProps(player,0,-1,'@')); // Test pour bord gauche bas de l'etage, valide si retourne = false

        player.getEtageActuel().getMap(4,0).moveProps(5,5,5,5,'A');
        player.setPosition(4,5);
        assertFalse(player.getEtageActuel().moveProps(player,5,5,'@')); // false car je tombe sur un 'A' qui est inconnu et invalide

        player.getEtageActuel().getMap(4,0).moveProps(6,6,6,6,'.');
        player.setPosition(5,6);
        assertTrue(player.getEtageActuel().moveProps(player,6,6,'@')); // true car je me déplace sur une cas '.' qui est valide

        player.getEtageActuel().getMap(3,0).moveProps(7,9,7,9,'X');
        player.setPosition(0,9);
        assertFalse(player.getEtageActuel().moveProps(player,-1,9,'@')); // false car une colline a été placé une case au dessus sur la map supérieur
        //Affichage affichage = new Affichage(player);
        //affichage.afficher();*/
    }
}