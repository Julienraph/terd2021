package terd.Player;

import terd.Map.Pos;
import terd.item.Arme;
import terd.item.Competence;
import terd.item.Inventaire;

public interface Props {
   void takeDamages(int damages);
   void setPosX(int x);
   void setPosY(int y);
   void setNewMap(int newPosY, int newPosX);
   void setPosition(int newPosX, int newPosY);
   int getX();
   int getY();
   Pos getPos();
   void setPos(Pos pos);
   int getPosEtageY();
   char getSkin();
   void setSkin(char skin);
   int getPV();
   int getCredit();
   void setCredit(int credit);
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
   double getDistance(Pos posProps, Pos posProps2);
   char getCache();
   void setCache(char cache);
   boolean isBeside(Pos posProps);
   String getName();
   void setXP(int xp);
   int getXP();
   String addXP(int monsterXP);
   void useWeapon(Props victime);
   double getDistance(Props props);

}
