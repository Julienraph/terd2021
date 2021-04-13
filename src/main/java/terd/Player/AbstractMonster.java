package terd.Player;

import terd.Map.Pos;

public abstract class AbstractMonster extends AbstractProps implements Monster {
    public AbstractMonster(String name, Pos pos, int posEtageY, int posEtageX, char skin, int pv) {
        super(name,pos,posEtageY,posEtageX,skin,pv);
    }
}
