package terd.utils;

import terd.Map.Map;

import java.util.Arrays;

public class Generator {

    private static final int SEED_START = 57;
    private static final int MINIMUM_PATH_LENGTH = 10;

    private final Seed seed;
    private final int mapWidth;
    private final int mapHeight;
    private final int length;
    private final Map[][] maps;

    public Generator(Seed seed, int mapWidth, int mapHeight) {
        this.seed = seed;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        this.maps = new Map[mapHeight][mapWidth];

        length = seed.getAnswer(SEED_START) + MINIMUM_PATH_LENGTH;
        generate();
    }

    public static void main(String[] args) {
        Generator generator = new Generator(new Seed(), 10, 10);
        System.out.println(Arrays.deepToString(generator.getFloor()));
    }

    private void generate() {
        int seedPos = SEED_START + 1;
        int x = seed.getAnswer(seedPos);
        if (x > mapWidth) {
            x = x % mapWidth;
        }

        seedPos++;
        int y = seed.getAnswer(seedPos);
        if (y > mapHeight) {
            y = y % mapHeight;
        }

        Location caseLocation = new Location(x, y);
        int nextMapDir = getNextMapDirectionInt(seedPos, caseLocation);
        Map map = new Map(seed.getAnswer(seedPos + 1), seed.getAnswer(seedPos + 2), seed, nextMapDir);
        maps[caseLocation.getY()][caseLocation.getX()] = map;

        for (int i = 0; i < length; i++) {
            seedPos++;
            caseLocation = this.getNextMapLocation(seedPos, caseLocation);
            nextMapDir = getNextMapDirectionInt(seedPos, caseLocation);

            map = new Map(seed.getAnswer(seedPos + 1), seed.getAnswer(seedPos + 2), seed, nextMapDir);
            maps[caseLocation.getY()][caseLocation.getX()] = map;
        }

    }

    private int getNextMapDirectionInt(int seedPos, Location caseLocation) {
        // Compute the position of next map.
        Location nextCaseLocation = this.getNextMapLocation(seedPos + 1, caseLocation);
        System.out.println("next loc: " + nextCaseLocation);

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

        System.out.println("dir int: " + nextMapDir);

        return nextMapDir;
    }

    private Location getNextMapLocation(int atSeedPos, Location orginalLoc) {
        Location caseLocation = new Location(orginalLoc.getX(), orginalLoc.getY()); // remove side effect, I don't want to modify originalLoc.
        boolean foundLoc = false;
        int dir = this.seed.getAnswer(atSeedPos);
        while (!foundLoc) {
            if (dir >= 12) {
                // up
                caseLocation.setY(caseLocation.getY() + 1);
                if (caseLocation.getY() > mapHeight) {
                    // Hors de la map!!!
                    dir = 8;
                } else {
                    foundLoc = true;
                }
            } else if (dir >= 8) {
                // down
                caseLocation.setY(caseLocation.getY() - 1);
                if (caseLocation.getY() < 0) {
                    // Hors de la map!!!
                    dir = 12;
                } else {
                    foundLoc = true;
                }
            } else if (dir >= 4) {
                // left
                caseLocation.setX(caseLocation.getX() - 1);
                if (caseLocation.getX() < 0) {
                    // Hors de la map!!!
                    dir = 0;
                } else {
                    foundLoc = true;
                }
            } else {
                // right
                caseLocation.setX(caseLocation.getX() + 1);
                if (caseLocation.getX() > mapWidth) {
                    // Hors de la map!!!
                    dir = 4;
                } else {
                    foundLoc = true;
                }
            }
        }
        System.out.println("Direction: " + dir);
        return caseLocation;
    }

    public Map[][] getFloor() {
        return maps;
    }

}
