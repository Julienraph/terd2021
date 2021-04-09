package terd.Map;

import terd.utils.Seed;

public class DecisionCase {
    private char caseDeBase;   private int borneMax=9;        private final int borneMaxDebut=9;
    private char caseCommune;  private int chanceCommune=9;  private final int chanceDebutCommune=9;
    private char caseRare;     private int chanceRare=13;    private final int chanceDebutRare=13;
    private char caseExceptionnel; private int chanceExceptionnel=15;
    private Seed seed;
    private int increment=0;

    // biome
    private int CaseRareRepetition;
    private int CaseCommuneRepetition;

    public DecisionCase(Seed seed)
    {
        this.seed=seed;
        this.caseDeBase='.';
        this.caseCommune=',';
        this.caseRare='L';
        this.caseExceptionnel='X';
        this.CaseRareRepetition=0;
        this.CaseCommuneRepetition=0;
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
        if(increment>20)
        {
            resetProba();
        }
       // System.out.print(decision);
        //System.out.print("=");
        changementProba(dessus,derriere);

        if(decision>=chanceExceptionnel){
            return caseExceptionnel;
        }
        if(decision>=chanceRare){
            return caseRare;
        }
        if(decision>=chanceCommune){
            return caseCommune;
        }
        else{return caseDeBase;}
        /*if(decision<=borneMax)
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
        }*/
    }

    private void changementProba(char dessus,char derriere)
    {
        //chanceCommune  chanceRare  chanceExceptionnel
        if(dessus==caseCommune) // /
        {
            if(CaseCommuneRepetition<3)
            {
                chanceCommune-=4;  // Biome de 3 de haut MAX
            }
            else
            {
                CaseCommuneRepetition=0;
                chanceCommune++;
            }
            CaseCommuneRepetition++;

        }
        if(derriere==caseCommune)
        {
            chanceCommune--;
        }
//////////////////////////
        if(dessus==caseRare)  // L
        {
            if(CaseRareRepetition<3)
            {
                chanceRare-=4;
                borneMax-=4;// Biome de 3 de haut MAX
            }
            else
            {
                CaseRareRepetition=0;
                borneMax=borneMaxDebut;
                chanceRare++;
            }
            CaseRareRepetition++;
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
            borneMax--;
        }
        if(derriere==caseDeBase)
        {
            borneMax++;
        }
    }




}
