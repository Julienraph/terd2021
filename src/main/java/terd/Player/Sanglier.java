package terd.Player;

import terd.Map.Map;
import terd.Map.Pos;
import terd.item.Arme;
import terd.item.Consommable;

import java.util.Random;

public class Sanglier extends AbstractMonster {
    Pos cache = new Pos(-1, -1);

    public Sanglier(Pos pos, char skin) {
        super("Sanglier", pos, 0, 0, skin, 80, 40);
        this.setMainWeapon(new Arme(0, "Charge", 10, 0, 5));
    }

    @Override
    public String recompensePlayer(Player player) {
        Random random = new Random();
        int nb = 1 + random.nextInt(3);
        Consommable consommable = new Consommable(0, "Viande", nb, 0, 15);
        player.getInventaire().ajoutItem(consommable);
        return String.format("%s tué, Vous avez gagné ! +%dxp \nVous récupérez %d %s(s)\n", this.getName(), this.getXP(),nb,consommable.getNom());
    }
}