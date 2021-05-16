/*
Auteur : Mascaro Valentin Contributeurs : Julien Raphael
Derniere modification : 18/04 par Mascaro Valentin // Modif RemplissageMap ajout du dernier argument dans l'appel a la methode de decisionCase WIP
Objectif : Créé une map contenant au plus une sortie, la sortie est un couloirs créé dans un des murs haut/bas/gauche/droite de la map, il marque la fin d'une map
    - La map possède toujours un chemin entre son entré et sa sortie, l'entrée est le point de spawn du Player
    - Le player spawn en 0,0 par défaut, sinon il spawn a la coordonné correspondante au points d'arrivé depuis la sortie d'une autre map
    - Si le Player sort d'une map en 5,25 il spawnera en 5,0 sur une nouvelle map ( 5 = hauteur, 25 et 0 correspond a la droite pour la map de sortie, et la gauche pour celle d'arrivé)
    - Le contenu de la map est déterminé par la classe DecisionCase qui renvoi les cases de manière aléatoires ( mais controler par certaines régle WIP )
 */

package terd.Map;
import terd.Player.*;
import terd.item.Arme;
import terd.item.Consommable;
import terd.item.Item;
import terd.utils.Seed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map{
    private char[][] tableauMap; // la map est un tableau de char[][], solution simple pour avoir une map 2d, le problème étant que déposé un "item" sur une case reviens a la supprimé
    private Seed seedMap;
    private int sortie;
    private  int width;
    private  int height;
    private int tailleReelX; // colonne // hauteur
    private int tailleReelY; // ligne // largeur
    private int decalage = 2 ;  // on remplie une map en décalé de 2, ce décalage est prévue pour pouvoir créé une "arrivée", un couloirs d'accès
                                // venue de l'exterieur de la map.

    private Pos haut; // en cas de sortie haut          // haut bas gauche et droite sont déterminé a etre remplacé par posSortie
    private Pos bas;    // en cas de sortie bas         // ce n'est toujours pas fait en attendant de créé des maps intermédiaire comme des marchands/commerce
    private Pos droite;  // en cas de sortie droite    // qui pourrait etre relié a haut/bas/gauche/droite,
    private Pos gauche; // en cas de sortie gauche
    private Pos spawnPos; // la position d'entrée sur la map par défaut 0,0 puis set en fonction de la sortie de la map relié a this
    private Pos posSortie; // la position de sortie sur la map par défaut 0,0 puis set en fonction de la sortie  haut/bas/gauche/droite
    private Pos posMonster; //Position du monster situé à la sortie

    Item tresorItem;
    int tresorCredit;
    private char cache;
    private int biome;
    DecisionCase decisionCase; // cette classe permet de remplir la map a partir du seed et de 4 arguments
    // new DecisionCase( la seed , ' la case de base presente sur la map ' , ' la case commune ' , ' la case rare ', ' la case très très rare ' )
    // DecisionCase est loin d'etre fini, le but est de généré une map contenant des biomes differents, telle que des 'foret' dans lequel la case T sera très presente
    // ou des plaines dans lesquel la case , sera très presente
    private List<Monster> monsterList = new ArrayList<>();

    // x et y permettent de déterminé des tailles pour la map,
    // la Seed est la clé qui remplace l'aléatoire dans le jeu ( plutot que de faire random.int(0-15), on va plutot rechercher la valeur a l'indice k sur la Seed
    // sortie correspond a la position demandé pour la sortie sur la map, l'int varie entre 4 valeur 1000 100 10 1, correspondant a haut bas droite gauche
    // seedpos correspond a un entier quelconque qui permet de généré une nouvelle seed pour chaque nouvelle map
    public Map(int x, int y, Seed seedMapBase,int sortie,int seedpos) {
        this.cache='.';
        this.seedMap = new Seed(seedMapBase,seedpos); // nouvelle generation du seed, chaque map a  le meme seed en argument de son constructeur, puis le modifie en fonction de seedpos
        this.biome= seedMap.getAnswer(1);
        if(biome>=7){
            this.decisionCase=new DecisionCase(seedMap,'.',',','T','X',seedpos);
        }
        else{
            this.decisionCase=new DecisionCase(seedMap,'.',',','L','X',seedpos);
        }
        this.width = (seedMap.getAnswer(2+seedpos))+y;     // les differentes tailles utilisé dans les map sont déduites a partir du seed
        this.height = (seedMap.getAnswer(8+seedpos))%10+x;
        this.tailleReelX = 15+x+decalage+1;
        this.tailleReelY = 15+y+decalage+1;
        tableauMap = new char[tailleReelX][tailleReelY];
        this.sortie=sortie;
        this.creationMap();
        this.spawnPos = new Pos(decalage+1,decalage+1);
        choixMonstre(seedpos);
    }
    //ce constructeur permet de faire une copie complete d'un objet map, Java utilisant des adresses pour ses objets, faire Map map2 = map1 ne copie pas
    // il faut donc faire Map map2 = map1.copy() , et copy() utilise ce constructeur
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
    // détermine si une Pos est a l'intérieur de la map ( a l'interieur des 4 murs )
    public boolean isInside(Pos pos) {
        return (pos.getX() >= decalage + 1 && pos.getX() <= tailleReelY - (tailleReelY - decalage - width)
                && pos.getY() >= decalage + 1 && pos.getY() <= tailleReelX - (tailleReelX - height - decalage));
    }
    // détermine si une entité est déplaçable a la position donné en X Y et déplace l'entité si possible
    // X et Y plutot que Pos car cette fonction a été créé avant l'existence de la classe Pos, il est prévue de la mettre a jour
    // cette fonction replace sur l'ancienne position de l'entité, la case anciennement placé. ( si @ était sur un '.', après s'etre déplacé, le '.' reviens )
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

    public int randomItem(int seedPos) {
        int choix = seedMap.getAnswer(seedPos);
        Pos pos = randomPos();
        if(choix % 8 == 0 && (tableauMap[pos.getY()][pos.getX()] == '.' || tableauMap[pos.getY()][pos.getX()] == ',')) {
            String[] nomTab = {"Cerise", "Abricot", "Viande", "Pomme", "Baie", "Fraise", "Potion"};
            int[] pvTab = {20,10,30,5,5,20,50};
            String nom = nomTab[seedMap.getAnswer(seedPos+1)%nomTab.length];
            int pv = pvTab[seedMap.getAnswer(seedPos+1)%pvTab.length];
            tresorItem = new Consommable(0, nom, (1 + (seedMap.getAnswer(seedPos+2)%2)), 10, pv);
            tableauMap[pos.getY()][pos.getX()] = 'P';
            return 1;
        }
        if(choix % 8 == 1 && (tableauMap[pos.getY()][pos.getX()] == '.' || tableauMap[pos.getY()][pos.getX()] == ',')){
            tresorCredit = 1 + seedMap.getAnswer(seedPos+1)%2;
            tableauMap[pos.getY()][pos.getX()] = 'O';
            return 1;
        }
        return 0;
    }

    public String recupererTresor(Player player) {
        if(tresorItem != null) {
            player.getInventaire().ajoutItem(tresorItem);
            player.setCache('.');
            return String.format("Vous avez récupéré %d %s(s) ", tresorItem.getNbrUtilisation(), tresorItem.getNom());
        } else {
            player.setCredit(player.getCredit() + tresorCredit);
            player.setCache('.');
            return String.format("Vous avez récupéré %d crédit(s)", tresorCredit);
        }
    }

    public void choixMonstre(int seedPos) {
        int choix = seedMap.getAnswer(seedPos+2)%3;
        if(choix == 0) {
            monsterList.add(new OrcWarrior(posMonster, 'M'));
        } else if(choix == 1) {
            monsterList.add(new Sanglier(posMonster, 'B'));
        } else {
            monsterList.add(new Cerf(posMonster, 'C'));
        }
    }

    public Pos randomPos() {
        Random random = new Random();
        int x = (decalage + 1)+random.nextInt((tailleReelY - (tailleReelY - decalage - width))-(decalage + 1));
        int y = (decalage + 2)+random.nextInt((tailleReelX - (tailleReelX - decalage - height))-(decalage + 2));
        return new Pos(x,y);
    }

    public int moveMonsters(Pos posPlayer) {
        for(int i = 0; i < monsterList.size(); i++) {
            Monster monster = monsterList.get(i);
            if(monster.isBeside(posPlayer)) {
                return i;
            }
            if(isInside(posPlayer)) {
                monster.act(posPlayer,this);
            }
            if (monster.isBeside(posPlayer)) {
                return i;
            }
        }
        return -1;
    }
    // supprime un monstre et remet la case prévue a la position du monstres
    public void killMonster(int i) {
        Monster monster = monsterList.get(i);
        monsterList.remove(i);
        resetCase(monster.getX(),monster.getY(),monster);
    }

    public void spawnProps(Props props) {
        //Si le spawn du monstre n'est pas le même que le spawn du Player
        if(!props.getPos().equals(spawnPos)) {
            props.setCache('.');
            tableauMap[props.getY()][props.getX()] = props.getSkin();
        } else {
            monsterList.remove(0);
        }
    }

    public void spawnExit() {
        tableauMap[posSortie.getX()][posSortie.getY()] = 'D';
        monsterList.remove(0);
    }
    // fait apparaitre le player a sa spawnPos
    public Pos spawnPlayer(Props props) {
        props.setCache('.');
        tableauMap[spawnPos.getX()][spawnPos.getY()] = props.getSkin();
        return(spawnPos);
    }
    public void resetCase(int colonne, int ligne, Props props) // fait réapparaitre l'ancienne case
    {
        tableauMap[ligne][colonne] = props.getCache();
    }
    public void moveProps(int newPosX, int newPosY, Props props) // deprecated
    {
        props.setCache(tableauMap[newPosY][newPosX]);
        tableauMap[newPosY][newPosX] = props.getSkin();
    }
    public List<Monster> getMonsterList() {
        return monsterList;
    }
    private void RemplissageMap(int test)  // inutilisé car DecisionCase WIP
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
    // Remplie l'interieur de la map en fonction des cases donné par decisionCase
    private void RemplissageMap()
    {
        int i;
        int j;
        for(i=decalage+1;i<height+decalage;i++)
        {
            for(j=decalage+1;j<width+decalage;j++)
            {
                char Lacase =decisionCase.DonneMoiUneCase(tableauMap[i-1][j],tableauMap[i][j-1],tableauMap[i-1][j-1]);
               //char Lacase =decisionCase.DonneMoiUneCase(tableauMap[i-1][j],tableauMap[i][j-1]);
                tableauMap[i][j]=Lacase;
            }
        }
    }
    /////////////////////////////////
    //Commence par déterminé la longueur et la hauteur de la map
    // Puis déterminé 4 positions de sortie
    // On remplie ensuite la Map en prenant en compte le décalage
    // On créé ensuite les 4 murs de '#'
    // Puis on créé les ponts (couloirs) de sortie de la map
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
                    this.posSortie=new Pos(decalage+1,sortieHautY); // cette affectation double n'est plus censé existé par la suite,
                    this.posMonster=new Pos(sortieHautY,decalage+1);
                    this.haut=new Pos(decalage+1,sortieHautY);      // a l'avenir posSortie remplacera completement haut/bas/gauche/droite

                }
                //Création pont du bas si y a une sortie
                if(sortie % 1000 >= 100 && ligne >= height + decalage && colonne == sortieBasY) {
                    tableauMap[ligne][colonne-1] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne][colonne+1] = '#';
                    this.posSortie=new Pos(height+decalage-1,sortieBasY);
                    this.posMonster=new Pos(sortieBasY,height+decalage-1);
                    this.bas=new Pos(height+decalage-1,sortieBasY);

                }
                //Création pont de droite si y a une sortie
                if(sortie % 100 >= 10 && ligne == sortieDroiteX && colonne >= width + decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.posSortie=new Pos(sortieDroiteX,width + decalage-1);
                    this.posMonster=new Pos(width + decalage-1,sortieDroiteX);
                    this.droite=new Pos(sortieDroiteX,width + decalage-1);

                }
                //Création pont de gauche si y a une sortie
                if(sortie % 10 >= 1 && ligne == sortieGaucheX && colonne <= decalage) {
                    tableauMap[ligne-1][colonne] = '#';
                    tableauMap[ligne][colonne] = '.';
                    tableauMap[ligne+1][colonne] = '#';
                    this.posSortie=new Pos(sortieGaucheX,decalage+1);
                    this.posMonster=new Pos(decalage+1,sortieGaucheX);
                    this.gauche=new Pos(sortieGaucheX,decalage+1);

                }
                //Remplissage de vide si rien n'a été remplie
                if(tableauMap[ligne][colonne] == '\u0000') {
                    tableauMap[ligne][colonne] = ' ';
                }
                if(posSortie == null) {
                    int x = (decalage + height)/2;
                    int y = (decalage + width)/2;
                    this.posSortie=new Pos(x,y);
                }
            }
        }

    }
    //Une fois une map créé, il faut rajouté la liaison entre deux maps
    // Cette liaison entre une map X et une map Y est representé par la sortie de la map X, qui donne sur l'entrée de la map Y
    // La sortie de X est créé lors de l'appel de créationMap(), l'entrée de Y est ensuite créé avec creationCheminDepuisExte
    // La pos en argument correspond a la répresentation de la Pos de sortie de la map X , reporté sur la map Y
    // Une sortie en haut a gauche sur X , deviens une entrée en haut a droite sur Y
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
                this.spawnPos = new Pos(curseurLigne, curseurColonne); // après avoir créé le couloirs d'entréé on set la position de spawn comme étant le début de ce couloir
            } else {
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            }
        } else {
            curseurLigne = alignementLigne(curseurLigne, curseurColonne, directionLigne);
            curseurColonne = alignementColonne(curseurLigne, curseurColonne, directionColonne);
            if(directionColonne < 0) {
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            } else {
                this.spawnPos = new Pos(curseurLigne, curseurColonne);
            }
        }
        this.creationCheminInterne(this.spawnPos,this.getPosSortie());
    }
    // fonction pour créé des couloirs verticaux
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
    // fonction pour créé des couloirs horizontaux
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

    // fait apparaitre une case, different de moveProps qui déplace une entité et remplace sa derniere case
    // l'interet est de forcer des valeurs du tableauMap
    public void popProps( int newPosX, int newPosY, char Props)
    {
        tableauMap[newPosY][newPosX] = Props;
    }
    // popProps mais compatible avec l'utilisation de coordonnée
    public void popProps(Pos coordonne,char Props)
    {
        popProps(coordonne.getY(), coordonne.getX(), Props);
    }
    // indique si la case ciblé est valide pour se déplacé ou non
    public boolean isValide(int gaucheDroite, int basHaut)
    {
        if(gaucheDroite<0 || basHaut<0 || gaucheDroite>=tailleReelY || basHaut >= tailleReelX)
        {
            return false;
        }
        if (tableauMap[basHaut][gaucheDroite] == '.' || tableauMap[basHaut][gaucheDroite] == 'O' || tableauMap[basHaut][gaucheDroite] == 'P' || tableauMap[basHaut][gaucheDroite] == 'D' || tableauMap[basHaut][gaucheDroite] == '-' || tableauMap[basHaut][gaucheDroite] == ',') {
            return true; //
        }
        else if (tableauMap[basHaut][gaucheDroite] == 'X' || tableauMap[basHaut][gaucheDroite] == 'L' || tableauMap[basHaut][gaucheDroite] == '#' || tableauMap[basHaut][gaucheDroite] == 'T')
        { // =='X'

            return false; // reviens a faire if('.',','){true}{else false} mais on se laisse la possibilité de faire d'autre actions si le joueur tente de rentré dans une case non valide
        }
        else{return false;}
    }
    // isValide mais compatible avec Pos
    public boolean isValide(Pos coordonne)
    {
        return isValide(coordonne.getY(),coordonne.getX());
    }
    // retourne la map
    public char[][] getTableauMap() {
        return tableauMap;
    }
    // affiche la map, uniquement utilisé pour faire des test unitaires sur map
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
    // créé une copy de la map actuelle
    public Map copy(){
        char[][] old=this.getTableauMap();
        char[][] current=new char[tailleReelX][tailleReelY];
        for(int i=0; i<tailleReelX; i++)
            for(int j=0; j<tailleReelY; j++)
                current[i][j]=old[i][j];
        Map map=new Map(current,this.seedMap,this.sortie,this.width,this.height,this.tailleReelX,this.tailleReelY,this.haut,this.bas,this.droite,this.gauche,this.spawnPos,this.posSortie,this.cache,this.biome,this.decisionCase);
        return map;
    }
    // Determine si il existe au moins un chemin entre Pos debut et Pos fin, sans passé par pos aEvite
    // PosEvite correspond a la derniere position traversé , pour évité de créé une boucle infini
    // cette methode laisse une trace des chemins déja traversé, ce qui détruit la map, il faut donc la lancé de la maniere suivante this.copy().IsCheminFromTo()
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
    // Cette méthode créé une chemin entre Pos debut et Pos fin si aucun chemin n'existe déja
    public boolean creationCheminInterne(Pos debut, Pos fin){
        if( posSortie==null )
        {
           return false;
        }
        Pos aEvite;
        if(debut.getY()==decalage+width-1 && debut.getX()!=decalage+1 && debut.getX()!=decalage+height-1){aEvite=debut.addY(1);}  //// on cherche la position a ne pas controler lors de la recherche de chemin
        if(debut.getY()==decalage+width+1 && debut.getX()!=decalage+1 && debut.getX()!=decalage+height-1){aEvite=debut.addY(-1);} //// cette position est en réalité le bout du couloir vers une autre map
        if(debut.getX()==decalage+1 && debut.getY()!=decalage+1 && debut.getY()!=decalage+width-1){aEvite=fin.addX(-1);}
        if(debut.getX()==decalage-1 && debut.getY()!=decalage+1 && debut.getY()!=decalage+width-1){aEvite=fin.addX(-1);}
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
        Seed seed2 = new Seed();
        System.out.println(seed2.getAnswer(0));
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