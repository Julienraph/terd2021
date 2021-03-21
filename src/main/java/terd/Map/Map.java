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
    private final int decalage=5;
    private Coordonne haut;
    private Coordonne bas;
    private Coordonne droite;
    private Coordonne gauche;
    public Map(int x, int y, Seed seedMap,int sortie) {
        this.seedMap = seedMap;
        this.tailleReelX=x;
        this.tailleReelY=y;
        tableauMap = new char[x][y];
        this.sortie=sortie;
        int Ligne;
        for ( Ligne = 0; Ligne < y; Ligne++) {
            int Colonne;
            for (Colonne = 0; Colonne < x; Colonne++) {
                tableauMap[Colonne][Ligne]=' ';
            }
        }
        this.creationMap(sortie);
    }
    public Map(int x, int y, Seed seedMap) {
        int Colonne;
        this.seedMap = seedMap;
        tableauMap = new char[x][y];
        for (Colonne = 0; Colonne < y; Colonne++) {
            int Ligne;
            for (Ligne = 0; Ligne < x; Ligne++) {
                whatToPutAt(Ligne, Colonne);
            }
        }
    }
    private void creationMap(int sortie){
        this.width= seedMap.getAnswer(10)+17;
        //this.width=18;
        //this.height=19;
        this.height=(width/2)+10;
        System.out.print("Width = ");
        System.out.println(width);
        System.out.print("height = ");
        System.out.println(height);
        //this.tableauMap=new char[height][width];
        for(int Ligne=decalage;Ligne<width+decalage;Ligne++)
        {
            tableauMap[0+decalage][Ligne]='#';
            tableauMap[height-1+decalage][Ligne]='#';
        }
        for(int Colonne=5;Colonne<height+5;Colonne++)
        {
            tableauMap[Colonne][0+decalage]='#';
            tableauMap[Colonne][width-1+decalage]='#';
        }
        this.remplissageInterieur();
        this.posezLaSortie(sortie);
    }
    private void remplissageInterieur()
    {
        int Colonne;
        for ( Colonne = 1+decalage; Colonne < height-1+decalage; Colonne++) {
            int Ligne;
            for (Ligne = 1+decalage; Ligne < width-1+decalage; Ligne++) {
                whatToPutAt(Colonne,Ligne);
            }
        }
    }
    private void posezLaSortie(int sortie)
    {
        if(sortie>=1000) //haut
        {
            int posW=width/2 - seedMap.getAnswer(12)/2 +decalage;
            tableauMap[0+decalage][posW]='=';
            sortie-=1000;
            this.haut=new Coordonne(0+decalage,posW);
            this.creationChemin(haut);
        }
        if(sortie>=100) //bas
        {
            int posW=width/2 - seedMap.getAnswer(13)/2 +decalage;
            tableauMap[height-1+decalage][posW]='=';
            sortie-=100;
            this.bas=new Coordonne(height-1+decalage,posW);
            this.creationChemin(bas);
        }
        if(sortie>=10) //droite
        {
            int posH=height/2 - seedMap.getAnswer(14)/2 +decalage;
            tableauMap[posH][width-1+decalage]='=';
            sortie-=10;
            this.droite=new Coordonne(posH,width-1+decalage);
            this.creationChemin(droite);
        }
        if(sortie>=1) //gauche
        {
            int posH=height/2 - seedMap.getAnswer(15)/2 +decalage;
            tableauMap[posH][0+decalage]='=';
            sortie-=1;
            this.gauche=new Coordonne(posH,0+decalage);
            this.creationChemin(gauche);
        }
    }
    private void creationChemin(Coordonne coordonneDepart) {
        int hauteur;
        int ligne;
        int x = coordonneDepart.getX();
        int y = coordonneDepart.getY();
        // coordonne est sur un mur, c'est donc une sortie
        if (x == decalage) {
            for (hauteur = decalage; hauteur >= 0; hauteur--) {
                tableauMap[hauteur][y - 1] = '#';
                tableauMap[hauteur][y] = '.';
                tableauMap[hauteur][y + 1] = '#';
            }
        } else if (x == height - 1 + decalage) {
            for (hauteur = height - 1 + decalage; hauteur <= tailleReelX - 1; hauteur++) {
                tableauMap[hauteur][y - 1] = '#';
                tableauMap[hauteur][y] = '.';
                tableauMap[hauteur][y + 1] = '#';
            }
        } else if (y == width - 1 + decalage) {
            for (ligne = width - 1 + decalage; ligne <= tailleReelY - 1; ligne++) {
                tableauMap[x - 1][ligne] = '#';
                tableauMap[x][ligne] = '.';
                tableauMap[x + 1][ligne] = '#';
            }
        } else if (y == decalage) {
            for (ligne = decalage; ligne >= 0; ligne--) {
                tableauMap[x - 1][ligne] = '#';
                tableauMap[x][ligne] = '.';
                tableauMap[x + 1][ligne] = '#';
            }
        }
    }
