package terd.etage;

import terd.Map.Map;
import terd.Player.Props;
import terd.utils.Seed;

import java.net.SocketTimeoutException;

public class Etage {
    private Map[][] tabMap;
    private int biome;
    private int niveau;
    private Seed seed;
    private int hauteurMap;
    private int largeurMap;
    private int hauteurEtage;
    private int largeurEtage;

    public Etage(int hauteurEtage, int largeurEtage, int hauteurMap, int largeurMap, int biome, int niveau, Seed seed) {
        this.tabMap = new Map[hauteurEtage][largeurEtage];
        this.hauteurMap = hauteurMap;
        this.largeurMap = largeurMap;
        this.hauteurEtage = hauteurEtage;
        this.largeurEtage = largeurEtage;
        this.biome = biome;
        this.niveau = niveau;
        this.seed = seed;
        for(int i=0;i<hauteurEtage;i++)
        {
            for(int y=0;y<largeurEtage;y++)
            {
                tabMap[i][y]=null;
            }
        }
        this.generationMapChemin();
        tabMap[0][0].spawnPlayer(0,0,'@'); //TODO pouvoir choisir le spawn à partir de je ne sais quelle classe
    }

    public void generationMapChemin(){
       // tabMap[0][0] = new Map(largeurMap,hauteurMap, seed);
        int x=0;
        int y=0;
        int incre=20;
        while ((x<hauteurEtage) && (y<largeurEtage))
        {
            tabMap[x][y] =  new Map(largeurMap,hauteurMap, seed);
            if(seed.getAnswer(incre)<8)
            {
                y++;
                System.out.print("y = ");
                System.out.println(y);
            }
            else
            {
                x++;
                System.out.print("x = ");
                System.out.println(x);
            }
            incre++;
        }
    }
   public void generationMapVoisine() {
        for(int i = 0; i < tabMap.length; i++) {
            for(int y = 0; y < tabMap[0].length; y++) {
                Seed seed = new Seed();
                tabMap[i][y] = new Map(largeurMap,hauteurMap, seed);
            }
        }
    }

    public void afficher(int x, int y) {
        StringBuilder sb = new StringBuilder();
        char[][] map = tabMap[x][y].getTableauMap();
        int i;
        int max = Math.max(map.length, hauteurEtage);
        for (i = 0; i < max; i++) {
            int j;
            if(i < map.length) {
                for (j = 0; j < map[0].length; j++) {
                    sb.append(map[i][j]);
                }
            }
            sb.append("       ");
            if (i == 0) {
                sb.append("Carte de l'étage :");
            }
            if(i < max && i > 0) {
                sb.append("         ");
                for (int k = 0; k < tabMap[0].length; k++) {
                    if (x == i - 1 && y == k) {
                        sb.append("[ @ ]");
                    } else {
                        if(tabMap[i-1][k]!=null) {
                            sb.append(String.format("[%d %d]", i - 1, k));
                        }
                        else{
                            sb.append(String.format("[X X]", i - 1, k));
                        }
                    }
                }
            }
            if(i < max - 1) {
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
        int ligneEtage = props.getPosEtageY();
        int colonneEtage = props.getPosEtageX();
        int posActuelX = props.getX();
        int posActuelY = props.getY();
        if((newPosY < 0 && ligneEtage > 0)) {
            if((tabMap[ligneEtage-1][colonneEtage]!=null)) {
                if (tabMap[ligneEtage - 1][colonneEtage].isValide(newPosX, hauteurMap - 1)) {
                    tabMap[ligneEtage][colonneEtage].resetCase(posActuelX, posActuelY);
                    tabMap[ligneEtage - 1][colonneEtage].moveProps(posActuelX, posActuelY, newPosX, hauteurMap - 1, skin);
                    props.setNewMap(ligneEtage - 1, colonneEtage);
                    props.setPosition(newPosX, hauteurMap - 1);
                    return true;
                } else {
                    return false;
                }
            }
            else {return false;}
        }
        if( (newPosY >= hauteurMap && ligneEtage < hauteurEtage - 1) ) {
            if((tabMap[ligneEtage+1][colonneEtage]!=null)) {
                if (tabMap[ligneEtage + 1][colonneEtage].isValide(newPosX, 0)) {
                    tabMap[ligneEtage][colonneEtage].resetCase(posActuelX, posActuelY);
                    tabMap[ligneEtage + 1][colonneEtage].moveProps(posActuelX, posActuelY, newPosX, 0, skin);
                    props.setNewMap(ligneEtage + 1, colonneEtage);
                    props.setPosition(newPosX, 0);
                    return true;
                } else {
                    return false;
                }
            }else{return false;}
        }
        if((newPosX < 0 && colonneEtage > 0)) {
            if((tabMap[ligneEtage][colonneEtage-1]!=null)) {
                if (tabMap[ligneEtage][colonneEtage - 1].isValide(largeurMap - 1, newPosY)) {
                    tabMap[ligneEtage][colonneEtage].resetCase(posActuelX, posActuelY);
                    tabMap[ligneEtage][colonneEtage - 1].moveProps(posActuelX, posActuelY, largeurMap - 1, newPosY, skin);
                    props.setNewMap(ligneEtage, colonneEtage - 1);
                    props.setPosition(largeurMap - 1, newPosY);
                    return true;
                } else {
                    return false;
                }
            }else{return false;}
        }
        if((newPosX >= largeurMap && colonneEtage < largeurEtage - 1)) {
            if((tabMap[ligneEtage][colonneEtage+1]!=null)) {
                if (tabMap[ligneEtage][colonneEtage + 1].isValide(0, newPosY)) {
                    tabMap[ligneEtage][colonneEtage].resetCase(posActuelX, posActuelY);
                    tabMap[ligneEtage][colonneEtage + 1].moveProps(posActuelX, posActuelY, 0, newPosY, skin);
                    props.setNewMap(ligneEtage, colonneEtage + 1);
                    props.setPosition(0, newPosY);
                    return true;
                } else {
                    return false;
                }
            }else{return false;}
        }
        if(newPosX < 0 || newPosY < 0 || newPosX >= largeurMap || newPosY >= hauteurMap) {
            return false;
        }
        if(posActuelY < hauteurMap && posActuelX < largeurMap) {
            if(tabMap[ligneEtage][colonneEtage].isValide(newPosX, newPosY)) {
                tabMap[ligneEtage][colonneEtage].moveProps(posActuelX, posActuelY, newPosX, newPosY, skin);
                props.setPosition(newPosX, newPosY);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
