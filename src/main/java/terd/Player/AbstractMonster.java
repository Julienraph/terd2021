package terd.Player;

import terd.Map.Pos;

public abstract class AbstractMonster extends AbstractProps implements Monster {
    public AbstractMonster(Pos pos, int posEtageY, int posEtageX, char skin, int pv) {
        super(pos,posEtageY,posEtageX,skin,pv);
    }
}
