package terd.Player;
import junit.framework.TestCase;
import org.junit.Test;
import terd.Map.Pos;
import terd.item.Arme;

public class AbstractPropsTest extends TestCase {

    public void testTakeDamages() {
        Props props = new Player('@',100);
        //Le props a un maximum de PV à 100, on rajoute 10 PV et on regarde que ça reste toujours 100 PV
        props.takeDamages(-10);
        assertEquals(props.getPV(),100);
        //Le joueur perd 10 PV
        props.takeDamages(10);
        assertEquals(props.getPV(),90);
        //Le joueur perd 200 PV, on regarde que ça assigne bien 0 PV
        props.takeDamages(200);
        assertEquals(props.getPV(),0);
    }

    public void testUseWeapon() {
        Props props = new Player('@',100);
        Props props2 = new Player('@',100);
        props.setMainWeapon(new Arme(0,"hache",3,0,20));
        props.useWeapon(props2);
        assertEquals(props2.getPV(),80);
    }

    public void testGetDistance() {
        Props props = new Player('@',100);
        Props props2 = new Player('@',100);
        props.setPos(new Pos(10,18));
        props2.setPos(new Pos(5,6));
        assertEquals(props.getDistance(props2), 13.0);
    }

    public void testAddXP() {
        //XP vaut 0 à l'initialisation, et le max d'XP est de 99.
        //Lorsqu'il est égal à 100, le Props gagne un level et gagne l'XP restant
        // Exemple : +130 XP va donner un level supplémentaire et +30 XP
        Props props = new Player('@',100);
        props.addXP(50);
        assertEquals(props.getLevelProps(), 1);
        assertEquals(props.getXP(), 50);
        props.addXP(50);
        assertEquals(props.getLevelProps(), 2);
        assertEquals(props.getXP(), 0);
        props.addXP(150);
        assertEquals(props.getLevelProps(), 3);
        assertEquals(props.getXP(), 50);
        props.addXP(200);
        assertEquals(props.getLevelProps(), 5);
        assertEquals(props.getXP(), 50);
    }

    public void testIsBeside() {
        Props props = new Player('@',100);
        Props props2 = new Player('@',100);
        props.setPos(new Pos(10,10));
        props2.setPos(new Pos(11,10));
        assertTrue(props.isBeside(props2.getPos()));
        props2.setPos(new Pos(9,10));
        assertTrue(props.isBeside(props2.getPos()));
        props2.setPos(new Pos(10,9));
        assertTrue(props.isBeside(props2.getPos()));
        props2.setPos(new Pos(10,11));
        assertTrue(props.isBeside(props2.getPos()));
        props2.setPos(new Pos(10,12));
        assertFalse(props.isBeside(props2.getPos()));
        props2.setPos(new Pos(12,10));
        assertFalse(props.isBeside(props2.getPos()));
    }
}

