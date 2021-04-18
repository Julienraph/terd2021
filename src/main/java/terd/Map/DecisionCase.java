package terd.Map;

import terd.utils.Seed;

public class DecisionCase {

    private Seed seed;


    private char caseCommune;
    private char caseDeBase;
    private int borneBase = 6;

    private int chanceCommune =6;
    private final int chanceDebutCommune = 6;

    private char caseRare;
    private int chanceRare = 10;
    private final int chanceDebutRare = 10;

    private char caseExceptionnel;
    private int chanceExceptionnel = 13;
    private int chanceDebutExceptionnel = 13;

    // 0 1 2 3 4 5 | 6 7 8 9 | 10 11 12 | 13 14 15

    private int baseRepetition;
    private int communeRepetition;
    private int rareRepetition;
    private int exceptionnelRepetition;
    private int increment = 0;

    private int nbrBiomeCommune=0;
    private int nbrBiomeRare=0;
    private int nbrBiomeExceptionnel=0;
    // biome

    private char[][] biome;

    public DecisionCase(Seed seed, char base, char commune, char rare, char exceptionnel, int seedPos) {
        this.seed = seed;
        this.caseDeBase = base; //  .
        this.caseCommune = commune; // ,
        this.caseRare = rare;  // L
        this.caseExceptionnel = exceptionnel; // X
        baseRepetition=0;
        communeRepetition=0;
        rareRepetition=0;
        exceptionnelRepetition=0;
    }

    public char[][] DonneMoiUneMap(int width, int height)
    {
        int i=0;
        int j=0;
        int tailleBiomeX;
        int tailleBiomeY;
        int decalage=0;
        int nbrBiome=0;
        char[][] map=new char[height][width];
        if(width>=height)
        {
            tailleBiomeY=width/2;
            tailleBiomeX=height;
        }
        else{
            tailleBiomeY=height/2;
            tailleBiomeX=width;
        }
        this.resetbiome('.','L',',','X');
        for(nbrBiome=0;nbrBiome<2;nbrBiome++)// fix pour l'instant
        {
            for (i = 0; i < tailleBiomeX-1; i++) {
                for (j = decalage; j < tailleBiomeY-1; j++) {
                    if(i==0 || j==0)
                    {
                        map[i][j]=this.pasDeBiome(seed.getAnswer(increment));
                        increment++;
                    }
                    else{
                        map[i][j] = this.DonneMoiUneCase(map[i - 1][j], map[i][j - 1]);
                    }
                }
            }
            decalage=j;
            tailleBiomeY=width;
            this.resetbiome(',','T','.','X');
        }
        return map;
    }
    public void resetbiome(char base,char commun,char rare, char exceptionnel)
    {
        this.seed=new Seed(seed,10);
        this.resetProba();
        this.caseDeBase=base;
        this.caseCommune=commun;
        this.caseRare=rare;
        this.caseExceptionnel=exceptionnel;
    }
    private void resetProba() {
        borneBase=7;
        chanceCommune = chanceDebutCommune;
        chanceRare = chanceDebutRare;
        chanceExceptionnel = chanceDebutExceptionnel;
    }
/*
    public char DonneMoiUneCase(char dessus, char derriere) {
        if(dessus==derriere && (dessus==caseCommune || dessus==caseExceptionnel || dessus==caseRare))
        {
            return dessus;
        }
        return pasDeBiome();

    }*/
    public char DonneMoiUneCase(char dessus, char derriere) {
        this.increment++;
        int decision = seed.getAnswer(increment);
        if(dessus==derriere && (dessus==caseCommune || dessus==caseExceptionnel || dessus==caseRare))
        {
            return dessus;
        }
        return pasDeBiome(decision);

    }
    public char DonneMoiUneCase(char dessus, char derriere, char diagonal) {
        this.increment++;
        int decision = seed.getAnswer(increment);
        if(dessus==derriere && (dessus==caseCommune || dessus==caseExceptionnel || dessus==caseRare))
        {
            return dessus;
        }
        else{
            if(diagonal==caseExceptionnel)
            {
                if(decision>=6)
                {
                    return caseExceptionnel;
                }
            }
            if(derriere==caseRare)
            {
                if(decision>=6)
                {
                    return caseRare;
                }
            }
        }
        return pasDeBiome(decision);

    }
        private char pasDeBiome(int decision)
        {
            if(decision>=chanceDebutExceptionnel){   // 0 1 2 3 4 5 6 7 | 8 9 10 11 | 12 13 14 |  15
                chanceExceptionnel--;  // 0 1 2 3 4 5 6 | 7 8 9 10 | 11  12 13 | 14  15
                chanceRare--;
                chanceCommune--;
                exceptionnelRepetition++;
                if(exceptionnelRepetition>4)
                {
                    resetProba();
                }
                return caseExceptionnel;
            }
            if(decision>=chanceRare){  //  0 1 2 3 4 5 6 7 | 8 9 10 11 | 12 13 14 |  15
                chanceExceptionnel++; //  0 1 2 3 4 5 6 | 7 8 9 10 | 11 12 13 14   15 |
                chanceRare--; //
                chanceCommune--; //  0 1 2 3 4 5 6 | 7 8 9 10 | 11 12 13 14   15 |
                rareRepetition++;
                if(rareRepetition>4)
                {
                    resetProba();
                }
                return caseRare;
            }
            if(decision>=chanceCommune)   // 0 1 2 3 4 5 6 7 | 8 9 10 11 | 12 13 14 |  15
            {
                chanceCommune--;  // 0 1 2 3 4 5 6 | 7 8 9 10 11 | 12 13 14 |  15
                chanceRare++;      // 0 1 2 3 4 5 6 | 7 8 9 10 11 12 |  13 | 14 15
                chanceExceptionnel--;
                communeRepetition++;
                if(communeRepetition>4)
                {
                    resetProba();
                }
                return caseCommune;
            }
            //else                  // 0 1 2 3 4 5 6 7 | 8 9 10 11 | 12 13 14 |  15
            chanceExceptionnel--;   // 0 1 2 3 4 5 6 7 | 8 9 10 11 | 12 13 | 14 15
            chanceRare-=2;          // 0 1 2 3 4 5 6 7 | 8 9 | 10 11 12 13 | 14 15
            chanceCommune-=3;       // 0 1 2 3 4 | 5 6 7 8 9 | 10 11 12 13 | 14 15
            return caseDeBase;
        }



}
