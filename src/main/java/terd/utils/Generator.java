package terd.utils;

import terd.Map.Map;
import terd.Map.Pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Generator {

    private static final int SEED_START = 57;
    private static final int MINIMUM_PATH_LENGTH = 10;

    private final Seed seed;
    private final int mapWidth;
    private final int mapHeight;
    private final int minLargeur;
    private final int minHauteur;
    public int tailleReelX;
    public int tailleReelY;
    public int spawnLigne;
    public int spawnColonne;
    private final int length;
    private final Map[][] maps;

    public Generator(Seed seed, int mapWidth, int mapHeight, int minLargeur, int minHauteur) {
        this.seed = seed;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.minHauteur = minHauteur;
        this.minLargeur = minLargeur;
        this.maps = new Map[mapHeight][mapWidth];
        length = seed.getAnswer(SEED_START) + MINIMUM_PATH_LENGTH;
        generate();
    }

    public static void main(String[] args) {
        Generator generator = new Generator(new Seed(), 4, 10, 10,5);
        generator.affichage();
    }

    public void affichage() {
        //     System.out.println(length);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mapHeight; i++) {
            for(int j = 0; j < mapWidth; j++) {
                if (maps[i][j] != null) {
                    sb.append(String.format("[%d]", maps[i][j].getSortie()));
                } else {
                    sb.append("[X X]");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    private void generate() {
        int seedPos = SEED_START + 1;
        int x = seed.getAnswer(seedPos);
        if (x > mapWidth - 1) {
            x = x % (mapWidth - 1);
        }

        seedPos++;
        int y = seed.getAnswer(seedPos);
        if (y > mapHeight - 1) {
            y = y % (mapHeight - 1);
        }

        Location caseLocation = new Location(x, y);
        int nextMapDir = getNextMapDirectionInt(seedPos, caseLocation);
        Map map = new Map(minHauteur, minLargeur, seed, nextMapDir,seedPos);
        maps[caseLocation.getY()][caseLocation.getX()] = map;
        spawnLigne = caseLocation.getY();
        spawnColonne = caseLocation.getX();

        for (int i = 0; i < length; i++) {
            seedPos++;
            caseLocation = getNextMapLocation(seedPos, caseLocation);
            if (caseLocation == null) {
                // There is no other map location possible, abort.
                break;
            }

            nextMapDir = getNextMapDirectionInt(seedPos, caseLocation);

            if(i == length - 1) {
                map = new Map(minHauteur, minLargeur, seed, 0, seedPos);
            } else {
                map = new Map(minHauteur, minLargeur, seed, nextMapDir, seedPos);
            }
            this.tailleReelX = map.getTailleReelX();
            this.tailleReelY = map.getTailleReelY();
            maps[caseLocation.getY()][caseLocation.getX()] = map;
            if(i==length-1){
                Pos basDroite = new Pos(map.getDecalage()+map.getHeight(),map.getDecalage()+map.getWidth());
                Pos hautgauche = new Pos(map.getDecalage(),map.getDecalage());
                map.setPosSortie(new Pos((hautgauche.getX()+ basDroite.getX())/2, (hautgauche.getY()+ basDroite.getY())/2));
            }
        }

    }

    private int getNextMapDirectionInt(int seedPos, Location caseLocation) {
        // Compute the position of next map.
        Location nextCaseLocation = getNextMapLocation(seedPos + 1, caseLocation);

        if (nextCaseLocation == null) {
            return 0;
        }
        int nextMapDir = 0;
        if (caseLocation.getX() + 1 == nextCaseLocation.getX()) {
            nextMapDir += Direction.RIGHT.getValue();
        }
        if (caseLocation.getX() - 1 == nextCaseLocation.getX()) {
            nextMapDir += Direction.LEFT.getValue();
        }
        if (caseLocation.getY() + 1 == nextCaseLocation.getY()) {
            nextMapDir += Direction.DOWN.getValue();
        }
        if (caseLocation.getY() - 1 == nextCaseLocation.getY()) {
            nextMapDir += Direction.UP.getValue();
        }

        return nextMapDir;
    }

    //                       seed position to look at  previous location of the case
    private Location getNextMapLocation(int atSeedPos, Location orginalLoc) {
        Location caseLocation = new Location(orginalLoc.getX(), orginalLoc.getY()); // remove side effect, I don't want to modify originalLoc.

        List<Location> available = new ArrayList<>();

        if (isPossibleLocation(caseLocation, Direction.UP)) {
            available.add(new Location(caseLocation.getX(), caseLocation.getY() - 1));
        }
        if (isPossibleLocation(caseLocation, Direction.DOWN)) {
            available.add(new Location(caseLocation.getX(), caseLocation.getY() + 1));
        }

        if (isPossibleLocation(caseLocation, Direction.LEFT)) {
            available.add(new Location(caseLocation.getX() - 1, caseLocation.getY()));
        }
        if (isPossibleLocation(caseLocation, Direction.RIGHT)) {
            available.add(new Location(caseLocation.getX() + 1, caseLocation.getY()));
        }

        if (available.size() == 0) {
            return null;
        }

        if (available.size() == 1) {
            return available.get(0);
        }

        int answer = seed.getAnswer(atSeedPos);
        if (available.size() == 2) {
            if (answer > 7) {
                return available.get(1);
            } else {
                return available.get(0);
            }
        }

        if (available.size() == 3) {
            if (answer > 9) {
                return available.get(2);
            } else if (answer > 4) {
                return available.get(1);
            } else {
                return available.get(0);
            }
        }

        if (answer > 6) {
            return available.get(3);
        } else if (answer > 4) {
            return available.get(2);
        } else if (answer > 2) {
            return available.get(1);
        } else {
            return available.get(0);
        }
    }

    public boolean isPossibleLocation(Location location, Direction direction) {
        Location nvLoc = new Location(location.getX(), location.getY());

        switch (direction) {
            case UP:
                nvLoc.setY(nvLoc.getY() - 1);
                break;
            case DOWN:
                nvLoc.setY(nvLoc.getY() + 1);
                break;
            case LEFT:
                nvLoc.setX(nvLoc.getX() - 1);
                break;
            case RIGHT:
                nvLoc.setX(nvLoc.getX() + 1);
                break;
        }

        if (!isInside(nvLoc.getX(), nvLoc.getY())) {
            return false;
        }
        return maps[nvLoc.getY()][nvLoc.getX()] == null;
    }

    public boolean isInside(int x, int y) {
        return !(x >= mapWidth || x < 0 || y >= mapHeight || y < 0);
    }

    public Map[][] getMaps() {
        return maps;
    }

    public Map[][] getFloor() {
        return maps;
    }

}
