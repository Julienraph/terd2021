package terd.etage;

import terd.Map.Coordonne;
import terd.Map.Map;
import terd.Player.Player;
import terd.Player.Props;
import terd.utils.MapColor;
import terd.utils.Seed;

public class Etage {
    private Map[][] tabMap;
    private int biome;
    private int niveau;
    private Seed seed;
    private int hauteurMap;
    private int largeurMap;
    private int hauteurEtage;
    private int largeurEtage;
    private int coefHauteurMap;
    private int coefLargeurMap;

    public Etage(int hauteurEtage, int largeurEtage, int coefHauteurMap, int coefLargeurMap, int biome, int niveau, Seed seed) {
        this.tabMap = new Map[hauteurEtage][largeurEtage];
        this.coefHauteurMap = coefHauteurMap;
        this.coefLargeurMap = coefLargeurMap;
        this.hauteurMap = 15 + coefHauteurMap + 2 + 1;
        this.largeurMap = 15 + coefLargeurMap + 2 + 1;
        this.hauteurEtage = hauteurEtage;
        this.largeurEtage = largeurEtage;
        this.biome = biome;
        this.niveau = niveau;
        this.seed = seed;
        this.generationMapVoisine();
        this.generationPont();
    }

    public void generationMapVoisine() {
        for (int i = 0; i < tabMap.length; i++) {
            for (int y = 0; y < tabMap[0].length; y++) {
                Seed seed = new Seed();
                if (i == 0 && y < tabMap[0].length - 1) {
                    tabMap[i][y] = new Map(coefHauteurMap, coefLargeurMap, seed, 10);
                } else if (i == 1 && y < tabMap[0].length - 1) {
                    tabMap[i][y] = new Map(coefHauteurMap, coefLargeurMap, seed, 1010);
                } else {
                    tabMap[i][y] = new Map(coefHauteurMap, coefLargeurMap, seed, 0);
                }
            }
        }
    }

    public void spawnPlayer(Player player, int ligne, int colonne) {
        tabMap[ligne][colonne].spawnPlayer(player.getSkin());
        player.setPosX(this.getMap(ligne, colonne).spawnPlayer(player.getSkin()).getY());
        player.setPosY(this.getMap(ligne, colonne).spawnPlayer(player.getSkin()).getX());
        player.setNewMap(ligne, colonne);
        player.setEtageActuel(this);
    }

    public int getHauteurEtage() {
        return hauteurEtage;
    }

    public int getLargeurEtage() {
        return largeurEtage;
    }

