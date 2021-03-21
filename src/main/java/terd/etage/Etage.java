package terd.etage;

import terd.Map.Coordonne;
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
        this.generationMapVoisine();
        generationPont();
        tabMap[0][0].spawnPlayer(20,20,'@'); //TODO pouvoir choisir le spawn à partir de je ne sais quelle classe
    }

    public void generationMapVoisine() {
        for(int i = 0; i < tabMap.length; i++) {
            for(int y = 0; y < tabMap[0].length; y++) {
                Seed seed = new Seed();
                if (i == 0 && y < tabMap[0].length - 1) {
                    tabMap[i][y] = new Map(largeurMap,hauteurMap, seed, 10);
                } else {
                    tabMap[i][y] = new Map(largeurMap, hauteurMap, seed, 0);
                }
            }
        }
    }

    public void generationPont() {
        for(int i = 0; i < tabMap.length; i++) {
            for(int y = 0; y < tabMap[0].length; y++) {
                Map map = tabMap[i][y];
                if(map != null) {
                    if (map.getHaut() != null) {
                        int hauteur = hauteurMap;
                        int largeur = map.getHaut().getY();
                        Coordonne pos = new Coordonne(hauteur, largeur);
                        tabMap[i - 1][y].creationCheminDepuisExte(pos);
                    }
                    if (map.getBas() != null) {
                        int hauteur = 0;
                        int largeur = map.getBas().getY();
                        Coordonne pos = new Coordonne(hauteur, largeur);
                        tabMap[i + 1][y].creationCheminDepuisExte(pos);
                    }
                    if (map.getGauche() != null) {
                        int hauteur = map.getGauche().getX();
                        int largeur = largeurMap;
                        Coordonne pos = new Coordonne(hauteur, largeur);
                        tabMap[i][y - 1].creationCheminDepuisExte(pos);
                    }
                    if (map.getDroite() != null) {
                        int hauteur = map.getDroite().getX();
                        int largeur = 0;
                        Coordonne pos = new Coordonne(hauteur, largeur);
                        tabMap[i][y + 1].creationCheminDepuisExte(pos);
                    }
                }
            }
        }
    }

    public void afficher(int x, int y) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        char[][] map = tabMap[x][y].getTableauMap();
        int i;
        System.out.println(String.format("max : %d", map.length));
        for (i = 0; i < map.length; i++) {
            int j;
            for (j = 0; j < map[0].length; j++) {
                sb.append(map[i][j]);
            }

            sb.append("       ");
            if (i == 0) {
                sb.append("Carte de l'étage :");
            }
            if(i < hauteurEtage && i > 0) {
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
            if(i == hauteurEtage + 2) {
                sb.append("ZQSD pour se déplacer / X pour arrêter");
            }
            if(i < map.length - 1) {
                sb.append("\n");
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
            System.out.println(newPosX);
            System.out.println(newPosY);
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
