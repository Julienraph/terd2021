package terd.Player;

import terd.Map.Map;
import terd.Map.Pos;
import terd.item.Arme;
import terd.item.Consommable;

import java.util.Random;

public class Cerf extends AbstractMonster {
    Pos cache = new Pos(-1,-1);
    public Cerf(Pos pos, char skin) {
        super("Cerf",pos, 0,0,skin, 100, 40);
        this.setMainWeapon(new Arme(0,"Coup de Bois",10,0,5));
    }

    @Override
    public String recompensePlayer(Player player) {
        Random random = new Random();
        int nb = 1 + random.nextInt(2);
        Consommable consommable = new Consommable(0,"Viande",nb,0,15);
        player.getInventaire().ajoutItem(consommable);
        return String.format("%s tué, Vous avez gagné ! +%dxp \nVous récupérez %d %s(s)\n", this.getName(),this.getXP(),nb,consommable.getNom());
    }

    private boolean bloquer(int x, int y, Pos posPlayer, Map map) {
        int nbCasevalide = 0;
        if(posPlayer.getY() == this.getY() || posPlayer.getX() == this.getX()) {
            if(!map.isValide(x+1,y)) {
                nbCasevalide += 1;
            }
            if(!map.isValide(x+1,y+1)) {
                nbCasevalide += 1;
            }
            if(!map.isValide(x,y+1)) {
                nbCasevalide += 1;
            }
            if(!map.isValide(x-1,y+1)) {
                nbCasevalide += 1;
            }
            if(!map.isValide(x-1,y)) {
                nbCasevalide += 1;
            }
            if(!map.isValide(x-1,y-1)) {
                nbCasevalide += 1;
            }
            if(!map.isValide(x,y-1)) {
                nbCasevalide += 1;
            }
            if(!map.isValide(x+1,y-1)) {
                nbCasevalide += 1;
            }
        }
        return nbCasevalide > 5;
    }

    //Déplacement à corriger, fuit plus ou moins
    @Override
    public void act(Pos posPlayer, Map map) {
        int directionX = (int) Math.signum(this.getX() - posPlayer.getX());
        int directionY = (int) Math.signum(this.getY() - posPlayer.getY());
        Random random = new Random();
        Pos pos = this.cache;
        int boucle = 0;
        Pos tmp = new Pos(0, 0);
        boolean hasMove = false;
        if(bloquer(this.getX(),this.getY(),posPlayer, map)) {

            map.moveProps(this, this.getX() + directionX, this.getY());
            map.moveProps(this, this.getX(), this.getY() + directionY);
            return;
        }
        while(true) {

                int choix = random.nextInt(2);
                if (choix == 1) {
                    if (!cache.equals(new Pos(this.getX() + directionX, this.getY()))) {
                        if (map.moveProps(this, this.getX() + directionX, this.getY())) {
                            pos = new Pos(this.getX(), this.getY());
                            tmp = new Pos(this.getX() - directionX, this.getY());
                            hasMove = true;
                            break;
                        }
                    }

                    if (!cache.equals(new Pos(this.getX(), this.getY() + directionY))) {
                        if (map.moveProps(this, this.getX(), this.getY() + directionY)) {
                            pos = new Pos(this.getX(), this.getY());
                            tmp = new Pos(this.getX(), this.getY() - directionY);
                            hasMove = true;
                            break;

                        }
                    }
                } else {
                    if (!cache.equals(new Pos(this.getX(), this.getY() + directionY))) {
                        if (map.moveProps(this, this.getX(), this.getY() + directionY)) {
                            pos = new Pos(this.getX(), this.getY());
                            tmp = new Pos(this.getX(), this.getY() - directionY);
                            hasMove = true;
                            break;
                        }
                    }
                    if (!cache.equals(new Pos(this.getX() + directionX, this.getY()))) {
                        if (map.moveProps(this, this.getX() + directionX, this.getY())) {
                            pos = new Pos(this.getX(), this.getY());
                            tmp = new Pos(this.getX() - directionX, this.getY());
                            hasMove = true;
                            break;
                        }
                    }
                }
                break;
            }
            if(!hasMove) {
                while(!hasMove) {
                    boucle += 1;
                    int direction = new Random().nextInt(4);
                    if (direction == 0 && !cache.equals(new Pos(this.getX(), this.getY()+1))) {
                        hasMove = map.moveProps(this, this.getX(), this.getY() + 1);
                        tmp = new Pos(this.getX(), this.getY() - 1);
                    }
                    if (direction == 1 && !cache.equals(new Pos(this.getX(), this.getY()-1))) {
                        hasMove = map.moveProps(this, this.getX(), this.getY() - 1);
                        tmp = new Pos(this.getX(), this.getY() + 1);
                    }
                    if (direction == 2 && !cache.equals(new Pos(this.getX() + 1, this.getY()))) {
                        hasMove = map.moveProps(this, this.getX() + 1, this.getY());
                        tmp = new Pos(this.getX() - 1, this.getY());
                    }
                    if (direction == 3 && !cache.equals(new Pos(this.getX()-1, this.getY()))) {
                        hasMove = map.moveProps(this, this.getX() - 1, this.getY());
                        tmp = new Pos(this.getX()+1, this.getY());
                    }
                    if(boucle > 20) {
                        if (direction == 0) {
                            hasMove = map.moveProps(this, this.getX(), this.getY() + 1);
                            tmp = new Pos(this.getX(), this.getY() - 1);
                        }
                        if (direction == 1) {
                            hasMove = map.moveProps(this, this.getX(), this.getY() - 1);
                            tmp = new Pos(this.getX(), this.getY() + 1);
                        }
                        if (direction == 2) {
                            hasMove = map.moveProps(this, this.getX() + 1, this.getY());
                            tmp = new Pos(this.getX() - 1, this.getY());
                        }
                        if (direction == 3) {
                            hasMove = map.moveProps(this, this.getX() - 1, this.getY());
                            tmp = new Pos(this.getX()+1, this.getY());
                        }
                    }
                }
            }
            cache = tmp;
        }

}

