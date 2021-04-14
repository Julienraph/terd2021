package terd.Map;
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
    private Pos droite;  // remplacé par posSortie
    private Pos gauche;
    private Pos spawnPos;
    private Pos posSortie;
    private char cache;


    private final int biome;
    DecisionCase decisionCase;

    public Map(int x, int y, Seed seedMap,int sortie,int seedpos) {
        this.cache='.';
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
                    this.posSortie=new Pos(decalage+1,sortieHautY);
                }
                //Création pont du bas si y a une sortie
                if(sortie % 1000 >= 100 && ligne >= height + decalage && colonne == sortieBasY) {
                    tableauMap[ligne][colonne-1] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne][colonne+1] = '#';
                    this.posSortie=new Pos(height+decalage-1,sortieBasY);
                }
                //Création pont de droite si y a une sortie
                if(sortie % 100 >= 10 && ligne == sortieDroiteX && colonne >= width + decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.posSortie=new Pos(sortieDroiteX,width + decalage-1);
                }
                //Création pont de gauche si y a une sortie
                if(sortie % 10 >= 1 && ligne == sortieGaucheX && colonne <= decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.posSortie=new Pos(sortieGaucheX,decalage+1);
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
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
               // tableauMap[curseurLigne][curseurColonne]='S'; /////////////////////
            } else {
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
               // tableauMap[curseurLigne][curseurColonne]='S'; /////////////////////
            }
        } else {
            curseurLigne = alignementLigne(curseurLigne, curseurColonne, directionLigne);
            curseurColonne = alignementColonne(curseurLigne, curseurColonne, directionColonne);
            if(directionColonne < 0) {
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
               // tableauMap[curseurLigne][curseurColonne]='S'; /////////////////////
            } else {
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
               // tableauMap[curseurLigne][curseurColonne]='S'; /////////////////////
            }
        }
       //tableauMap[spawnPos.getX()][spawnPos.getY()]='S';


    }

    private int alignementColonne(int curseurLigne, int curseurColonne, int direction) {
        while((curseurColonne >= 0 && curseurColonne <= decalage ) || (curseurColonne >= width + decalage  && curseurColonne < tailleReelY))
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

        while((curseurLigne >= 0 && curseurLigne < decalage ) || (curseurLigne >= height + decalage  && curseurLigne < tailleReelX))
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

    public Pos spawnPlayer(char skin) {

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
        tableauMap[ligne][colonne]=cache;
    }
    public void moveProps(int colonne, int ligne, int newPosX, int newPosY, char Props) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        resetCase(colonne, ligne); //
        cache=tableauMap[newPosY][newPosX];
        tableauMap[newPosY][newPosX] = Props;
    }
    public void popProps( int newPosX, int newPosY, char Props) // fait apparaitre une case
    {
       // resetCase(colonne, ligne); //
        //cache=tableauMap[newPosY][newPosX];
        tableauMap[newPosY][newPosX] = Props;
    }
    public void popProps(Pos coordonne,char Props)
    {
        popProps(coordonne.getY(), coordonne.getX(), Props);
    }

    public boolean isValide(int gaucheDroite, int basHaut)  // indique si la case ciblé est valide pour se déplacé ou non
    {
        if(gaucheDroite<0 || basHaut<0 || gaucheDroite>tailleReelY || basHaut > tailleReelX)
        {
            return false;
        }
        if (tableauMap[basHaut][gaucheDroite] == '.' || tableauMap[basHaut][gaucheDroite] == '-' || tableauMap[basHaut][gaucheDroite] == ',') {
            return true; //
        }
        else if (tableauMap[basHaut][gaucheDroite] == 'X' || tableauMap[basHaut][gaucheDroite] == '-' || tableauMap[basHaut][gaucheDroite] == '#')
        { // =='X'

            return false;
        }
        else{return false;}
    }
    public boolean isValide(Pos coordonne)
    {
        return isValide(coordonne.getY(),coordonne.getX());
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
    public Pos getPosSortie() {
        return posSortie;
    }
    public Pos getSpawnPos()
    {
        return spawnPos;
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

   public void creationCheminInterne(Pos debut, Pos fin)   // getY = gaucheDroite   getX = hautBas
   {
     while(!(debut.equals(fin))) {

         if(debut.getY()<fin.getY()) {
             while (debut.getY() < fin.getY() && isValide(debut.addY(1))) {
                 System.out.println("while gaucheDroite");
                 debut.setY(debut.getY() + 1);
                 popProps(debut, '1');
             }
             if(debut.getY()!=fin.getY()) {
                 if (isValide(debut.addX(1))) // case du haut libre
                 {
                     debut = debut.addX(1);

                 } else if (isValide(debut.addX(-1))) // case du bas libre
                 {
                     debut = debut.addX(-1);

                 } else {                      // pas de chemin par là, retour en arriere
                     popProps(debut, 'I');
                     debut = debut.addY(-1);
                 }
             }
         }
         if(debut.getX()> fin.getX())
         {
             while (debut.getX() > fin.getX() && isValide(debut.addX(-1))) {
                 debut.setX(debut.getX() - 1);
                 popProps(debut, '2');
             }
             if(debut.getX()!=fin.getX()) {
                 if (isValide(debut.addY(1))) // case de droite libre
                 {
                     debut = debut.addY(1);

                 } else if (isValide(debut.addY(-1))) // case de gauche libre
                 {
                     debut = debut.addY(-1);

                 } else {                      // pas de chemin par là, retour en arriere
                     popProps(debut, 'I');
                     debut = debut.addX(1);
                 }
             }
         }
          if(debut.getY()>fin.getY()) {
             while (debut.getY() > fin.getY() && isValide(debut.addY(-1))) {
                 System.out.println("while gaucheDroite");
                 debut=debut.addY(-1);
                 popProps(debut, '3');
             }
             if(debut.getY()!=fin.getY()) {
                 if (isValide(debut.addX(1))) // case du haut libre
                 {
                     debut = debut.addX(1);

                 } else if (isValide(debut.addX(-1))) // case du bas libre
                 {
                     debut = debut.addX(-1);

                 } else {                      // pas de chemin par là, retour en arriere
                     popProps(debut, 'I');
                     debut = debut.addY(1);
                 }
             }
         }
          if(debut.getX()< fin.getX())
          {
              while (debut.getX() > fin.getX() && isValide(debut.addX(1))) {
                  debut.setX(debut.getX() + 1);
                  popProps(debut, '2');
              }
              if(debut.getX()!=fin.getX()) {
                  if (isValide(debut.addY(-1))) // case de droite libre
                  {
                      debut = debut.addY(-1);

                  } else if (isValide(debut.addY(1))) // case de gauche libre
                  {
                      debut = debut.addY(1);

                  } else {                      // pas de chemin par là, retour en arriere
                      popProps(debut, 'I');
                      debut = debut.addX(-1);
                  }
              }
          }
         System.out.println(debut);
         System.out.println(fin);
         affichageMap();
     }
   }


    public static void main(String[] args) {
        Seed seed=new Seed("1a354af1afbc55784784a8e22d969f9d1380a229dd06fe7dc69a371bf829a19ea83bffaeeb58f7a44bfe26ce51b03a8c2fa40a6ad990fde1e573fd80415490de81c8ceb99a46276bcfa98e843f46b3e88b5cec0fc1d7a95819042bc8a6417b8aa5f93a281f72a81cf57255c33d883dc985fd5ad062b4b2d43107f86da92a34b3ad50e402976a0290385ba922f142651b5ec5ecf31635c9003ec1a953879dd7694bf8b97068d219c51c687fc6848de4b58f49");
        Map map = new Map(5,5,seed,1000,1);
       // map.popProps(11,3,'X');
        map.popProps(12,3,'.');
        map.popProps(9,8,'X');
        map.popProps(5,8,'.');
        map.popProps(8,9,'X');
        map.creationCheminDepuisExte(new Pos(8,0));
       System.out.println(map.getSpawnPos());
        System.out.println(map.getPosSortie());
        Pos debut = new Pos(3,3);
        map.popProps(debut,'k');
        Pos fin = new Pos(3,11);
      //  System.out.println(debut.getY()<fin.getY());
       // map.popProps(pos,'K');
        //map.popProps(pos2,'M');
    // map.creationCheminInterne(map.getSpawnPos(),map.getPosSortie());
       map.affichageMap();
    }

}