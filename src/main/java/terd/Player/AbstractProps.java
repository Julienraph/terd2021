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
    private int levelProps;
    private Arme mainWeapon;
    private Competence mainCompetence;
    private Inventaire inventaire;
    private Pos pos = new Pos(0,0);

    public AbstractProps(Pos pos, int posEtageY, int posEtageX, char skin, int pv) {
        this.pos = pos;
        this.posEtageY = posEtageY;
        this.posEtageX = posEtageX;
        this.skin = skin;
        this.speed = 1;
        this.pv = pv;
        this.maxPV = pv;
        this.inventaire = new Inventaire();
    }

    public AbstractProps(char skin, int pv) {
        this.posEtageY = 0;
        this.posEtageX = 0;
        this.speed = 1;
        this.skin = skin;
        this.pv = pv;
        this.maxPV = pv;
        this.inventaire = new Inventaire();
    }

    public void takeDamages(int damages) {
        if(pv - damages > 0) {
            pv -= damages;
        } else {
            /* TODO : gérer la mort */
        }
    }

    public void useWeapon(Props victime) {
        victime.takeDamages(this.getMainWeapon().getDegat());
    }

    public double getDistance(AbstractProps abstractProps) {
        double dx = pos.getX() - abstractProps.getX();
        double dy = pos.getY() - abstractProps.getY();
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

    public int getPv() {
        return pv;
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
}