    //Connecte les sorties avec la carte suivante
    public void generationPont() {
        for (int i = 0; i < tabMap.length; i++) {
            for (int y = 0; y < tabMap[0].length; y++) {
                Map map = tabMap[i][y];
                if (map != null) {
                    if (map.getHaut() != null) {
                        int hauteur = hauteurMap - 1;
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
                        int largeur = largeurMap - 1;
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

    public Boolean moveProps(Props props, int newPosX, int newPosY, char skin) {
        int ligneEtage = props.getPosEtageY();
        int colonneEtage = props.getPosEtageX();
        int posActuelX = props.getX();
        int posActuelY = props.getY();
        boolean isVertical = (newPosY < 0 && ligneEtage > 0) || (newPosY >= hauteurMap && ligneEtage < hauteurEtage - 1);
        boolean isHorizontal = (newPosX < 0 && colonneEtage > 0) || (newPosX >= largeurMap && colonneEtage < largeurEtage - 1);
        //Si le joueur essaye de sortir de la map
        if (isHorizontal || isVertical) {
            int directionVertical = isVertical ? (int) Math.signum(newPosY) : 0;
            int directionHorizontal = isHorizontal ? (int) Math.signum(newPosX) : 0;
            int spawnLigne = (directionVertical == 0) ? newPosY : ((directionVertical > 0) ? 0 : hauteurMap - 1);
            int spawnColonne = (directionHorizontal == 0) ? newPosX : ((directionHorizontal > 0) ? 0 : largeurMap - 1);
            if ((tabMap[ligneEtage + directionVertical][colonneEtage + directionHorizontal] != null)) {
                if (tabMap[ligneEtage + directionVertical][colonneEtage + directionHorizontal].isValide(spawnColonne, spawnLigne)) {
                    tabMap[ligneEtage][colonneEtage].resetCase(posActuelX, posActuelY);
                    tabMap[ligneEtage + directionVertical][colonneEtage + directionHorizontal].moveProps(spawnColonne, spawnLigne, spawnColonne, spawnLigne, skin);
                    props.setNewMap(ligneEtage + directionVertical, colonneEtage + directionHorizontal);
                    props.setPosition(spawnColonne, spawnLigne);
                    return true;
                }
            }
        }
        if (newPosX < 0 || newPosY < 0 || newPosX >= largeurMap || newPosY >= hauteurMap) {
            return false;
        }
        //Si le joueur se déplace à l'intérieur de la salle
        if (posActuelY < hauteurMap && posActuelX < largeurMap) {
            if (tabMap[ligneEtage][colonneEtage].isValide(newPosX, newPosY)) {
                tabMap[ligneEtage][colonneEtage].moveProps(posActuelX, posActuelY, newPosX, newPosY, skin);
                props.setPosition(newPosX, newPosY);
                return true;
            }
        }
        return false;
    }


    public void afficherMap(int x, int y) {
        StringBuilder sb = new StringBuilder();
        char[][] map = tabMap[x][y].getTableauMap();
        int positionCarte = 10;
        int positionEcritCarte = positionCarte - 2;
        int positionTuto = map.length - 5;
        int positionAfficherCarte = map.length - 4;
        int positionTP = map.length - 3;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (MapColor.getMapColorForSymbol(map[i][j]) == null) {
                    throw new EnumConstantNotPresentException(MapColor.class, String.valueOf(map[i][j]));
                }
                sb.append(MapColor.getMapColorForSymbol(map[i][j]).getColoredString());
                //sb.append(map[i][j]);
            }
            sb.append("       ");
            if (i == positionEcritCarte) {
                sb.append("Carte de l'étage :");
            }
            if (i >= positionCarte && i < positionCarte + hauteurEtage) {
                sb.append("         ");
                for (int k = 0; k < tabMap[0].length; k++) {
                    if (x == i - positionCarte && y == k) {
                        sb.append("[ @ ]");
                    } else {
                        if (tabMap[i - positionCarte][k] != null) {
                            sb.append(String.format("[%d %d]", i - positionCarte, k));
                        } else {
                            sb.append(String.format("[X X]", i - positionCarte, k));
                        }
                    }
                }
            }
            if (i == positionTuto) {
                sb.append("- ZQSD pour se déplacer / X pour arrêter le jeu");
            }
            if (i == positionAfficherCarte) {
                sb.append("- M pour afficher la carte");
            }
            if (i == positionTP) {
                sb.append("- T pour se téléporter");
            }
            if (i < map.length - 1) {
                sb.append("\n");
            }

        }
        System.out.println(sb.toString());
    }


    public void afficherCarte(int x, int y) {
        char[][] map = tabMap[x][y].getTableauMap();
        int nbHorizontalMap = largeurEtage;
        int nbVerticalMap = hauteurEtage;
        int hauteurMap = map.length;
        int largeurMap = map[0].length;
        String delimitation = new String(new char[nbHorizontalMap * largeurMap]).replace('\0', '-');
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hauteurMap * nbVerticalMap; i++) {
            for (int j = 0; j < largeurMap * nbHorizontalMap; j++) {
                map = ((i % hauteurMap == 0) || (j % largeurMap == 0)) ? tabMap[x + i / hauteurMap][y + j / largeurMap].getTableauMap() : map;
                sb.append(map[i % hauteurMap][j % largeurMap]);
            }
            sb.append("\n");
        }
        System.out.println("\n\n");
        System.out.println(delimitation);
        System.out.println(sb.toString());
        System.out.println(delimitation);
    }

    public Map[][] getEtage() {
        return tabMap;
    }

    public Map getMap(int x, int y) {
        return tabMap[x][y];
    }

}