public void creationCheminDepuisExte(Coordonne coordonne)
{
    int CurseurLigne= coordonne.getY(); // x - >
    int CurseurColonne=coordonne.getX(); // y ^
    if(CurseurLigne<decalage || CurseurLigne>decalage){
        //System.out.println("CurseurLigne<decalage");
    while(CurseurLigne<=decalage+2)
    {
        //System.out.println("CurseurLigne<=decalage+2");
        tableauMap[CurseurColonne][CurseurLigne]='.';
        if((CurseurColonne+1<height)&&(tableauMap[CurseurColonne+1][CurseurLigne]==' '))
        {
            tableauMap[CurseurColonne+1][CurseurLigne]='#';
        }
        if((CurseurColonne-1>0)&&(tableauMap[CurseurColonne-1][CurseurLigne]==' '))
        {
            tableauMap[CurseurColonne-1][CurseurLigne]='#';
        }
        CurseurLigne++;
    }}
    while(CurseurLigne>=decalage+width)
    {
       // System.out.println("CurseurLigne>=decalage+width");
        tableauMap[CurseurColonne][CurseurLigne]='.';
        if((CurseurColonne+1<height)&&tableauMap[CurseurColonne+1][CurseurLigne]==' ')
        {
            tableauMap[CurseurColonne+1][CurseurLigne]='#';
        }
        if((CurseurColonne-1>0)&&tableauMap[CurseurColonne-1][CurseurLigne]==' ')
        {
            tableauMap[CurseurColonne-1][CurseurLigne]='#';
        }
        CurseurLigne--;
    }
    if(CurseurLigne!=coordonne.getY()) // on a donc déja bougé il faut fermer le haut du couloir
    {
        if((CurseurColonne+1<height) &&(CurseurColonne-1>0 )) {
            tableauMap[CurseurColonne - 1][CurseurLigne - 1] = '#';
            tableauMap[CurseurColonne - 1][CurseurLigne] = '#';
        }
    }
    while(CurseurColonne<=decalage+2)
    {
        //System.out.println("CurseurColonne<=decalage+2");
        tableauMap[CurseurColonne][CurseurLigne]='.';

        if(tableauMap[CurseurColonne][CurseurLigne+1]==' ')
        {
            tableauMap[CurseurColonne][CurseurLigne+1]='#';
        }
        if(tableauMap[CurseurColonne][CurseurLigne-1]==' ')
        {
            tableauMap[CurseurColonne][CurseurLigne-1]='#';
        }
        CurseurColonne++;
    }
    while(CurseurColonne>=height+decalage-2)
    {
       // System.out.println("CurseurColonne>=height+decalage-1");
        tableauMap[CurseurColonne][CurseurLigne]='.';
        if(tableauMap[CurseurColonne][CurseurLigne+1]==' ')
        {
            tableauMap[CurseurColonne][CurseurLigne+1]='#';
        }
        if(tableauMap[CurseurColonne][CurseurLigne-1]==' ')
        {
            tableauMap[CurseurColonne][CurseurLigne-1]='#';
        }
        CurseurColonne--;
    }
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
        Map map = new Map(40, 40, seed,0000);
        map.creationCheminDepuisExte(new Coordonne(39,24));
        map.affichageMap();
    }


}

