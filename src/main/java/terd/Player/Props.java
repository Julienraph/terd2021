package terd.Player;

public abstract class Props implements Position {
    private int posX;
    private int posY;
    private int posEtageY;
    private int posEtageX;

    public Props(int posX, int posY, int posEtageY, int posEtageX, char skin) {
        this.posX = posX;
        this.posY = posY;
        this.posEtageY = posEtageY;
        this.posEtageX = posEtageX;
        this.skin = skin;
    }

    public Props(char skin) {
        this.posX = 0;
        this.posY = 0;
        this.posEtageY = 0;
        this.posEtageX = 0;
        this.skin = skin;
    }

    private char skin;

    public void setPosX(int x){
        posX = x;
    }

    public void setPosY(int y){
        posY = y;
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

    public int getPosEtageX() {
        return posEtageX;
    }


}
