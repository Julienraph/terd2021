package terd.Map;

import terd.utils.Seed;

public class DecisionCase {
    private char caseDeBase;   private int borneMax=9;        private final int borneMaxDebut=9;
    private char caseCommune;  private int chanceCommune=9;  private final int chanceDebutCommune=9;
    //private char caseMoinsCommune; private int chanceMoinsCommune=11; private final int chanceDebutMoinsCommune=11;
    private char caseRare;     private int chanceRare=13;    private final int chanceDebutRare=13;
    private char caseExceptionnel; private int chanceExceptionnel=15;
    private Seed seed;
    private int increment=0;

    // biome
    private int CaseRareRepetitionDessus;
    private int CaseCommuneRepetitionDessus;
    private int CaseRareRepetitionLigne;
    private int CaseCommuneRepetitionLigne;
    private int seedPos;
    public DecisionCase(Seed seed,char base,char commune,char rare, char exceptionnel,int seedPos)
    {
        this.seed=seed;
        this.caseDeBase=base;
        this.caseCommune=commune;
        this.caseRare=rare;
        this.caseExceptionnel=exceptionnel;
        //this.caseMoinsCommune='T';
        this.CaseRareRepetitionDessus=0;
        this.CaseCommuneRepetitionDessus=0;
        this.CaseRareRepetitionLigne=0;
        this.CaseCommuneRepetitionLigne=0;
        this.seedPos=seedPos;
    }
    private void resetProba(){
        borneMax=borneMaxDebut;
        chanceCommune=chanceDebutCommune;
        chanceRare=chanceDebutRare;
        chanceExceptionnel=15;
    }

    public char DonneMoiUneCase(char dessus,char derriere)
    {
        int decision=seed.getAnswer(this.increment);
       // this.increment++;
        this.increment=seed.getAnswer(seedPos);
        seedPos++;
        //System.out.println(increment);

       // System.out.print(decision);
        //System.out.print("=");
        changementProba(dessus,derriere);

        if(decision>=chanceExceptionnel){
            return caseExceptionnel;
        }
       /*if(decision>=chanceMoinsCommune){
            return caseMoinsCommune;
        }*/
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
        if(dessus==caseCommune) // ,
        {
            if(CaseCommuneRepetitionDessus<3)
            {
                chanceCommune-=4;
                chanceRare++;
                // Biome de 3 de haut MAX
            }
            else
            {
                resetProba();
                CaseCommuneRepetitionDessus=0;
                chanceCommune++;
            }
            CaseCommuneRepetitionDessus++;

        }
        if(derriere==caseCommune)
        {
            if(CaseCommuneRepetitionLigne<4)
            {
                chanceCommune-=2;  // Biome de 3 de haut MAX
                chanceRare++;
            }
            else
            {
                resetProba();
                CaseCommuneRepetitionLigne=0;
                chanceCommune++;
            }
            CaseCommuneRepetitionLigne++;
        }
////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////
        if(dessus==caseRare)  // L
        {
            if(CaseRareRepetitionDessus<3)
            {

                chanceRare-=3;
               // Biome de 3 de haut MAX
            }
            else
            {
                resetProba();
                CaseRareRepetitionDessus=0;
                chanceRare++;
            }
            CaseRareRepetitionDessus++;
        }
        if(derriere==caseRare)
        {
            if(CaseRareRepetitionLigne<4)
            {
                chanceRare-=2;
            }
            else
            {
                resetProba();
                CaseRareRepetitionLigne=0;
                borneMax=borneMaxDebut;
                chanceRare++;
            }
            CaseRareRepetitionLigne++;
        }
////////////////////////////////////////////////////
// /////////////////////////////////////////////////
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
            resetProba();
        }
    }
}
