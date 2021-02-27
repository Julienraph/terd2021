package terd.Player;

public abstract class Props implements Position {
    private int posX;
    private int posY;
    private int posXetage;
    private int posYetage;

    public Props(int posX, int posY, int posXetage, int posYetage, char skin) {
        this.posX = posX;
        this.posY = posY;
        this.posXetage = posXetage;
        this.posYetage = posYetage;
        this.skin = skin;
    }

    private char skin;

    public void setPosX(int x){
        posX = x;
    }

    public void setPosY(int y){
        posY = y;
    }

    public void setNewMap(int newPosX, int newPosY) {
        this.posXetage = newPosX;
        this.posYetage = newPosY;
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

    public int getPosXetage() {
        return posXetage;
    }

    public char getSkin() {
        return skin;
    }

    public int getPosYetage() {
        return posYetage;
    }


}
