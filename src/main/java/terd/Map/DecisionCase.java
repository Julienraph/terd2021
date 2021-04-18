/*
Auteur : Mascaro Valentin
Derniere modification : 18/04 par Mascaro Valentin // création DonneMoiUneCase pasDeBiome DonneMoiUneMap // Suppression des ancienne methodes du meme nom
                                                   // création de plusieurs methodes differentes pour obtenir une case
WIP absolument pas satisfait du remplissage des map
Objectif : Obtenir une map "ordonnée", a savoir, des parties avec beaucoup de case semblable, des biomes composé de beaucoup d'arbre ou lac ou colline ou d'herbes



Les fonctions sont assez peu commenté car pas fini, voir probablement seront entierement remanié
Toutefois l'idée est la suivante

La seed comporte une suite de chiffre entre 0 et 15 ( 0àF )
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
Pour déterminé une case a une position donné, on commence par regardé la valeur de la seed a la position X
Disons 12            0 1 2 3 4 5 6 7 8 9 10 11 (12) 13 14 15
Maintenant on regarde pour 7 quel serait la valeur de la case ?
    Pour repondre a cette question on associe des bornes a chaque valeur de retour possible

    caseCommune : 6 et +  0 1 2 3 4 5 | 6 7 8 9 10 11 12 13 14 15
    caseRare : 10 et + 0 1 2 3 4 5 6 7 8 9 | 10 11 12 13 14 15
    caseExceptionnel : 13 et + 0 1 2 3 4 5 6 7 8 9 10 11 12 | 13 14 15

    Dans le code on commence par controler si une case est exceptionnel ou rare, ou commune on obtiens donc
    0 1 2 3 4 5 | 6 7 8 9 | 10 11 (12) | 13 14 15
    La valeur 12 correspond donc a commune et rare, mais puisque l'on vérifie d'abord si elle est rare, elle sera rare

    Viens ensuite la partie la compliqué, obtenir des biomes, un biome étant défini par une suite d'une meme case, on va augmenté
    la chance d'obtenir un type de case, si celui est déja tombé  ( fonction pasDeBiome(), oui pasDeBiome() créé des biomes, car cette fonction est appelé c'est que le biome n'est pas encore créé )

    Ainsi disons que j'obtiens une caseRare je vais supprimé de 1 la hauteur de ma borne Rare
    caseCommune : 6 et +  0 1 2 3 4 5 | 6 7 8 9 10 11 12 13 14 15
    caseRare : 9 et + 0 1 2 3 4 5 6 7 8 | 9 10 11 12 13 14 15
    caseExceptionnel : 13 et + 0 1 2 3 4 5 6 7 8 9 10 11 12 | 13 14 15

    Diminuant ainsi la chance d'obtenir une caseCommune de 1, et augmentant celle d'une caseRare de 1
    On peux aussi augmenté la borne d'une des cases pour diminuer la chance de l'obtenir et donc augmenté indirectement les chances d'obtenir la case de rareté inférieur

    Le danger étant de supprimé completement les caseCommunes, je rajoute donc un 'gardefou' resetProba() qui remet les probabilité d'obtenir chaque case, comme au début de la classe


    Viens ensuite la partie la plus probable a etre modifié, dans la methode DonneMoiUneCase est demandé en argument la valeur de la case precedente, et celle du dessus ( voir meme celle en diagonal gauche )
    L'interet est de, en fonction de ces trois cases, modifié de maniere brut la valeur de la case ciblé, a savoir, si 'dessus' et 'derriere' sont identiques, c'est que nous sommes dans un biome
    par conséquent la case obtiens cette meme valeur

    A l'avenir cela devrai déclenché une autre fonction qui construira un biome.
 */

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
                if(decision>=9)
                {
                    return caseExceptionnel;
                }
            }
            if(derriere==caseRare)
            {
                if(decision>=9)
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
