package terd.Player;

import terd.item.Arme;

public abstract class AbstractProps implements Props {
    private int posX;
    private int posY;
    private int posEtageY;
    private int posEtageX;
    private int pv;
    private int maxPV;
    private int speed;
    private char skin;
    private int levelProps;
    private Arme mainWeapon;

    public AbstractProps(int posX, int posY, int posEtageY, int posEtageX, char skin, int pv) {
        this.posX = posX;
        this.posY = posY;
        this.posEtageY = posEtageY;
        this.posEtageX = posEtageX;
        this.skin = skin;
        this.speed = 1;
        this.pv = pv;
        this.maxPV = pv;
    }

    public AbstractProps(char skin, int pv) {
        this.posX = 0;
        this.posY = 0;
        this.posEtageY = 0;
        this.posEtageX = 0;
        this.speed = 1;
        this.skin = skin;
        this.pv = pv;
        this.maxPV = pv;
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
        double dx = posX - abstractProps.getX();
        double dy = posY - abstractProps.getY();
        return Math.hypot(dx, dy);
    }

    public void setPosX(int x){
        posX = x;
    }

    public void setPosY(int y){
        posY = y;
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
        return posX;
    }

    @Override
    public int getY() {
        return posY;
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
}
