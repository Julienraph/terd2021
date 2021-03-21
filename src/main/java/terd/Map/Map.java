package terd.Map;

import terd.utils.Seed;


public class Map {
    Seed seedMap;
    private char[][] tableauMap;
    private final int nextMapDirection;

    public Map(int x, int y, Seed seedMap, int nextMapDirection) {
        int Colonne;
        this.seedMap = seedMap;
        tableauMap = new char[x][y];
        for (Colonne = 0; Colonne < y; Colonne++) {
            int Ligne;
            for (Ligne = 0; Ligne < x; Ligne++) {
                whatToPutAt(Ligne, Colonne);
            }
        }
        this.nextMapDirection = nextMapDirection;
    }

    public static void main(String[] args) {
        Seed seed = new Seed();
        Map map = new Map(3, 3, seed, 0);
        map.moveProps(0, 0, 1, 0, 'X');
        System.out.println(map.isValide(1, 0));
        System.out.println(map.isValide(0, 1));
        System.out.println(seed.getSeed());

    }

    /**
     * Get the next map direction int.
     *
     * @return int
     */
    public int getNextMapDirection() {
        return nextMapDirection;
    }

    public void spawnPlayer(int x, int y, char skin) {
        tableauMap[x][y] = skin;
    }

    private void whatToPutAt(int ligne, int colonne) // choisi quel case placé a une position donnée en fonction de la seed
    {
        int oracle = this.seedMap.getAnswer(ligne * 3 + colonne * 5) % 16;
        if (oracle < 7) {
            tableauMap[ligne][colonne] = ':';
        } else if (oracle <= 11) {
            tableauMap[ligne][colonne] = '.';
        } else if (oracle < 14) {
            tableauMap[ligne][colonne] = 'L';
        } else {
            tableauMap[ligne][colonne] = 'X';
        }
    }

    //Appelé si le joueur passe dans une autre map, cela fait réapparaitre l'ancienne case de l'ancienne map
    public void resetCase(int posActuelX, int posActuelY) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        whatToPutAt(posActuelY, posActuelX); //
    }

    public void moveProps(int posActuelX, int posActuelY, int newPosX, int newPosY, char Props) // deplace un Props sur la map et fait réapparaitre l'ancienne case
    {
        whatToPutAt(posActuelY, posActuelX); //
        tableauMap[newPosY][newPosX] = Props;
    }

    public boolean isValide(int colonne, int ligne)  // indique si la case ciblé est valide pour se déplacé ou non
    {
        if (tableauMap[ligne][colonne] == 'L' || tableauMap[ligne][colonne] == '.' || tableauMap[ligne][colonne] == ':') {
            return true; //
        } else { // =='X'
            return false;
        }
    }

    public char[][] getTableauMap() {
        return tableauMap;
    }

    @Override
    public String toString() {
//        return "Map{" +
//                "tableauMap=" + Arrays.deepToString(tableauMap) +
//                ", seedMap=" + seedMap +
//                '}';
        return "Map";
    }
}

