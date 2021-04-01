package terd.Map;

import terd.utils.Seed;

public class DecisionCase {
    private char caseDeBase;   private int borneMax=10;        private final int borneMaxDebut=10;
    private char caseCommune;  private int chanceCommune=10;  private final int chanceDebutCommune=10;
    private char caseRare;     private int chanceRare=13;    private final int chanceDebutRare=13;
    private char caseExceptionnel; private int chanceExceptionnel=15;
    private Seed seed;
    private int increment=0;
    public DecisionCase(Seed seed)
    {
        this.seed=seed;
        this.caseDeBase='.';
        this.caseCommune='/';
        this.caseRare='L';
        this.caseExceptionnel='X';
    }
    private void resetProba(){
        borneMax=borneMaxDebut;
        chanceCommune=chanceDebutCommune;
        chanceRare=chanceDebutRare;
    }

    public char DonneMoiUneCase(char dessus,char derriere)
    {
        int decision=seed.getAnswer(this.increment);
        this.increment++;
       /* if(increment%20==0)
        {
            resetProba();
        }*/
       // System.out.print(decision);
        //System.out.print("=");
        changementProba(dessus,derriere);
        if(decision<=borneMax)
        {
            return caseDeBase;
        }
        if(decision>=chanceExceptionnel){
            return caseExceptionnel;
        }
        if(decision>=chanceRare){
            return caseRare;
            }
        if(decision>=chanceCommune){
            return caseCommune;
        }
        else{
            return caseDeBase;  // impossible;
        }
    }

    private void changementProba(char dessus,char derriere)
    {
        //chanceCommune  chanceRare  chanceExceptionnel
        if(dessus==caseCommune) // /
        {
            chanceCommune--;
        }
        if(derriere==caseCommune)
        {
            chanceCommune--;
        }
//////////////////////////
        if(dessus==caseRare)  // L
        {
            chanceRare--;
        }
        if(derriere==caseRare)
        {
            chanceRare--;
        }
//////////////////////////
        if(dessus==caseExceptionnel) // X
        {
            chanceCommune--;
            chanceExceptionnel++;
        }
        if(derriere==caseExceptionnel) {
            chanceCommune--;
            chanceExceptionnel++;
        }
//////////////////////////
        if(dessus==caseDeBase)  // .
        {
           // borneMax++;
        }
        if(derriere==caseDeBase)
        {
           // borneMax++;
        }
    }




}
