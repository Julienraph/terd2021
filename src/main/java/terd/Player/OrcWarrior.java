package terd.Player;

import terd.Map.Map;
import terd.Map.Pos;
import terd.item.Arme;

public class OrcWarrior extends AbstractMonster {
    public OrcWarrior(Pos pos, char skin) {
        super("Orc Warrior",pos, 0,0,skin, 100, 50);
        this.setMainWeapon(new Arme(0,"Massue",10,0,8));
    }

    @Override
    public void act(Pos posPlayer, Map map) {

    }
}

