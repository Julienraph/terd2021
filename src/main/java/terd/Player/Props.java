package terd.Player;

public abstract class Props implements Position {
    private int posX;
    private int posY;

    public void addPosX(int x){
        posX += x;
    }

    public void addPosY(int y){
        posY += y;
    }

    @Override
    public int getX() {
        return posX;
    }

    @Override
    public int getY() {
        return posY;
    }
}
