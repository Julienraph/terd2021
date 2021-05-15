package terd.Player;

import terd.Map.Map;
import terd.Map.Pos;

import java.util.Random;

public abstract class AbstractMonster extends AbstractProps implements Monster {
    int casePrecedent;

    public AbstractMonster(String name, Pos pos, int posEtageY, int posEtageX, char skin, int pv, int xp) {
        super(name,pos,posEtageY,posEtageX,skin,pv, xp);
    }

    public String recompensePlayer(Player player) {
        return String.format("%s tué, Vous avez gagné ! +%dxp\n", this.getName(), this.getXP());
    }

    public void randomMove(Map map) {
        Random random = new Random();
        int direction = random.nextInt(4);
        boolean hasMove = false;
        int boucle = 0;
       while(!hasMove && boucle < 30){
            if (direction == 0 && casePrecedent != 1) {
                hasMove = map.moveProps(this, this.getX(), this.getY() + 1);
                casePrecedent = 0;
            }
            if (direction == 1 && casePrecedent != 0) {
                hasMove = map.moveProps(this, this.getX(), this.getY() - 1);
                casePrecedent = 1;
            }
            if (direction == 2 && casePrecedent != 3) {
                hasMove = map.moveProps(this, this.getX() + 1, this.getY());
                casePrecedent = 2;
            }
            if (direction == 3 && casePrecedent != 2) {
                hasMove = map.moveProps(this, this.getX() - 1, this.getY());
                casePrecedent = 3;
            }
            boucle += 1;
            direction = random.nextInt(4);
        }
    }
}
