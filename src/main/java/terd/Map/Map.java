package terd.Map;

import terd.utils.Seed;



public class Map {
    private char[][] tableauMap;
    public Map(int x,int y, Seed seedMap){
        int Colonne;
        tableauMap=new char[x][y];
        for(Colonne=0;Colonne<y;Colonne++)
        {
            int Ligne;
            for(Ligne=0;Ligne<x;Ligne++)
            {
                whatToPutAt(Ligne,Colonne,seedMap);
            }
        }
    }
    private void whatToPutAt(int ligne,int colonne,Seed seedMap)
    {
        int oracle = seedMap.getAnswer(ligne+colonne);
        //System.out.println(oracle);
        if(oracle<7) {
            tableauMap[ligne][colonne]='.';
        }
        else if(oracle>7 && oracle<11){
            tableauMap[ligne][colonne]='/';
        }
        else if(oracle>10 && oracle <14){
            tableauMap[ligne][colonne]='L';
        }
        else{
            tableauMap[ligne][colonne]='X';
        }
        //this.tableauMap[ligne][colonne] ='a';
    }
    public void printTest(){
        int i;
        for(i=0;i<15;i++)
        {
            int j;
            for(j=0;j<15;j++)
            {
                System.out.print(tableauMap[i][j]);
            }
            System.out.print("\n");
        }
    }
    public static void main(String[] args) {
        Seed seed=new Seed();
        Map map =new Map(15,15,seed);
        map.printTest();
        System.out.println(seed.getSeed());
    }
}

