package terd.Player;

public abstract class AbstractMonster extends AbstractProps implements Monster {
    public AbstractMonster(int x, int y, int posEtageY, int posEtageX, char skin, int pv) {
        super(x,y,posEtageY,posEtageX,skin,pv);
    }
}
