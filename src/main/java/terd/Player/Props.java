package terd.Player;

import terd.item.Arme;
import terd.item.Competence;
import terd.item.Inventaire;

public interface Props {
   void takeDamages(int damages);
   double getDistance(AbstractProps abstractProps);
   void setPosX(int x);
   void setPosY(int y);
   void setNewMap(int newPosY, int newPosX);
   void setPosition(int newPosX, int newPosY);
   int getX();
   int getY();
   int getPosEtageY();
   char getSkin();
   void setSkin(char skin);
   int getPv();
   int getMaxPV();
   int getSpeed();
   int getPosEtageX();
   Arme getMainWeapon();
   void setMainWeapon(Arme mainWeapon);
   int getLevelProps();
   void setLevelProps(int levelProps);
   void addPV(int pv);
   Competence getMainCompetence();
   void setMainCompetence(Competence mainCompetence);
   Inventaire getInventaire();
}
