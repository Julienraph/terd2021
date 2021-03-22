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
    private final int decalage = 5 ;
    private Coordonne haut;
    private Coordonne bas;
    private Coordonne droite;
    private Coordonne gauche;
    public Map(int x, int y, Seed seedMap,int sortie) {
        this.seedMap = seedMap;
        this.tailleReelX=x;
        this.tailleReelY=y;
        this.width = seedMap.getAnswer(10)+17;
        this.height = (width/2)+10;
        tableauMap = new char[x][y];
        this.sortie=sortie;
        this.creationMap();

    }

    private void creationMap(){
        int sortieHautY = width/2 - seedMap.getAnswer(12)/2 +decalage;
        int sortieBasY = width/2 - seedMap.getAnswer(13)/2 +decalage;
        int sortieGaucheX = width/2 - seedMap.getAnswer(14)/2 +decalage;
        int sortieDroiteX = width/2 - seedMap.getAnswer(15)/2 +decalage;

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
                    this.bas=new Coordonne(height-1+decalage,sortieBasY);
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
        while(curseurColonne <= decalage + 2 || curseurColonne >= width + decalage - 2)
        {
            tableauMap[curseurLigne][curseurColonne]='.';
            if((curseurLigne <= height + decalage)&&(tableauMap[curseurLigne +1][curseurColonne]==' '))
            {
                tableauMap[curseurLigne +1][curseurColonne]='#';
            }
            if((curseurLigne >= 1)&&(tableauMap[curseurLigne -1][curseurColonne]==' '))
            {
                tableauMap[curseurLigne -1][curseurColonne]='#';
            }
            curseurColonne += direction;
        }
       return curseurColonne;
    }

    private int alignementLigne(int curseurLigne, int curseurColonne, int direction) {
        while(curseurLigne <=decalage + 2 || curseurLigne >= height + decalage - 2)
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
        return curseurLigne;
    }

    public void spawnPlayer(int x, int y, char skin) {
        tableauMap[x][y] = skin;
    }
    private void whatToPutAt(int ligne, int colonne) // choisi quel case placé a une position donnée en fonction de la seed
    {
        tableauMap[ligne][colonne] = '.';
    }
    //Appelé si le joueur passe dans une autre map, cela fait réapparaitre l'ancienne case de l'ancienne map
    public void resetCase(int posActuelX, int posActuelY) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        whatToPutAt(posActuelY, posActuelX); //
    }
    public void moveProps(int posActuelX, int posActuelY, int newPosX, int newPosY, char Props) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        whatToPutAt(posActuelY, posActuelX); //
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
    public static void main(String[] args) {
        Seed seed = new Seed();
        Map map = new Map(40, 40, seed,10);
        map.creationCheminDepuisExte(new Coordonne(8,0));
        System.out.println(map.getDroite().toString());
        map.affichageMap();
        char[][] tab = new char[5][5];
        System.out.println(tab[0][0] == '\u0000');
        System.out.println(map.height/2 - seed.getAnswer(15)/2 +5);
        System.out.println(Math.signum(0));
    }


}

