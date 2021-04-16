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
    private final int decalage = 2 ;
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
    /////////////////////////////////
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
                    System.out.println("pas executer");
                    this.posSortie=new Pos(decalage+1,sortieHautY);
                    this.haut=new Pos(decalage+1,sortieHautY);
                    System.out.println(haut);
                    System.out.println(posSortie);
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
                System.out.println(spawnPos);
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            } else {
                System.out.println(spawnPos);
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            }
        } else {
            curseurLigne = alignementLigne(curseurLigne, curseurColonne, directionLigne);
            curseurColonne = alignementColonne(curseurLigne, curseurColonne, directionColonne);
            if(directionColonne < 0) {
                System.out.println(spawnPos);
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            } else {
                System.out.println(spawnPos);
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            }
        }
          this.creationCheminInterne(this.spawnPos,this.getPosSortie(),this.sortie);
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

    public boolean creationCheminInterne(Pos debut, Pos fin,int from){
        if( posSortie==null )
        {
            return true;
        }
        Pos aEvite;
        if(from==1000){aEvite=fin.addY(1);}  //// on cherche la position a ne pas controler lors de la recherche de chemin
        if(from==100){aEvite=fin.addY(-1);} //// cette position est en réalité le bout du couloir vers une autre map
        if(from==10){aEvite=fin.addX(1);} //// mais je laisse la possibilité d'utililisé cette methode pour faire autre chose qu'une liaison interne entre deux couloirs
        if(from==1){aEvite=fin.addX(-1);} ////int from correspond donc a la direction où on ne veux pas chercher, obligatoire pour IsCheminFromTo
        else{aEvite=fin;} // impossible, c juste pour faire plaisir au compilo
        if(this.copy().IsCheminFromTo(debut,fin,aEvite))
        {
            return true;
        }
        else
        {
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
        //Seed seed=new Seed("1a354af1afbc55784784a8e22d969f9d1380a229dd06fe7dc69a371bf829a19ea83bffaeeb58f7a44bfe26ce51b03a8c2fa40a6ad990fde1e573fd80415490de81c8ceb99a46276bcfa98e843f46b3e88b5cec0fc1d7a95819042bc8a6417b8aa5f93a281f72a81cf57255c33d883dc985fd5ad062b4b2d43107f86da92a34b3ad50e402976a0290385ba922f142651b5ec5ecf31635c9003ec1a953879dd7694bf8b97068d219c51c687fc6848de4b58f49");
        //Seed seed=new Seed("f3a300134c0b020b2fd3b41ace65b34e60c04c6fd63b73d7f52412ee5677873afb288b9a842f213b88d46d22949f6ff9d4505790cf0ad051f4e7d691185cee23c8059e34f2f7ed5ee95b25823d615745619b5f124f0463d316d4fff1279b74d6bc91e4f672b850a849eed77dff51a9213aa859f6de8508b8786cbd5e4f539160c4d49f860e50764ebba41b5978b7307af36cb018d5e59b61ca2a8d096a41f726df6c3d60e48eedd9d47c1da48a242bead");
        // Seed seed=new Seed("db9ab3576b058ededcb7bfe7531331978fc861d1cf3ac95c0871cf2fa5a2cc9ac99952179b1b29a7746da526ae131fdef51f2a05de964b37b7ce2ec4a2c652e1794ae9f92e10cc4ced37dcf080a1811944a953b458c22d97cc75982994b62e840257bb4d97f3ef2cb30756a819eb812e5bda8630a376ac905acab492e5bf539ed28e5bb5c7d75e3ae3574a07a2d6f91e0a55bd72d06085a4997f93de35692dd339a7ca4f91042959ec54efb27e5518d3f3ff6");
        //Seed seed=new Seed("90adae30d0c28688de49cbdbf6b844cf6d90faa7a2e5679292f0262af1a312c5e67dcaf5497fc79881d0187fcbdd07861f000f0547a1f671e657b0987e60c7c5ef4a54a2ff5ff5f0778e7fae6d8c8bf2380dd3a2307d656463b2fd73207c3d76679cbe535fce60699028bab790c4f61ee43afa450e9a87f9a7cdc2a9904b0bd3e13eded91a5c885f5a8c0ddd7553ba82b52d167ab4c79a1b9f0e63a1624d2ce529340f171d2fccc736c3e4c49c85da2");
        Seed seed=new Seed("ac5008b1903e1a84430f2589a13eb647fe65c546c467583bfeaf47c83f306089e73e9a3bb86627a5ac5e42631f640bcdd7a1a191b8806e6579134c87c1c8bb09aa41bd9a81d375c7c465e8cc21c202ed3d5ae5bcb9f5b23ae092099774cd607f5350fb751da90dfb196f97c0dc0de4e7e6f86b9e475aab1366093f7417c64a87bf11ff388850835cd8a75babb6c74e730084d2edf2f6bcfa6c75fc4cd5304eb5dfa742d9092754d79ec960692b428b");
       // System.out.println(seed.getSeed());
        Map map = new Map(5,5,seed,1000,1);
       //map.affichageMap();
        Pos fin = map.getPosSortie();
        Pos debut = map.getSpawnPos();

        // map.popProps(fin,'X');
        map.creationCheminDepuisExte(new Pos(5,0));
        fin=map.getPosSortie();
        debut =map.getSpawnPos();
        System.out.println(debut);
        System.out.println(fin);
        map.popProps(debut,'I');
        map.popProps(fin,'S');
       // map.creationCheminInterne(map.getSpawnPos(),map.getPosSortie(),100);
        map.affichageMap();
    }


}