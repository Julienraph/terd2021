package terd.Player;

import terd.Map.Pos;
import terd.item.Arme;

public class OrcWarrior extends AbstractMonster {
    public OrcWarrior(Pos pos, char skin) {
        super("Orc Warrior",pos, 0,0,skin, 100);
        this.setMainWeapon(new Arme(0,"Massue",10,0,5));
    }

    @Override
    public void act(Player player) {

    }
}

