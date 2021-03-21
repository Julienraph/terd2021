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
        //this.width=32;
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
    private void creationChemin(Coordonne coordonneDepart){
        int hauteur;
        int ligne;
        int x = coordonneDepart.getX();
        int y = coordonneDepart.getY();
        if(x==decalage){
            for(hauteur=decalage;hauteur>=0;hauteur--)
            {
                tableauMap[hauteur][y-1]='#';tableauMap[hauteur][y]='.';tableauMap[hauteur][y+1]='#';
            }
        }
        else if(x==height-1+decalage)
        {
            for(hauteur=height-1+decalage;hauteur<=tailleReelX-1;hauteur++)
            {
                tableauMap[hauteur][y-1]='#';tableauMap[hauteur][y]='.';tableauMap[hauteur][y+1]='#';
            }
        }
        else if(y==width-1+decalage){
            for(ligne=width-1+decalage;ligne<=tailleReelY-1;ligne++)
            {
                tableauMap[x-1][ligne]='#';tableauMap[x][ligne]='.';tableauMap[x+1][ligne]='#';
            }
        }
        else if(y==decalage){
            for(ligne=decalage;ligne>=0;ligne--)
            {
                tableauMap[x-1][ligne]='#';tableauMap[x][ligne]='.';tableauMap[x+1][ligne]='#';
            }
        }
    }
    public void creationChemin(Coordonne Depart,Coordonne arrive) {
        int hauteurDepart = Depart.getX();
        int hauteurArrive=arrive.getX();
        int ligneDepart= Depart.getY();
        int ligneArrive=arrive.getY();
        if(hauteurArrive>hauteurDepart){  // arrive en bas par rapport au depart
            tableauMap[hauteurDepart][0]='.';tableauMap[hauteurDepart-1][0]='#';tableauMap[hauteurDepart-1][2]='#'; // debut de
            tableauMap[hauteurDepart][1]='.';tableauMap[hauteurDepart-1][1]='#';tableauMap[hauteurDepart][2]='#';   // couloir en bord de map
            int hauteur;
            for(hauteur=hauteurDepart+1;hauteur<hauteurArrive;hauteur++)
            {
                tableauMap[hauteur][0]='#';tableauMap[hauteur][1]='.';tableauMap[hauteur][2]='#';
            }
            tableauMap[hauteur][0]='#';tableauMap[hauteur][1]='.';
            tableauMap[hauteur+1][0]='#';tableauMap[hauteur+1][1]='#';
            int ligne;
            for(ligne=2;ligne<ligneArrive;ligne++)
            {
                tableauMap[hauteur][ligne]='.';tableauMap[hauteur+1][ligne]='#';tableauMap[hauteur-1][ligne]='#';
            }
            tableauMap[hauteurArrive][ligneArrive]='.';
        }
        else if(hauteurArrive<hauteurDepart){
            tableauMap[hauteurDepart][0]='.';tableauMap[hauteurDepart-1][0]='#';tableauMap[hauteurDepart-1][2]='#'; // debut de
            tableauMap[hauteurDepart][1]='.';tableauMap[hauteurDepart-1][1]='#';tableauMap[hauteurDepart][2]='#';   // couloir en bord de map
            int hauteur;
            for(hauteur=hauteurDepart+1;hauteur<hauteurArrive;hauteur++)
            {
                tableauMap[hauteur][0]='#';tableauMap[hauteur][1]='.';tableauMap[hauteur][2]='#';
            }
            tableauMap[hauteur][0]='#';tableauMap[hauteur][1]='.';
            tableauMap[hauteur+1][0]='#';tableauMap[hauteur+1][1]='#';
            int ligne;
            for(ligne=2;ligne<ligneArrive;ligne++)
            {
                tableauMap[hauteur][ligne]='.';tableauMap[hauteur+1][ligne]='#';tableauMap[hauteur-1][ligne]='#';
            }
            tableauMap[hauteurArrive][ligneArrive]='.';
        }
        else if(hauteurArrive==hauteurDepart){
            System.out.println("Vrai");
            creationChemin(Depart);
        }
    }

    public void spawnPlayer(int x, int y, char skin) {
        tableauMap[x][y] = skin;
    }
    private void whatToPutAt(int ligne, int colonne) // choisi quel case placé a une position donnée en fonction de la seed
    {
            int oracle = this.seedMap.getAnswer(ligne * 3 + colonne * 5) % 16;
        tableauMap[ligne][colonne] = '.';
            /*if (oracle < 7) {
                tableauMap[ligne][colonne] = '/';
            } else if (oracle <= 11) {
                tableauMap[ligne][colonne] = '_';
            } else if (oracle < 15) {
                tableauMap[ligne][colonne] = 'L';
            } else {
                tableauMap[ligne][colonne] = 'X';
            }*/
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
        if (tableauMap[ligne][colonne] == 'L' || tableauMap[ligne][colonne] == '.' || tableauMap[ligne][colonne] == ':' || tableauMap[ligne][colonne] == ' ') {
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
    public static void main(String[] args) {
        Seed seed = new Seed();
        Map map = new Map(40, 40, seed,1000);
        map.creationChemin(new Coordonne(10,5),new Coordonne(20,5));
        map.affichageMap();
       // map.moveProps(0, 0, 1, 0, 'X');
       // System.out.println(map.getTableauMap());
       // System.out.println(map.isValide(1, 0));
       // System.out.println(map.isValide(0, 1));
       // System.out.println(seed.getSeed());
    }

}

