package terd.Map;
import terd.utils.Seed;
public class Map {
    private char[][] tableauMap;
    private Seed seedMap;
    private int sortie;
    private  int width;
    private  int height;
    private int tailleReelX; // colonne // hauteur
    private int tailleReelY; // ligne // largeur
    private final int decalage = 2 ;
    private Coordonne haut;
    private Coordonne bas;
    private Coordonne droite;
    private Coordonne gauche;
    private Coordonne spawnPos;

    public Map(int x, int y, Seed seedMap,int sortie, int seedpos) {
        this.seedMap = seedMap;
        this.width = (seedMap.getAnswer(10+seedpos))+y;
        this.height = (seedMap.getAnswer(8+seedpos))%10+x;
        this.tailleReelX = 15+x+decalage+1;
        this.tailleReelY = 15+y+decalage+1;
        tableauMap = new char[tailleReelX][tailleReelY];
        this.sortie=sortie;
        this.creationMap();
        this.spawnPos = new Coordonne(decalage+1,decalage+1);
    }

    private void creationMap(){
        int moduloWidth = (width - decalage - 2) == 0 ? (width - decalage - 2 + 1) : (width - decalage - 2);
        int moduloHeight = (height - decalage - 2) == 0 ? (height - decalage - 2 + 1) : (height - decalage - 2);
        int sortieHautY = ((width/2 - seedMap.getAnswer(12)/2 +decalage)%moduloWidth) + decalage + 1;
        int sortieBasY = ((width/2 - seedMap.getAnswer(13)/2 +decalage) %moduloWidth) + decalage + 1;
        int sortieGaucheX = ((width/2 - seedMap.getAnswer(14)/2 +decalage) %moduloHeight) + decalage + 1;
        int sortieDroiteX = ((width/2 - seedMap.getAnswer(15)/2 +decalage) %moduloHeight) + decalage + 1;
        for(int ligne = 0; ligne < tailleReelX; ligne++) {
            for(int colonne = 0; colonne < tailleReelY; colonne++) {
                //Remplissage de la salle si on est à l'intérieur
                if(isInside(ligne, colonne, decalage)) {
                    whatToPutAt(ligne, colonne);
                }
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
                    this.haut=new Coordonne(decalage,sortieHautY);
                }
                //Création pont du bas si y a une sortie
                if(sortie % 1000 >= 100 && ligne >= height + decalage && colonne == sortieBasY) {
                    tableauMap[ligne][colonne-1] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne][colonne+1] = '#';
                    this.bas=new Coordonne(height+decalage,sortieBasY);
                }
                //Création pont de droite si y a une sortie
                if(sortie % 100 >= 10 && ligne == sortieDroiteX && colonne >= width + decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.droite=new Coordonne(sortieDroiteX,width + decalage);
                }
                //Création pont de gauche si y a une sortie
                if(sortie % 10 >= 1 && ligne == sortieGaucheX && colonne <= decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.gauche=new Coordonne(sortieGaucheX,decalage);
                }
                //Remplissage de vide si rien n'a été remplie
                if(tableauMap[ligne][colonne] == '\u0000') {
                    tableauMap[ligne][colonne] = ' ';
                }
            }
        }
    }

    public static void main(String[] args) {
        Seed seed = new Seed();
        Map map = new Map(3, 3, seed, 0);
        map.moveProps(0, 0, 1, 0, 'X');
        System.out.println(map.isValide(1, 0));
        System.out.println(map.isValide(0, 1));
        System.out.println(seed.getSeed());

    }

    private boolean isInside(int ligne, int colonne, int decalage) {
        return ((ligne > decalage && ligne < height + decalage) && (colonne > decalage && colonne < width + decalage));
    }

    public void creationCheminDepuisExte(Coordonne coordonne)
    {
        int curseurColonne = coordonne.getY();
        int curseurLigne = coordonne.getX();
        int directionColonne = (int)Math.signum(decalage + 3 - coordonne.getY());
        int directionLigne = (int)Math.signum(decalage + 3 - coordonne.getX());
        if(curseurLigne == tailleReelX - 1 || curseurLigne == 0) {
            curseurColonne = alignementColonne(curseurLigne, curseurColonne, directionColonne);
            alignementLigne(curseurLigne, curseurColonne, directionLigne);
        } else {
            curseurLigne = alignementLigne(curseurLigne, curseurColonne, directionLigne);
            alignementColonne(curseurLigne, curseurColonne, directionColonne);
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

    public Coordonne spawnPlayer(char skin) {
        tableauMap[spawnPos.getX()][spawnPos.getY()] = skin;
        return(spawnPos);
    }
    private void whatToPutAt(int ligne, int colonne) // choisi quel case placé a une position donnée en fonction de la seed
    {
        tableauMap[ligne][colonne] = '.';
    }
    //Appelé si le joueur passe dans une autre map, cela fait réapparaitre l'ancienne case de l'ancienne map
    public void resetCase(int colonne, int ligne) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        whatToPutAt(ligne, colonne); //
    }
    public void moveProps(int colonne, int ligne, int newPosX, int newPosY, char Props) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        whatToPutAt(ligne, colonne); //
        tableauMap[newPosY][newPosX] = Props;
    }
    public boolean isValide(int colonne, int ligne)  // indique si la case ciblé est valide pour se déplacé ou non
    {
        if (tableauMap[ligne][colonne] == '.') {
            return true; //
        } else { // =='X'
            return false;
        }
    }
    public char[][] getTableauMap() {
        return tableauMap;
    }
    public void affichageMap(){
        ////affichage test//
        int i;
        for ( i = 0; i < tailleReelX; i++) {
            int Ligne;
            for (Ligne = 0; Ligne < tailleReelY; Ligne++) {
                System.out.print(tableauMap[i][Ligne]);
            }
            System.out.print("\n");
        }
    }
    public int getSortie() {
        return sortie;
    }

    public Coordonne getHaut() {
        return haut;
    }

    public Coordonne getBas() {
        return bas;
    }

    public Coordonne getDroite() {
        return droite;
    }

    public Coordonne getGauche() {
        return gauche;
    }


    public int getTailleReelX() {
        return tailleReelX;
    }

    public int getTailleReelY() {
        return tailleReelY;
    }


}