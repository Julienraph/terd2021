package terd.item;
import junit.framework.TestCase;
import org.junit.Test;
import terd.Map.Pos;
import terd.Player.Player;
import terd.item.Arme;

public class InventaireTest extends TestCase {

    public void testAjoutItem() {
        Inventaire inventaire = new Inventaire();

        //On regarde si arme va dans la bonne liste
        Arme arme = new Arme(0,"Hache",2,0,20);
        inventaire.ajoutItem(arme);
        assertEquals("Hache",inventaire.getArmes().get(0).getNom());

        //On regarde si competence va dans la bonne liste
        Competence competence = new Competence(0,"Feu",2,0,20);
        inventaire.ajoutItem(competence);
        assertEquals("Feu",inventaire.getCompetences().get(0).getNom());

        //On regarde si consommable va dans la bonne liste
        //On ajoute 2 Cerise, puis 4 Cerise, et on regarde si ça s'incrémente bien
        Consommable consommable = new Consommable(0,"Cerise",2,0,20);
        inventaire.ajoutItem(consommable);
        assertEquals("Cerise",inventaire.getConsommables().get(0).getNom());
        Consommable consommable2 = new Consommable(0,"Cerise",4,0,20);
        inventaire.ajoutItem(consommable2);
        assertEquals(6,inventaire.getConsommables().get(0).getNbrUtilisation());
    }

    public void testAffichageItem() {

        //Le Player à une épée en Arme Principal et Eau en Compétence Principale
        //Dans l'inventaire à l'initialisation de player il y a une arme "Hache", une compétence "Feu",et deux cerise.
        Player player = new Player('@', 100);
        Inventaire inventaire = player.getInventaire();

        //Inventaire à 4 état :
        // -1 = Dans le Menu Principal, 0 = Dans les Armes, 1 = Dans les Compétences, 2 = Dans les Consommables
        // On choisit d'aller dans les Armes
        inventaire.affichageItem(player,0);
        //On est dans l'état 0, on choisit la première arme qui est une Hache
        inventaire.affichageItem(player,0);
        assertEquals("Hache", player.getMainWeapon().getNom());

        //Vérifions que l'état s'est bien réinitialisé à -1
        assertEquals(-1, inventaire.getEtat());

        //Vérifions que l'épée s'est bien remis dans l'Inventaire
        assertEquals("Epée", inventaire.getArmes().get(0).getNom());

        //Refaisons la même chose pour Compétence
        inventaire.affichageItem(player,1);
        inventaire.affichageItem(player,0);
        assertEquals("Feu", player.getMainCompetence().getNom());
        assertEquals(-1, inventaire.getEtat());
        assertEquals("Eau", inventaire.getCompetences().get(0).getNom());

        //Vérifions que lorsque on utilise un consommable, celui-ci diminue de 1.
        assertEquals(2,  inventaire.getConsommables().get(0).getNbrUtilisation());
        player.takeDamages(50);
        inventaire.affichageItem(player,2);
        inventaire.affichageItem(player,0);
        assertEquals(1,  inventaire.getConsommables().get(0).getNbrUtilisation());
    }

    public void testQuitter() {
        //Le Player à une épée en Arme Principal et Eau en Compétence Principale
        //Dans l'inventaire à l'initialisation de player il y a une arme "Hache", une compétence "Feu",et deux cerise.
        Player player = new Player('@', 100);
        Inventaire inventaire = player.getInventaire();
        inventaire.affichageItem(player,2);
        boolean inInventory = inventaire.quitter();
        assertEquals(-1,  inventaire.getEtat());
        assertFalse(inInventory);
    }


    public void testRetour() {
        //Le Player à une épée en Arme Principal et Eau en Compétence Principale
        //Dans l'inventaire à l'initialisation de player il y a une arme "Hache", une compétence "Feu",et deux cerise.
        Player player = new Player('@', 100);
        Inventaire inventaire = player.getInventaire();
        inventaire.affichageItem(player,2);
        boolean inInventory = inventaire.retour();
        //Si dans les Menus on revient au Menu Principal
        assertEquals(-1,  inventaire.getEtat());
        assertTrue(inInventory);
        //Si dans le Menu Principal on quitte l'inventaire
        inInventory = inventaire.retour();
        assertFalse(inInventory);
    }
}

