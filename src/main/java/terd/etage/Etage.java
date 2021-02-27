package terd.etage;

import terd.Map.Map;
import terd.Player.Props;
import terd.utils.Seed;

public class Etage {
    private Map[][] tabMap;
    private int biome;
    private int niveau;
    private Seed seed;
    private int tailleMapX;
    private int tailleMapY;
    private int tailleEtageX;
    private int tailleEtageY;

    public Etage(int tailleEtageX , int tailleEtageY, int tailleMapX, int tailleMapY, int biome, int niveau, Seed seed) {
        this.tabMap = new Map[tailleEtageX][tailleEtageY];
        this.tailleMapX = tailleMapX;
        this.tailleMapY = tailleMapY;
        this.tailleEtageX = tailleEtageX;
        this.tailleEtageY = tailleEtageY;
        this.biome = biome;
        this.niveau = niveau;
        this.seed = seed;
        this.generationMapVoisine();
        tabMap[0][0].spawnPlayer(0,0,'@'); //TODO pouvoir choisir le spawn à partir de je ne sais quelle classe
    }

    public void generationMapVoisine() {
        for(int i = 0; i < tabMap.length; i++) {
            for(int y = 0; y < tabMap[0].length; y++) {
                Seed seed = new Seed();
                tabMap[i][y] = new Map(tailleMapX, tailleMapY, seed);
            }
        }
    }

    public void afficher(int x, int y) {
        StringBuilder sb = new StringBuilder();
        char[][] map = tabMap[x][y].getTableauMap();
        int i;
        for (i = 0; i < map.length; i++) {
            int j;
            for (j = 0; j < map[0].length; j++) {
                sb.append(map[i][j]);
            }
            sb.append("       ");
            if (i == 0) {
                sb.append("Carte de l'étage :");
            }
            if(i <= tabMap.length && i > 0) {
                sb.append("         ");
                for (int k = 0; k < tabMap[0].length; k++) {
                    if (x == i - 1 && y == k) {
                        sb.append("[ @ ]");
                    } else {
                        sb.append(String.format("[%d %d]", i - 1, k));
                    }
                }
            }
            if(i < map.length - 1) {
                sb.append("\n");
            } else {
                sb.append("ZQSD pour se déplacer / X pour arrêter");
            }
        }
        System.out.println(sb.toString());
    }

    public Map[][] getEtage() {
        return tabMap;
    }

    public Map getMap(int x, int y) {
        return tabMap[x][y];
    }

    public Boolean moveProps(Props props, int newPosX, int newPosY, char skin)
    {
        int xMap = props.getPosXetage();
        int yMap = props.getPosYetage();
        int posActuelX = props.getX();
        int posActuelY = props.getY();
        if(newPosX < 0 && xMap > 0) {
            if(tabMap[xMap - 1][yMap].isValide(tailleMapX - 1, newPosY)) {
                tabMap[xMap][yMap].resetCase(posActuelX, posActuelY);
                tabMap[xMap - 1][yMap].moveProps(posActuelX, posActuelY, tailleMapX - 1, newPosY, skin);
                props.setNewMap(xMap - 1, yMap);
                props.setPosition(tailleMapX - 1, newPosY);
                return true;
            } else {
                return false;
            }
        }
        if(newPosX == tailleMapX && xMap < tailleEtageX - 1) {
            if(tabMap[xMap + 1][yMap].isValide(0, newPosY)) {
                tabMap[xMap][yMap].resetCase(posActuelX, posActuelY);
                tabMap[xMap + 1][yMap].moveProps(posActuelX, posActuelY, 0, newPosY, skin);
                props.setNewMap(xMap + 1, yMap);
                props.setPosition(0, newPosY);
                return true;
            } else {
                return false;
            }
        }
        if(newPosY < 0 && yMap > 0) {
            if(tabMap[xMap][yMap - 1].isValide(newPosX, tailleMapY - 1)) {
                tabMap[xMap][yMap].resetCase(posActuelX, posActuelY);
                tabMap[xMap][yMap - 1].moveProps(posActuelX, posActuelY, newPosX, tailleMapY - 1, skin);
                props.setNewMap(xMap, yMap - 1);
                props.setPosition(newPosX, tailleMapY - 1);
                return true;
            } else {
                return false;
            }
        }
        if(newPosY == tailleMapY && yMap < tailleEtageY - 1) {
            if(tabMap[xMap][yMap + 1].isValide(newPosX, 0)) {
                tabMap[xMap][yMap].resetCase(posActuelX, posActuelY);
                tabMap[xMap][yMap + 1].moveProps(posActuelX, posActuelY, newPosX, 0, skin);
                props.setNewMap(xMap, yMap + 1);
                props.setPosition(newPosX, 0);
                return true;
            } else {
                return false;
            }
        }
        if(newPosX < 0 || newPosY < 0 || newPosX == tailleMapX || newPosY == tailleMapY ) {
            return false;
        }
        if(posActuelX < tailleMapX && posActuelY < tailleMapY) {
            if(tabMap[xMap][yMap].isValide(newPosX, newPosY)) {
                tabMap[xMap][yMap].moveProps(posActuelX, posActuelY, newPosX, newPosY, skin);
                props.setPosition(newPosX, newPosY);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
