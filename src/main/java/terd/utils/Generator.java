package terd.utils;

import terd.Map.Map;

import java.util.HashMap;

public class Generator {

    private static final int SEED_START = 57;
    private static final int MINIMUM_PATH_LENGTH = 10;

    private final Seed seed;
    private final int mapWidth;
    private final int mapHeight;
    private final int length;
    private final HashMap<Location, Map> maps = new HashMap<>();

    public Generator(Seed seed, int mapWidth, int mapHeight) {
        this.seed = seed;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        length = seed.getAnswer(SEED_START) + MINIMUM_PATH_LENGTH;
        generate();
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
        Seed seed = new Seed();
        Map map = new Map(seed.getAnswer(seedPos + 1), seed.getAnswer(seedPos + 2), seed);
        maps.put(caseLocation, map);

        for (int i = 0; i < length; i++) {
            seedPos++;
            boolean foundLoc = false;
            int dir = seed.getAnswer(seedPos);
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

            seed = new Seed();
            map = new Map(seed.getAnswer(seedPos + 1), seed.getAnswer(seedPos + 2), seed);
            maps.put(caseLocation, map);
        }

    }


    public Map[][] getFloor() {
        Map[][] mapsArray = new Map[mapHeight][mapWidth];
        for (java.util.Map.Entry<Location, Map> entry : maps.entrySet()) {
            Location loc = entry.getKey();
            mapsArray[loc.getY()][loc.getX()] = entry.getValue();
        }
        return mapsArray;
    }

}
