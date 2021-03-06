package terd.etage;


import terd.Map.Pos;
import terd.Map.Map;
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
    private int largeurMinMap;
    private int hauteurMinMap;
    private int hauteurEtage;
    private int largeurEtage;
    private int spawnLigne;
    private int spawnColonne;

    public Etage(int hauteurEtage, int largeurEtage, int coefHauteurMap, int coefLargeurMap, int biome, int niveau, Seed seed) {
        this.hauteurEtage = hauteurEtage;
        this.largeurEtage = largeurEtage;
        this.largeurMinMap = coefLargeurMap;
        this.hauteurMinMap = coefHauteurMap;
        this.biome = biome;
        this.niveau = niveau;
        this.seed = seed;
        Generator generator = new Generator(seed, largeurEtage, hauteurEtage, coefLargeurMap, coefHauteurMap);
        tabMap = generator.getMaps();
        this.hauteurMap = generator.tailleReelX;
        this.largeurMap = generator.tailleReelY;
        this.spawnColonne = generator.spawnColonne;
        this.spawnLigne = generator.spawnLigne;
        this.generationPont();
        this.spawnProps();
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

    //Spawn les monstres et les objets du niveau
    public void spawnProps() {
        int nbTresor = 0;
        for (int i = 0; i < tabMap.length; i++) {
            for (int y = 0; y < tabMap[0].length; y++) {
                Map map = tabMap[i][y];
                if (map != null) {
                    if (map.getMonsterList().get(0).getPos() != null) {
                        map.spawnProps(map.getMonsterList().get(0));
                        if(nbTresor < 3) {
                            nbTresor += map.randomItem(i);
                        }
                    } else {
                        map.spawnExit();
                        map.randomItem(i);
                    }
                }
            }
        }
    }

    ///Connecte les sorties de chaque map avec la map suivante
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

    //G??re le d??placement d'un Props
    public Boolean moveProps(Props props, int newPosX, int newPosY, char skin)
    {
        int ligneEtage = props.getPosEtageY();
        int colonneEtage = props.getPosEtageX();
        int posActuelX = props.getX();
        int posActuelY = props.getY();
        boolean isVertical = (newPosY < 0 && ligneEtage > 0) || (newPosY >= hauteurMap && ligneEtage < hauteurEtage - 1);
        boolean isHorizontal = (newPosX < 0 && colonneEtage > 0) || (newPosX >= largeurMap && colonneEtage < largeurEtage - 1);
        //Si le Props essaye de sortir de la map
        if (isHorizontal || isVertical) {
            int directionVertical = isVertical ? (int) Math.signum(newPosY) : 0; //Si le joueur sort de la map vers le haut, directionVertical = -1. Si vers le bas directionVertical = 1. Sinon 0.
            int directionHorizontal = isHorizontal ? (int) Math.signum(newPosX) : 0; //Si le joueur sort de la map vers la gauche, directionHorizontal = -1. Si vers la droite directionHorizontal = 1. Sinon 0.
            int spawnLigne = (directionVertical == 0) ? newPosY : ((directionVertical > 0) ? 0 : hauteurMap - 1); //La ligne o?? on va spawn le Props de la map vis??e
            int spawnColonne = (directionHorizontal == 0) ? newPosX : ((directionHorizontal > 0) ? 0 : largeurMap - 1); //La colonne o?? on va spawn le Props de la map vis??e
            if ((tabMap[ligneEtage + directionVertical][colonneEtage + directionHorizontal] != null)) { // Si la map vis??e existe
                if (tabMap[ligneEtage + directionVertical][colonneEtage + directionHorizontal].isValide(spawnColonne, spawnLigne)) { // Si la case vis??e de la map vis??e est valide
                    tabMap[ligneEtage][colonneEtage].resetCase(posActuelX, posActuelY,props); //On reset la case o?? se situe le Props de la map actuel avant de passer ?? la map vis??e
                    tabMap[ligneEtage + directionVertical][colonneEtage + directionHorizontal].moveProps(spawnColonne, spawnLigne, props); //On place le Props sur la map vis??e
                    props.setNewMap(ligneEtage + directionVertical, colonneEtage + directionHorizontal); //On assigne la nouvelle map du Props
                    props.setPosition(spawnColonne, spawnLigne); //On la nouvelle position du Props
                    return true;
                }
            }
        }
        //Si le Props voulait sortir de la map mais n'a pas pu
        if (newPosX < 0 || newPosY < 0 || newPosX >= largeurMap || newPosY >= hauteurMap) {
            return false;
        }
        //Si le Props se d??place ?? l'int??rieur de la map
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


    //Affiche l'UI/Indication ?? droite de l'affichage
    public void afficherMap(int x, int y, Player player) {
        StringBuilder sb = new StringBuilder();
        char[][] map = tabMap[x][y].getTableauMap();
        int positionCarte = 2;
        int positionEcritCarte = positionCarte - 2;
        int positionTuto = positionCarte + 1 + hauteurEtage;
        int positionAfficherCarte = positionTuto + 1;
        int positionTP = positionAfficherCarte + 1;
        int positionInventaire = positionTP + 1;
        int positionMenuPrincipal = positionInventaire + 1;
        int positionXP = positionMenuPrincipal + 2;
        int positionLevel = positionXP + 1;
        int positionPV = positionLevel + 1;
        int positionArme = positionPV + 1;
        int positionCompetence = positionArme + 1;
        int positionCredit = positionCompetence + 1;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (MapColor.getMapColorForSymbol(map[i][j]) == null) {
                    throw new EnumConstantNotPresentException(MapColor.class, String.valueOf(map[i][j]));
                }
                sb.append(MapColor.getMapColorForSymbol(map[i][j]).getColoredString());
            }
            sb.append("       ");
            if (i == positionEcritCarte) {
                sb.append("Carte de l'??tage :");
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
                sb.append("- ZQSD pour se d??placer");
            }
            if (i == positionAfficherCarte) {
                sb.append("- M pour afficher la carte");
            }
            if (i == positionTP) {
                sb.append("- T pour se t??l??porter");
            }
            if (i == positionInventaire) {
                sb.append("- P pour afficher l'Inventaire");
            }
            if (i == positionMenuPrincipal) {
                sb.append("- X pour quitter au Menu Principal");
            }
            if (i == positionXP) {
                sb.append(String.format("XP : %d", player.getXP()));
            }
            if (i == positionLevel) {
                sb.append(String.format("Level : %d", player.getLevelProps()));
            }
            if (i == positionPV) {
                sb.append(String.format("PV : %d", player.getPV()));
            }
            if (i == positionArme) {
                sb.append(String.format("Arme Principale : %s - %d d??gats", player.getMainWeapon().getNom(), player.getMainWeapon().getDegat()));
            }
            if (i == positionCompetence) {
                sb.append(String.format("Competence Principale : %s - %d d??gats", player.getMainCompetence().getNom(), player.getMainCompetence().getDegat()));
            }
            if (i == positionCredit) {
                sb.append(String.format("Cr??dit : %d", player.getCredit()));
            }
            if (i < map.length - 1) {
                sb.append("\n");
            }

        }
        System.out.println(sb.toString());
    }


    //Affiche la carte dans son ensemble
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

    public int getBiome() {
        return biome;
    }

    public int getNiveau() {
        return niveau;
    }

    public Seed getSeed() {
        return seed;
    }

    public int getHauteurMap() {
        return hauteurMap;
    }

    public int getLargeurMap() {
        return largeurMap;
    }

    public int getLargeurMinMap() {
        return largeurMinMap;
    }

    public int getHauteurMinMap() {
        return hauteurMinMap;
    }

    //Constructeur pour pouvoir faire des tests dans EtageTest
    public Etage(int hauteurEtage, int largeurEtage, int hauteurSalle, int LargeurSalle) {
        Seed seed = new Seed();
        this.hauteurEtage = hauteurEtage;
        this.largeurEtage = largeurEtage;
        this.hauteurMap = hauteurSalle+2;
        this.largeurMap = LargeurSalle+2;
        this.spawnColonne = 1;
        this.spawnLigne = 0;
        int decalage = 1;
        this.tabMap = new Map[hauteurEtage][largeurEtage];
        tabMap[0][0] = new Map(hauteurSalle,LargeurSalle,0,decalage,seed);
        tabMap[0][1] = new Map(hauteurSalle,LargeurSalle,100,decalage,seed);
        tabMap[0][2] = new Map(hauteurSalle,LargeurSalle,0,decalage,seed);
        tabMap[1][0] = new Map(hauteurSalle,LargeurSalle,10,decalage,seed);
        tabMap[1][1] = new Map(hauteurSalle,LargeurSalle,0,decalage,seed);
        tabMap[1][2] = new Map(hauteurSalle,LargeurSalle,1,decalage,seed);
        tabMap[2][0] = new Map(hauteurSalle,LargeurSalle,0,decalage,seed);
        tabMap[2][1] = new Map(hauteurSalle,LargeurSalle,1000,decalage,seed);
        tabMap[2][2] = new Map(hauteurSalle,LargeurSalle,0,decalage,seed);
        this.generationPont();
        affichage();
    }

}
