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

    private char cache;

    public Map(int x, int y, Seed seedMap,int sortie) {
        this.seedMap = seedMap;
        this.width = seedMap.getAnswer(10)+y;
        this.height = seedMap.getAnswer(8)+x;
        this.tailleReelX = 15+x+decalage+1;
        this.tailleReelY = 15+y+decalage+1;
        tableauMap = new char[tailleReelX][tailleReelY];
        this.sortie=sortie;
        this.creationMap();
        this.spawnPos = new Coordonne(decalage+1,decalage+1);
    }
    private void RemplissageMap()
    {
        DecisionCase decisionCase=new DecisionCase(seedMap);
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


    private void creationMap(){
        int sortieHautY = ((width/2 - seedMap.getAnswer(12)/2 +decalage)%(width - decalage - 2)) + decalage + 1;
        int sortieBasY = ((width/2 - seedMap.getAnswer(13)/2 +decalage) %(width - decalage - 2)) + decalage + 1;
        int sortieGaucheX = ((width/2 - seedMap.getAnswer(14)/2 +decalage) %(height - decalage - 2)) + decalage + 1;
        int sortieDroiteX = ((width/2 - seedMap.getAnswer(15)/2 +decalage) %(height - decalage - 2)) + decalage + 1;
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
                    tableauMap[ligne][colonne] = '-';
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
        while((curseurColonne >= 0 && curseurColonne <= decalage + 2) || (curseurColonne >= width + decalage - 2 && curseurColonne < tailleReelY))
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
        while((curseurLigne >= 0 && curseurLigne <=decalage + 2) || (curseurLigne >= height + decalage - 2 && curseurLigne < tailleReelX))
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
        tableauMap[colonne][ligne]=cache;
        whatToPutAt(ligne, colonne); //
        cache=tableauMap[newPosY][newPosX];
        tableauMap[newPosY][newPosX] = Props;

    }
    public boolean isValide(int colonne, int ligne)  // indique si la case ciblé est valide pour se déplacé ou non
    {
        if (tableauMap[ligne][colonne] == '.' || tableauMap[ligne][colonne] == 'L' || tableauMap[ligne][colonne] == ',') {
            return true; //
        }
        else if (tableauMap[ligne][colonne] == 'X' || tableauMap[ligne][colonne] == '-')
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
        //Seed seed = new Seed();
        Seed seed=new Seed("da354af7afa61784784a8e22d969f9d1380a229dd06fe7dc69a371bf829a19ea83bffaeeb58f7a44bfe26ce51b03a8c2fa40a6ad990fde1e573fd80415490de81c8ceb99a46276bcfa98e843f46b3e88b5cec0fc1d7a95819042bc8a6417b8aa5f93a281f72a81cf57255c33d883dc985fd5ad062b4b2d43107f86da92a34b3ad50e402976a0290385ba922f142651b5ec5ecf31635c9003ec1a953879dd7694bf8b97068d219c51c687fc6848de4b58f49");
        //Seed seed = new Seed("f45146c80362fff50de78a7");
       // Seed seed=new Seed("bbd416a5e50a092415cf1de7ac3cacc3439037f6b556d671d8de273f");
        Map map = new Map(0, 15, seed,10);
      //  map.creationCheminDepuisExte(new Coordonne(8,0));
    //    System.out.println(map.getDroite().toString());
        map.affichageMap();
    }


}