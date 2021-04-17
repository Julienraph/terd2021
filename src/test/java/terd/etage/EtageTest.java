package terd.etage;

import junit.framework.TestCase;
import terd.Map.Pos;
import terd.Player.Player;
import terd.utils.Seed;

import static org.junit.jupiter.api.Assertions.*;

public class EtageTest extends TestCase {
    public void testMoveProps()
    {
        Etage etage = new Etage(3,3,20,20);
        Player player = new Player('@',100);
        player.setEtageActuel(etage);

        /*
         Les test suivants servent à tester le passage d'une map à l'autre d'un Props ainsi que
         vérifier si les ponts se sont bien créé entres les deux maps.
         Etage : [0 0][0 1][0 2]
                 [1 0][1 1][1 2]
                 [2 0][2 1][2 2]
         La map [0 1] à une sortie vers le bas vers la map [1 1]
         La map [2 1] à une sortie vers le haut vers la map [1 1]
         La map [1 0] à une sortie vers la droite vers la map [1 1]
         La map [1 2] à une sortie vers la gauche vers la map [1 1]
         Si le pont s'est pas créé, alors le Props ne pourrait pas se déplacer puisque des murs entourent chaque map
         */

        //On assigne la position dans l'étage à [1 0] au Player
        //La Map en [1 0] à une sortie à droite
        player.setNewMap(1,0);
        int sortieDroitY = etage.getMap(1,0).getDroite().getX();
        int sortieDroitX = etage.getMap(1,0).getTailleReelY() - 1;
        player.setPosition(sortieDroitX,sortieDroitY);
        player.getEtageActuel().moveProps(player,sortieDroitX+1,sortieDroitY,'@'); //
        assertTrue(player.getPosEtageX() == 1 && player.getPosEtageY() == 1);
        assertTrue(player.getX() == 0 && player.getY() == sortieDroitY);

        //On assigne la position dans l'étage à [1 2] pour le player
        //La Map en [1 2] à une sortie à gauche
        player.setNewMap(1,2);
        int sortieGaucheY = etage.getMap(1,2).getGauche().getX();
        int sortieGaucheX = 0;
        player.setPosition(sortieGaucheX,sortieGaucheY);
        player.getEtageActuel().moveProps(player,sortieGaucheX-1,sortieGaucheY,'@'); //
        assertTrue(player.getPosEtageX() == 1 && player.getPosEtageY() == 1);
        assertTrue(player.getX() == etage.getMap(1,1).getTailleReelY()-1 && player.getY() == sortieGaucheY);

        //On assigne la position dans l'étage à [2 1] pour le player
        //La Map en [2 1] à une sortie vers le haut
        player.setNewMap(2,1);
        int sortieHautY = 0;
        int sortieHautX = etage.getMap(2,1).getHaut().getY();
        player.setPosition(sortieHautX,sortieHautY);
        player.getEtageActuel().moveProps(player,sortieHautX,sortieHautY-1,'@');
        assertTrue(player.getPosEtageX() == 1 && player.getPosEtageY() == 1);
        assertTrue(player.getX() == sortieHautX && player.getY() == etage.getMap(1,1).getTailleReelX()-1);

        //On assigne la position dans l'étage à [0 1] pour le player
        //La Map en [0 1] à une sortie vers le bas
        player.setNewMap(0,1);
        int sortieBasY = etage.getMap(0,1).getTailleReelX() - 1;
        int sortieBasX = etage.getMap(0,1).getBas().getY();
        player.setPosition(sortieBasX,sortieBasY);
        player.getEtageActuel().moveProps(player,sortieBasX,sortieBasY+1,'@'); //
        assertTrue(player.getPosEtageX() == 1 && player.getPosEtageY() == 1);
        assertTrue(player.getX() == sortieBasX && player.getY() == 0);

        /*
         Testons désormais les cases, un Props à le droit de se déplacer que dans '.' et ','
         On place un '.' devant lui et on test si il peut bien se déplacer dessus
         */
        player.setPosition(5,5);
        etage.getMap(1,1).popProps(6,5,'.');
        assertTrue(player.getEtageActuel().moveProps(player,6,5,'@'));
        //On place un ',' devant lui et on test si il peut bien se déplacer dessus
        player.setPosition(5,5);
        etage.getMap(1,1).popProps(6,5,',');
        assertTrue(player.getEtageActuel().moveProps(player,6,5,'@'));
        //On place une colline, un lac, un mur et un arbre autour de lui
        //Et on test qu'il ne peut pas se déplacer sur ces cases
        player.setPosition(5,5);
        etage.getMap(1,1).popProps(6,5,'X');
        etage.getMap(1,1).popProps(5,4,'#');
        etage.getMap(1,1).popProps(4,5,'L');
        etage.getMap(1,1).popProps(5,6,'T');
        assertFalse(player.getEtageActuel().moveProps(player,6,5,'@'));
        assertFalse(player.getEtageActuel().moveProps(player,5,4,'@'));
        assertFalse(player.getEtageActuel().moveProps(player,4,5,'@'));
        assertFalse(player.getEtageActuel().moveProps(player,5,6,'@'));
        etage.afficherCarte(0,0);
    }
}
