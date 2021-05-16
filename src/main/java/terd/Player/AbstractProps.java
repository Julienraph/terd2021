package terd.Player;

import terd.Map.Pos;
import terd.item.Arme;
import terd.item.Competence;
import terd.item.Inventaire;

public abstract class AbstractProps implements Props {
    private int posEtageY;
    private int posEtageX;
    private int pv;
    private int maxPV;
    private int speed;
    private char skin;
    private char cache;
    private int xp;
    private final String name;
    private int levelProps;
    private Arme mainWeapon;
    private Competence mainCompetence;
    private Inventaire inventaire;
    private int credit;
    private Pos pos = new Pos(0,0);

    public AbstractProps(String name, Pos pos, int posEtageY, int posEtageX, char skin, int pv,int xp) {
        this.pos = pos;
        this.posEtageY = posEtageY;
        this.posEtageX = posEtageX;
        this.skin = skin;
        this.speed = 1;
        this.pv = pv;
        this.maxPV = pv;
        this.xp = xp;
        this.name = name;
        this.inventaire = new Inventaire();
    }

    public AbstractProps(String name, char skin, int pv) {
        this.posEtageY = 0;
        this.posEtageX = 0;
        this.speed = 1;
        this.skin = skin;
        this.pv = pv;
        this.maxPV = 100;
        this.name = name;
        this.inventaire = new Inventaire();
    }

    public void takeDamages(int damages) {
        if(pv - damages > maxPV) {
            pv = this.getMaxPV();
        }
        else if (pv - damages > 0) {
            pv -= damages;
        } else {
            pv = 0;
        }
    }

    public void useWeapon(Props victime) {
        victime.takeDamages(this.getMainWeapon().getDegat());
    }

    public double getDistance(Props props) {
        double dx = pos.getX() - props.getX();
        double dy = pos.getY() - props.getY();
        return Math.hypot(dx, dy);
    }

    public double getDistance(Pos posProps, Pos posProps2) {
        double dx = posProps2.getX() - posProps.getX();
        double dy = posProps2.getY() - posProps.getY();
        return Math.hypot(dx, dy);
    }

    public void setPosX(int x){
        pos.setX(x);
    }

    public void setPosY(int y){
        pos.setY(y);
    }

    public Arme getMainWeapon() {
        return mainWeapon;
    }

    public void setMainWeapon(Arme mainWeapon) {
        this.mainWeapon = mainWeapon;
    }

    public void setNewMap(int newPosY, int newPosX) {
        this.posEtageY = newPosY;
        this.posEtageX = newPosX;
    }

    public void setPosition(int newPosX, int newPosY) {
        this.setPosX(newPosX);
        this.setPosY(newPosY);
    }

    public String addXP(int monsterXP) {
        String message = "";
        if((xp + monsterXP) >= 100) {
            credit += (xp + monsterXP) / 100;
            message = String.format("Vous avez level-up ! Vous gagnez +%d crédit(s)\n", (xp + monsterXP) / 100);
        }
        levelProps += (xp + monsterXP) / 100;
        xp = (xp + monsterXP) % 100;
        return message;
    }

    public boolean isBeside(Pos posProps) {
        return pos.getX() == posProps.getX() + 1 && pos.getY() == posProps.getY()
                || pos.getX() == posProps.getX() - 1 && pos.getY() == posProps.getY()
                || pos.getY() == posProps.getY() + 1 && pos.getX() == posProps.getX()
                || pos.getY() == posProps.getY() - 1 && pos.getX() == posProps.getX();
    }

    @Override
    public int getX() {
        return pos.getX();
    }


    @Override
    public int getY() {
        return pos.getY();
    }

    public int getPosEtageY() {
        return posEtageY;
    }

    public char getSkin() {
        return skin;
    }

    public void setSkin(char skin) {
        this.skin = skin;
    }

    public int getPV() {
        return pv;
    }

    public String getName() {
        return name;
    }

    public int getMaxPV() {
        return maxPV;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPosEtageX() {
        return posEtageX;
    }

    public int getLevelProps() {
        return levelProps;
    }

    public void addPV(int pv){
        this.pv += pv;
    }

    public void setLevelProps(int levelProps) {
        this.levelProps = levelProps;
    }

    public Competence getMainCompetence() {
        return mainCompetence;
    }

    public void setMainCompetence(Competence mainCompetence) {
        this.mainCompetence = mainCompetence;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    public char getCache() {
        return cache;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getXP() {
        return xp;
    }

    public void setXP(int xp) {
        this.xp = xp;
    }

    public void setCache(char cache) {
        this.cache = cache;
    }
}
