package terd.Map;
import terd.Player.Monster;
import terd.Player.OrcWarrior;
import terd.Player.Player;
import terd.Player.Props;
import terd.utils.Seed;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private char[][] tableauMap;
    private Seed seedMap;
    private int sortie;
    private  int width;
    private  int height;
    private int tailleReelX; // colonne // hauteur
    private int tailleReelY; // ligne // largeur
    private final int decalage = 2 ;
    private Pos haut;
    private Pos bas;
    private Pos droite;
    private Pos gauche;
    private Pos spawnPos;
    private List<Monster> monsterList = new ArrayList<>();


    private final int biome;
    DecisionCase decisionCase;

    public Map(int x, int y, Seed seedMap,int sortie,int seedpos) {
        this.seedMap = seedMap;
        this.biome= seedMap.getAnswer(0)%2;
        if(biome==0){
            this.decisionCase=new DecisionCase(seedMap,'.',',','T','X');
        }
        else{
            this.decisionCase=new DecisionCase(seedMap,'.',',','L','X');
        }
        this.width = (seedMap.getAnswer(10+seedpos))+y;
        this.height = (seedMap.getAnswer(8+seedpos))%10+x;
        this.tailleReelX = 15+x+decalage+1;
        this.tailleReelY = 15+y+decalage+1;
        tableauMap = new char[tailleReelX][tailleReelY];
        this.sortie=sortie;
        this.creationMap();
        this.spawnPos = new Pos(decalage+1,decalage+1);
        monsterList.add(new OrcWarrior(new Pos(5,6), 'M'));
        spawnProps(monsterList.get(0));
    }
    private void RemplissageMap()
    {
        int i;
        int j;
        for(i=decalage+1;i<height+decalage;i++)
        {
            for(j=decalage+1;j<width+decalage;j++)
            {
                char Lacase =decisionCase.DonneMoiUneCase(tableauMap[i-1][j],tableauMap[i][j-1]);
              //  System.out.println(Lacase);
                tableauMap[i][j]=Lacase;
            }
        }
    }

    public boolean isInside(Pos pos) {
        return (pos.getX() > decalage && pos.getX() < tailleReelY - (tailleReelY - decalage - width)
                && pos.getY() > decalage && pos.getY() < tailleReelX - (tailleReelX - height - decalage));
    }

    public Boolean moveProps(Props props, int newPosX, int newPosY) {
        if (props.getY() < tailleReelX && props.getX() < tailleReelY) {
            if (this.isValide(newPosX, newPosY)) {
                this.resetCase(props.getX(), props.getY(),props);
                this.moveProps(newPosX, newPosY, props);
                props.setPosition(newPosX, newPosY);
                return true;
            }
        }
        return false;
    }

    public int moveMonsters(Pos posPlayer) {
        for(int i = 0; i < monsterList.size(); i++) {
            Monster monster = monsterList.get(i);
            if(monster.isBeside(posPlayer)) {
                return i;
            }
            if(isInside(posPlayer)) {
                moveProps(monster, monster.getX(), monster.getY() - 1);
            }
            if (monster.isBeside(posPlayer)) {
                return i;
            }
        }
        return -1;
    }


    private void creationMap(){
        int moduloWidth = (width - decalage - 2) == 0 ? (width - decalage - 2 + 1) : (width - decalage - 2);
        int moduloHeight = (height - decalage - 2) == 0 ? (height - decalage - 2 + 1) : (height - decalage - 2);
        int sortieHautY = ((width/2 - seedMap.getAnswer(12)/2 +decalage)%moduloWidth) + decalage + 1;
        int sortieBasY = ((width/2 - seedMap.getAnswer(13)/2 +decalage) %moduloWidth) + decalage + 1;
        int sortieGaucheX = ((width/2 - seedMap.getAnswer(14)/2 +decalage) %moduloHeight) + decalage + 1;
        int sortieDroiteX = ((width/2 - seedMap.getAnswer(15)/2 +decalage) %moduloHeight) + decalage + 1;
        RemplissageMap();
        for(int ligne = 0; ligne < tailleReelX; ligne++) {
            for(int colonne = 0; colonne < tailleReelY; colonne++) {
                //Remplissage de la salle si on est à l'intérieur
                /*if(isInside(ligne, colonne, decalage)) {
                    whatToPutAt(ligne, colonne);
                }*/
                //Création mur vertical
                if((ligne >= decalage && ligne <= height + decalage) && (colonne == decalage || colonne == width + decalage)) {
                    tableauMap[ligne][colonne] = '#';
                }
                //Création mur horizontal
                if((colonne >= decalage && colonne <= width + decalage) && (ligne == decalage || ligne == height + decalage)) {
                    tableauMap[ligne][colonne] = '#';
                }
                //Création pont du haut si y a une sortie
                if(sortie >= 1000 && ligne <= decalage && colonne == sortieHautY) {
                    tableauMap[ligne][colonne-1] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne][colonne+1] = '#';
                    this.haut=new Pos(decalage+1,sortieHautY);
                }
                //Création pont du bas si y a une sortie
                if(sortie % 1000 >= 100 && ligne >= height + decalage && colonne == sortieBasY) {
                    tableauMap[ligne][colonne-1] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne][colonne+1] = '#';
                    this.bas=new Pos(height+decalage-1,sortieBasY);
                }
                //Création pont de droite si y a une sortie
                if(sortie % 100 >= 10 && ligne == sortieDroiteX && colonne >= width + decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.droite=new Pos(sortieDroiteX,width + decalage-1);
                }
                //Création pont de gauche si y a une sortie
                if(sortie % 10 >= 1 && ligne == sortieGaucheX && colonne <= decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.gauche=new Pos(sortieGaucheX,decalage+1);
                }
                //Remplissage de vide si rien n'a été remplie
                if(tableauMap[ligne][colonne] == '\u0000') {
                    tableauMap[ligne][colonne] = ' ';
                }
            }
        }
    }

    public void killMonster(int i) {
        Monster monster = monsterList.get(i);
        monsterList.remove(i);
        resetCase(monster.getX(),monster.getY(),monster);
    }

    public void creationCheminDepuisExte(Pos pos)
    {
        int curseurColonne = pos.getY();
        int curseurLigne = pos.getX();
        int directionColonne = (int)Math.signum(decalage + 3 - pos.getY());
        int directionLigne = (int)Math.signum(decalage + 3 - pos.getX());
        if(curseurLigne == tailleReelX - 1 || curseurLigne == 0) {
            curseurColonne = alignementColonne(curseurLigne, curseurColonne, directionColonne);
            curseurLigne = alignementLigne(curseurLigne, curseurColonne, directionLigne);
            if(directionLigne < 0) {
                this.bas = new Pos(curseurLigne+1, curseurColonne);
            } else {
                this.haut = new Pos(curseurLigne, curseurColonne);
            }
        } else {
            curseurLigne = alignementLigne(curseurLigne, curseurColonne, directionLigne);
            curseurColonne = alignementColonne(curseurLigne, curseurColonne, directionColonne);
            if(directionColonne < 0) {
                this.droite = new Pos(curseurLigne, curseurColonne+1);
            } else {
                this.gauche = new Pos(curseurLigne, curseurColonne-1);
            }
        }
    }

    private int alignementColonne(int curseurLigne, int curseurColonne, int direction) {
        while((curseurColonne >= 0 && curseurColonne <= decalage + 1) || (curseurColonne >= width + decalage - 1 && curseurColonne < tailleReelY))
        {
            tableauMap[curseurLigne][curseurColonne]='.';
            if((curseurLigne < height + decalage)&&(tableauMap[curseurLigne +1][curseurColonne]==' '))
            {
                tableauMap[curseurLigne +1][curseurColonne]='#';
            }
            if((curseurLigne >= 1)&&(tableauMap[curseurLigne -1][curseurColonne]==' '))
            {
                tableauMap[curseurLigne -1][curseurColonne]='#';
            }
            curseurColonne += direction;
        }
        curseurColonne = (curseurColonne < 0) ? (curseurColonne + 1) : curseurColonne;
        curseurColonne = (curseurColonne == tailleReelY) ? (curseurColonne - 1) : curseurColonne;
        return curseurColonne;
    }

    private int alignementLigne(int curseurLigne, int curseurColonne, int direction) {

        while((curseurLigne >= 0 && curseurLigne < decalage + 1) || (curseurLigne >= height + decalage - 1 && curseurLigne < tailleReelX))
        {
            tableauMap[curseurLigne][curseurColonne]='.';
            if((curseurColonne < width + decalage) && tableauMap[curseurLigne][curseurColonne+1]==' ')
            {
                tableauMap[curseurLigne][curseurColonne+1]='#';
            }
            if((curseurColonne >= 1) && tableauMap[curseurLigne][curseurColonne-1]==' ')
            {
                tableauMap[curseurLigne ][curseurColonne-1]='#';
            }
            curseurLigne += direction;

        }
        curseurLigne = (curseurLigne < 0) ? (curseurLigne + 1) : curseurLigne;
        curseurLigne = (curseurLigne == tailleReelX) ? (curseurLigne - 1) : curseurLigne;
        return curseurLigne;
    }

    public void spawnProps(Props props) {
        props.setCache(tableauMap[props.getY()][props.getX()]);
        tableauMap[props.getY()][props.getX()] = props.getSkin();
    }

    public Pos spawnPlayer(Props props) {
        props.setCache('.');
        tableauMap[spawnPos.getX()][spawnPos.getY()] = props.getSkin();
        return(spawnPos);
    }

    //Appelé si le joueur passe dans une autre map, cela fait réapparaitre l'ancienne case de l'ancienne map
    public void resetCase(int colonne, int ligne, Props props) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        tableauMap[ligne][colonne] = props.getCache();
    }
    public void moveProps(int newPosX, int newPosY, Props props) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        props.setCache(tableauMap[newPosY][newPosX]);
        tableauMap[newPosY][newPosX] = props.getSkin();
    }

    public boolean isValide(int colonne, int ligne)  // indique si la case ciblé est valide pour se déplacé ou non
    {
        if (tableauMap[ligne][colonne] == '.' || tableauMap[ligne][colonne] == 'L' || tableauMap[ligne][colonne] == ',') {
            return true; //
        }
        else if (tableauMap[ligne][colonne] == 'X' || tableauMap[ligne][colonne] == '-' || tableauMap[ligne][colonne] == '#')
        { // =='X'
            return false;
        }
        else{return false;}
    }
    public char[][] getTableauMap() {
        return tableauMap;
    }
    public void affichageMap(){
        ////affichage test//
        int i;
        //tableauMap[decalage+1][decalage+1]='A';
        for ( i = 0; i < tailleReelX; i++) {
            int Ligne;
            for (Ligne = 0; Ligne < tailleReelY; Ligne++) {
                System.out.print(tableauMap[i][Ligne]);
            }
            System.out.print("\n");
        }
    }

    public List<Monster> getMonsterList() {
        return monsterList;
    }

    public int getSortie() {
        return sortie;
    }

    public Pos getHaut() {
        return haut;
    }

    public Pos getBas() {
        return bas;
    }

    public Pos getDroite() {
        return droite;
    }

    public Pos getGauche() {
        return gauche;
    }



    public int getTailleReelX() {
        return tailleReelX;
    }

    public int getTailleReelY() {
        return tailleReelY;
    }


}