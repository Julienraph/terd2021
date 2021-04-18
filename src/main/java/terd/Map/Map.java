package terd.Map;
import terd.Player.Monster;
import terd.Player.OrcWarrior;
import terd.Player.Player;
import terd.Player.Props;
import terd.utils.Seed;

import java.util.ArrayList;
import java.util.List;

public class Map{
    private char[][] tableauMap;
    private Seed seedMap;
    private int sortie;
    private  int width;
    private  int height;
    private int tailleReelX; // colonne // hauteur
    private int tailleReelY; // ligne // largeur
    private int decalage = 2 ;
    private Pos haut;
    private Pos bas;
    private Pos droite;  // remplacé par posSortie
    private Pos gauche;
    private Pos spawnPos;
    private Pos posSortie;
    private char cache;
    private int biome;
    DecisionCase decisionCase;

    private List<Monster> monsterList = new ArrayList<>();
    public Map(int x, int y, Seed seedMapBase,int sortie,int seedpos) {
        this.cache='.';
        this.seedMap = new Seed(seedMapBase,seedpos);
        this.biome= seedMap.getAnswer(1);
        /*System.out.print("Numero :");
        System.out.print(seedpos);
        System.out.print(" :");
        System.out.print(seedMap.getSeed());*/
        if(biome>=7){
            this.decisionCase=new DecisionCase(seedMap,'.',',','T','X',seedpos);
        }
        else{
            this.decisionCase=new DecisionCase(seedMap,'.',',','L','X',seedpos);
        }
        this.width = (seedMap.getAnswer(10+seedpos))+y;
        this.height = (seedMap.getAnswer(8+seedpos))%10+x;
        this.tailleReelX = 15+x+decalage+1;
        this.tailleReelY = 15+y+decalage+1;
        tableauMap = new char[tailleReelX][tailleReelY];
        this.sortie=sortie;
        this.creationMap();
        this.spawnPos = new Pos(decalage+1,decalage+1);
        //this.posSortie= new Pos(decalage+1,decalage+1);
        monsterList.add(new OrcWarrior(new Pos(5,6), 'M'));
        spawnProps(monsterList.get(0));
    }
    //copy constructor because clone is broken
    public Map(char[][] tableauMap, Seed seedMap, int sortie, int width, int height, int tailleReelX, int tailleReelY, Pos haut, Pos bas, Pos droite, Pos gauche, Pos spawnPos, Pos posSortie, char cache, int biome, DecisionCase decisionCase) {
        this.tableauMap = tableauMap;
        this.seedMap = seedMap;
        this.sortie = sortie;
        this.width = width;
        this.height = height;
        this.tailleReelX = tailleReelX;
        this.tailleReelY = tailleReelY;
        this.haut = haut;
        this.bas = bas;
        this.droite = droite;
        this.gauche = gauche;
        this.spawnPos = spawnPos;
        this.posSortie = posSortie;
        this.cache = cache;
        this.biome = biome;
        this.decisionCase = decisionCase;
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
    public void killMonster(int i) {
        Monster monster = monsterList.get(i);
        monsterList.remove(i);
        resetCase(monster.getX(),monster.getY(),monster);
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
    public void resetCase(int colonne, int ligne, Props props) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        tableauMap[ligne][colonne] = props.getCache();
    }
    public void moveProps(int newPosX, int newPosY, Props props) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        props.setCache(tableauMap[newPosY][newPosX]);
        tableauMap[newPosY][newPosX] = props.getSkin();
    }
    public List<Monster> getMonsterList() {
        return monsterList;
    }
    private void RemplissageMap(int fake)
    {
        char[][] copy=decisionCase.DonneMoiUneMap(width,height);
        int i;
        int j;
        int ibis = 0;
        for(i=decalage+1;i<height+decalage;i++)
        {
            int jbis=0;
            for(j=decalage+1;j<width+decalage;j++)
            {
                tableauMap[i][j]=copy[ibis][jbis];
                jbis++;
            }
            ibis++;
        }
    }
    private void RemplissageMap()
    {
        int i;
        int j;
        for(i=decalage+1;i<height+decalage;i++)
        {
            for(j=decalage+1;j<width+decalage;j++)
            {
                char Lacase =decisionCase.DonneMoiUneCase(tableauMap[i-1][j],tableauMap[i][j-1],tableauMap[i-1][j-1]);
                tableauMap[i][j]=Lacase;
            }
        }
    }
    /////////////////////////////////
    private void creationMap(){
        int moduloWidth = (width - decalage - 2) == 0 ? (width - decalage - 2 + 1) : (width - decalage - 2);
        int moduloHeight = (height - decalage - 2) == 0 ? (height - decalage - 2 + 1) : (height - decalage - 2);
        int sortieHautY = ((width/2 - seedMap.getAnswer(12)/2 +decalage)%moduloWidth) + decalage + 1;
        int sortieBasY = ((width/2 - seedMap.getAnswer(13)/2 +decalage) %moduloWidth) + decalage + 1;
        int sortieGaucheX = ((width/2 - seedMap.getAnswer(14)/2 +decalage) %moduloHeight) + decalage + 1;
        int sortieDroiteX = ((width/2 - seedMap.getAnswer(15)/2 +decalage) %moduloHeight) + decalage + 1;
        RemplissageMap(1);
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
                    this.haut=new Pos(decalage+1,sortieHautY);

                }
                //Création pont du bas si y a une sortie
                if(sortie % 1000 >= 100 && ligne >= height + decalage && colonne == sortieBasY) {
                    tableauMap[ligne][colonne-1] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne][colonne+1] = '#';
                    this.posSortie=new Pos(height+decalage-1,sortieBasY);
                    this.bas=new Pos(height+decalage-1,sortieBasY);

                }
                //Création pont de droite si y a une sortie
                if(sortie % 100 >= 10 && ligne == sortieDroiteX && colonne >= width + decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.posSortie=new Pos(sortieDroiteX,width + decalage-1);
                    this.droite=new Pos(sortieDroiteX,width + decalage-1);

                }
                //Création pont de gauche si y a une sortie
                if(sortie % 10 >= 1 && ligne == sortieGaucheX && colonne <= decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.posSortie=new Pos(sortieGaucheX,decalage+1);
                    this.gauche=new Pos(sortieGaucheX,decalage+1);

                }
                //Remplissage de vide si rien n'a été remplie
                if(tableauMap[ligne][colonne] == '\u0000') {
                    tableauMap[ligne][colonne] = ' ';
                }
            }
        }

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
               // System.out.println(spawnPos);
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            } else {
              //  System.out.println(spawnPos);
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            }
        } else {
            curseurLigne = alignementLigne(curseurLigne, curseurColonne, directionLigne);
            curseurColonne = alignementColonne(curseurLigne, curseurColonne, directionColonne);
            if(directionColonne < 0) {
             //   System.out.println(spawnPos);
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            } else {
               // System.out.println(spawnPos);
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            }
        }
          this.creationCheminInterne(this.spawnPos,this.getPosSortie());
    }
    private int alignementColonne(int curseurLigne, int curseurColonne, int direction) {
        while((curseurColonne >= 0 && curseurColonne <= decalage +1 ) || (curseurColonne >= width + decalage -1  && curseurColonne < tailleReelY))
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
        while((curseurLigne >= 0 && curseurLigne < decalage +1 ) || (curseurLigne >= height + decalage -1 && curseurLigne < tailleReelX))
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
        if(gaucheDroite<0 || basHaut<0 || gaucheDroite>=tailleReelY || basHaut >= tailleReelX)
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
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public Map copy(){
        char[][] old=this.getTableauMap();
        char[][] current=new char[tailleReelX][tailleReelY];
        for(int i=0; i<tailleReelX; i++)
            for(int j=0; j<tailleReelY; j++)
                current[i][j]=old[i][j];
        Map map=new Map(current,this.seedMap,this.sortie,this.width,this.height,this.tailleReelX,this.tailleReelY,this.haut,this.bas,this.droite,this.gauche,this.spawnPos,this.posSortie,this.cache,this.biome,this.decisionCase);
        return map;
    }
    public boolean IsCheminFromTo(Pos debut, Pos fin, Pos aEvite)   // getY = gaucheDroite   getX = hautBas // int = nbr de case traversé
    {
        if(isValide(fin)==false)
        {
            return false;
        }
        if(isValide(debut)==false)
        {
            return false;
        }
        popProps(debut,'1');
        if (debut.equals(fin)) {
            return true;
        }
        else {
            if(debut.getY()-aEvite.getY()>0)
            {
                return ( isValide(debut.addY(1)) && IsCheminFromTo(debut.addY(1),fin,debut) ) || ( isValide(debut.addX(-1)) && IsCheminFromTo(debut.addX(-1),fin,debut) ) || ( isValide(debut.addX(1)) && IsCheminFromTo(debut.addX(1),fin,debut) ) ;
            }
            if(debut.getY()-aEvite.getY()<0)
            {
                return ( isValide(debut.addY(-1)) && IsCheminFromTo(debut.addY(-1),fin,debut) ) || ( isValide(debut.addX(-1)) && IsCheminFromTo(debut.addX(-1),fin,debut) ) || ( isValide(debut.addX(1)) && IsCheminFromTo(debut.addX(1),fin,debut) ) ;
            }
            if(debut.getX()-aEvite.getX()<0)
            {
                return ( isValide(debut.addX(-1)) && IsCheminFromTo(debut.addX(-1),fin,debut) ) || ( isValide(debut.addY(-1)) && IsCheminFromTo(debut.addY(-1),fin,debut) ) || ( isValide(debut.addY(1)) && IsCheminFromTo(debut.addY(1),fin,debut) ) ;
            }
            if(debut.getX()-aEvite.getX()>0)
            {
                return ( isValide(debut.addX(1)) && IsCheminFromTo(debut.addX(1),fin,debut) ) || ( isValide(debut.addY(-1)) && IsCheminFromTo(debut.addY(-1),fin,debut) ) || ( isValide(debut.addY(1)) && IsCheminFromTo(debut.addY(1),fin,debut) ) ;
            }
            else{
                return false; // aucun chemin
            }
        }
    }

    public boolean creationCheminInterne(Pos debut, Pos fin){
        if( posSortie==null )
        {
           return false;
        }
        Pos aEvite;
        if(debut.getY()==decalage+width-1 && debut.getX()!=decalage+1 && debut.getX()!=decalage+height-1){aEvite=debut.addY(1);}  //// on cherche la position a ne pas controler lors de la recherche de chemin
        if(debut.getY()==decalage+width+1 && debut.getX()!=decalage+1 && debut.getX()!=decalage+height-1){aEvite=debut.addY(-1);} //// cette position est en réalité le bout du couloir vers une autre map
        if(debut.getX()==decalage+1 && debut.getY()!=decalage+1 && debut.getY()!=decalage+width-1){aEvite=fin.addX(-1);} //// mais je laisse la possibilité d'utililisé cette methode pour faire autre chose qu'une liaison interne entre deux couloirs
        if(debut.getX()==decalage-1 && debut.getY()!=decalage+1 && debut.getY()!=decalage+width-1){aEvite=fin.addX(-1);} ////int from correspond donc a la direction où on ne veux pas chercher, obligatoire pour IsCheminFromTo
        else{aEvite=debut;} //
        if(this.copy().IsCheminFromTo(debut,fin,aEvite))
        {
            return true;
        }
        else
        {
            popProps(debut,'.');
            popProps(fin,'.');
            Pos chemin=new Pos(debut.getX(), debut.getY());
            while(!(chemin.equals(fin))){
                if(chemin.getX()< fin.getX()){
                    if(!(isValide(chemin) && tableauMap[chemin.getX()][chemin.getY()]!='#'))
                    {
                        popProps(chemin,',');
                        chemin=chemin.addX(1);
                    }
                    else
                    {
                        chemin=chemin.addX(1);
                    }
                }
                else if(chemin.getX()>fin.getX())
                {
                    if(!(isValide(chemin) && tableauMap[chemin.getX()][chemin.getY()]!='#'))
                    {
                        popProps(chemin,',');
                        chemin=chemin.addX(-1);
                    }
                    else
                    {
                        chemin=chemin.addX(-1);
                    }
                }
                if(chemin.getY()>fin.getY())
                {
                    if(!(isValide(chemin) && tableauMap[chemin.getX()][chemin.getY()]!='#'))
                    {
                        popProps(chemin,',');
                        chemin=chemin.addY(-1);
                    }
                    else
                    {
                        chemin=chemin.addY(-1);
                    }
                }
                else if(chemin.getY()<fin.getY())
                {
                    if(!(isValide(chemin) && tableauMap[chemin.getX()][chemin.getY()]!='#'))
                    {
                        popProps(chemin,',');
                        chemin=chemin.addY(1);
                    }
                    else
                    {
                        chemin=chemin.addY(1);
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
       Seed seed=new Seed("81cad2488b706822a6472707bbf11f762b6d006f0fbe5e548446569998affd0a4a3037b10add7022821a20750edfa5c63cabcdcafe9ed3f973ba2ccd68927537aa5c3f077b95c4e29628ee2562b266b309958cf988453ec3183284b633abeec0b1fcede89e5334f1d999de4fdb086a64248561da47b59296d30ae291c4a5be8434f3a7c611a15af3b8ff4a563835faf175bace6ad77dc09754c94777c0e88317ffa6afec110a86e2ad4a7033907e2164c35020cfb68cf9e77a39a9dcedf6dff7f3812cd7d05dfdf12b25de2056bfe65b5485edd9b4d98e65b8c876593e7bd5aebd20a583a10ddc0961af3914b8d1e54eddd37216a8ad1aaf0d20");
        Map map = new Map(10,20,seed,1000,1);
        map.affichageMap();
    }

    public int getDecalage() {
        return decalage;
    }

    public void setPosSortie(Pos posSortie) {
        this.posSortie = posSortie;
    }

    //Constructeur pour pouvoir faire des tests dans EtageTest
    public Map(int x, int y, int sortie, int decalage, Seed seed) {
        this.decisionCase=new DecisionCase(seed,'.','.','.','.',0);
        this.decalage = decalage;
        this.seedMap = seed;
        this.height = x;
        this.width = y;
        this.tailleReelX = this.decalage+height+1;
        this.tailleReelY = this.decalage+width+1;
        tableauMap = new char[tailleReelX][tailleReelY];
        this.sortie=sortie;
        this.creationMap();
        this.spawnPos = new Pos(decalage+2,decalage+2);
        monsterList.add(new OrcWarrior(new Pos(5,6), 'M'));
        spawnProps(monsterList.get(0));
    }
}