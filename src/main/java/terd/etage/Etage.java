package terd.etage;


import terd.Map.Pos;
import terd.Map.Map;
import terd.Player.OrcWarrior;
import terd.Player.Player;
import terd.Player.Props;
import terd.utils.Generator;
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
    private int spawnLigne;
    private int spawnColonne;

    public Etage(int hauteurEtage, int largeurEtage, int coefHauteurMap, int coefLargeurMap, int biome, int niveau, Seed seed) {
        this.hauteurEtage = hauteurEtage;
        this.largeurEtage = largeurEtage;
        this.biome = biome;
        this.niveau = niveau;
        this.seed = seed;
        Generator generator = new Generator(seed, largeurEtage, hauteurEtage, coefLargeurMap, coefHauteurMap);
        tabMap = generator.getMaps();
        this.hauteurMap = generator.tailleReelX;
        this.largeurMap = generator.tailleReelY;
        this.spawnColonne = generator.spawnColonne;
        this.spawnLigne = generator.spawnLigne;
        affichage();
        this.generationPont();
    }

    public void spawnPlayer(Player player, int ligne, int colonne) {
        tabMap[ligne][colonne].spawnPlayer(player);
        player.setPosX(this.getMap(ligne, colonne).spawnPlayer(player).getY());
        player.setPosY(this.getMap(ligne, colonne).spawnPlayer(player).getX());
        player.setNewMap(ligne, colonne);
        player.setEtageActuel(this);
    }

    public int getHauteurEtage() {
        return hauteurEtage;
    }

    public int getLargeurEtage() {
        return largeurEtage;
    }

    ///Connecte les sorties avec la carte suivante
    public void generationPont() {
        for (int i = 0; i < tabMap.length; i++) {
            for (int y = 0; y < tabMap[0].length; y++) {
                Map map = tabMap[i][y];
                if(i==spawnLigne && y==spawnColonne){map.creationCheminInterne(map.getSpawnPos(),map.getPosSortie());}
                if (map != null) {
                    if (map.getHaut() != null) {
                        int hauteur = hauteurMap - 1;
                        int largeur = map.getHaut().getY();
                        Pos pos = new Pos(hauteur, largeur);
                        tabMap[i - 1][y].creationCheminDepuisExte(pos);
                    }
                    if (map.getBas() != null) {
                        int hauteur = 0;
                        int largeur = map.getBas().getY();
                        Pos pos = new Pos(hauteur, largeur);
                        tabMap[i + 1][y].creationCheminDepuisExte(pos);
                    }
                    if (map.getGauche() != null) {
                        int hauteur = map.getGauche().getX();
                        int largeur = largeurMap - 1;
                        Pos pos = new Pos(hauteur, largeur);
                        tabMap[i][y - 1].creationCheminDepuisExte(pos);
                    }
                    if (map.getDroite() != null) {
                        int hauteur = map.getDroite().getX();
                        int largeur = 0;
                        Pos pos = new Pos(hauteur, largeur);
                        tabMap[i][y + 1].creationCheminDepuisExte(pos);
                    }
                }
            }
        }
    }


    public void affichage() {
        //     System.out.println(length);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < hauteurEtage; i++) {
            for(int j = 0; j < largeurEtage; j++) {
                if (tabMap[i][j] != null) {
                    sb.append(String.format("[%d]", tabMap[i][j].getSortie()));
                } else {
                    sb.append("[X X]");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public Boolean moveProps(Props props, int newPosX, int newPosY, char skin)
    {
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
                    tabMap[ligneEtage][colonneEtage].resetCase(posActuelX, posActuelY,props);
                    tabMap[ligneEtage + directionVertical][colonneEtage + directionHorizontal].moveProps(spawnColonne, spawnLigne, props);
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
                tabMap[ligneEtage][colonneEtage].resetCase(posActuelX, posActuelY,props);
                tabMap[ligneEtage][colonneEtage].moveProps(newPosX, newPosY, props);
                props.setPosition(newPosX, newPosY);
                return true;
            }
        }
        return false;
    }


    public void afficherMap(int x, int y, Player player) {
        StringBuilder sb = new StringBuilder();
        char[][] map = tabMap[x][y].getTableauMap();
        int positionCarte = 2;
        int positionEcritCarte = positionCarte - 2;
        int positionTuto = positionCarte + 1 + hauteurEtage;
        int positionAfficherCarte = positionTuto + 1;
        int positionTP = positionAfficherCarte + 1;
        int positionXP = positionTP + 2;
        int positionLevel = positionXP + 1;
        int positionPV = positionLevel + 1;
        int positionArme = positionPV + 1;
        int positionCompetence = positionArme + 1;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (MapColor.getMapColorForSymbol(map[i][j]) == null) {
                    throw new EnumConstantNotPresentException(MapColor.class, String.valueOf(map[i][j]));
                }
                sb.append(MapColor.getMapColorForSymbol(map[i][j]).getColoredString());
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
            if (i == positionXP) {
                sb.append(String.format("XP : %d", player.getXP()));
            }
            if (i == positionLevel) {
                sb.append(String.format("Level : %d", player.getLevelProps()));
            }
            if (i == positionPV) {
                sb.append(String.format("PV : %d", player.getPv()));
            }
            if (i == positionArme) {
                sb.append(String.format("Arme Principale : %s", player.getMainWeapon().getNom()));
            }
            if (i == positionCompetence) {
                sb.append(String.format("Competence Principale : %s", player.getMainCompetence().getNom()));
            }
            if (i < map.length - 1) {
                sb.append("\n");
            }

        }
        System.out.println(sb.toString());
    }


    public void afficherCarte(int x, int y) {
        char[][] map;
        if(tabMap[x][y] == null) {
            map = new char[hauteurMap][largeurMap];
        } else {
            map = tabMap[x][y].getTableauMap();
        }
        int nbHorizontalMap = largeurEtage;
        int nbVerticalMap = hauteurEtage;
        int hauteurMap = map.length;
        int largeurMap = map[0].length;
        String delimitation = new String(new char[nbHorizontalMap * largeurMap]).replace('\0', '-');
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hauteurMap * nbVerticalMap; i++) {
            boolean deleteLigne = true;
            StringBuilder sb2 = new StringBuilder();
            for (int j = 0; j < largeurMap * nbHorizontalMap; j++) {
                if(tabMap[x+i/hauteurMap][y+j/largeurMap] != null) {
                    map = ((i % hauteurMap == 0) || (j % largeurMap == 0)) ? tabMap[x + i / hauteurMap][y + j / largeurMap].getTableauMap() : map;
                    sb2.append(map[i % hauteurMap][j % largeurMap]);
                    deleteLigne = false;
                } else {
                    sb2.append(' ');
                }
            }
            if(!deleteLigne) {
                sb.append(sb2.toString());
                sb.append("\n");
            }
        }
        System.out.println("\n\n");
        System.out.println(delimitation);
        System.out.println(sb.toString());
        System.out.println(delimitation);
    }

    public Map[][] getEtage() {
        return tabMap;
    }

    public int getSpawnLigne() {
        return spawnLigne;
    }

    public int getSpawnColonne() {
        return spawnColonne;
    }

    public Map getMap(int x, int y) {
        return tabMap[x][y];
    }

}
