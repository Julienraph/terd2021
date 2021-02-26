package terd.Map;

import terd.utils.Seed;



public class Map {
    private char[][] tableauMap;
    Seed seedMap;
    public Map(int x,int y, Seed seedMap){
        int Colonne;
        this.seedMap=seedMap;
        tableauMap=new char[x][y];
        for(Colonne=0;Colonne<y;Colonne++)
        {
            int Ligne;
            for(Ligne=0;Ligne<x;Ligne++)
            {
                whatToPutAt(Ligne,Colonne);
            }
        }
    }
    private void whatToPutAt(int ligne,int colonne) // choisi quel case placé a une position donnée en fonction de la seed
    {
        int oracle = this.seedMap.getAnswer(ligne+colonne);
        if(oracle<7) {
            tableauMap[ligne][colonne]='.';
        }
        else if(oracle>=7 && oracle<=11){
            tableauMap[ligne][colonne]='/';
        }
        else if(oracle>11 && oracle <14){
            tableauMap[ligne][colonne]='L';
        }
        else{
            tableauMap[ligne][colonne]='X';
        }
    }
    public void moveProps(int posActuelX, int posActuelY,int NewPosX,int NewPosY, char Props) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        whatToPutAt(posActuelX,posActuelY); //
        tableauMap[NewPosX][NewPosY]=Props;
    }
    boolean isValide(int posX,int posY)  // indique si la case ciblé est valide pour se déplacé ou non
    {
        if(tableauMap[posX][posY]=='L' || tableauMap[posX][posY]=='/' || tableauMap[posX][posY]=='.')
        {
            return true; //
        }
        else{ // =='X'
            return false;
        }
    }
/*
    public void printTest(){
        int i;
        for(i=0;i<3;i++)
        {
            int j;
            for(j=0;j<3;j++)
            {
                System.out.print(tableauMap[i][j]);
            }
            System.out.print("\n");
        }
    }*/
/*
    public static void main(String[] args) {
        Seed seed=new Seed();
        Map map =new Map(3,3,seed);
        map.printTest();
        map.moveProps(0,0,1,0,'X');
        System.out.println(map.isValide(1,0));
        System.out.println(map.isValide(0,1));
        System.out.println(seed.getSeed());
    }*/
}

