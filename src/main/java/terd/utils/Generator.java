package terd.utils;

import terd.Map.Map;

import java.util.Arrays;
import java.util.Objects;

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

        System.out.println("Int: " + nextMapDir);
        System.out.println("Direction: " + (Direction.getByValue(nextMapDir) != null ? Direction.getByValue(nextMapDir).toString() : "?" ));

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
                if (!isInside(caseLocation.getX(), caseLocation.getY())) {
                    dir = 8;
                } else {
                    foundLoc = true;
                }
            } else if (dir >= 8) {
                // down
                caseLocation.setY(caseLocation.getY() - 1);
                if (!isInside(caseLocation.getX(), caseLocation.getY())) {
                    dir = 12;
                } else {
                    foundLoc = true;
                }
            } else if (dir >= 4) {
                // left
                caseLocation.setX(caseLocation.getX() - 1);
                if (!isInside(caseLocation.getX(), caseLocation.getY())) {
                    dir = 0;
                } else {
                    foundLoc = true;
                }
            } else {
                // right
                caseLocation.setX(caseLocation.getX() + 1);
                if (!isInside(caseLocation.getX(), caseLocation.getY())) {
                    dir = 4;
                } else {
                    foundLoc = true;
                }
            }
        }

        // OK, a new location has been found, let's check there is not already a map at this position.
        if (maps[caseLocation.getY()][caseLocation.getX()] != null) {
            // Oh, crap! We can handle this, calm down!
            Location newLoc = new Location(orginalLoc.getX(), orginalLoc.getY());
            if (isPossibleLocation(newLoc, Direction.UP)) {
                newLoc.setY(newLoc.getY() - 1);
                caseLocation = newLoc;
            } else if (isPossibleLocation(newLoc, Direction.DOWN)) {
                newLoc.setY(newLoc.getY() + 1);
                caseLocation = newLoc;
            } else if (isPossibleLocation(newLoc, Direction.LEFT)) {
                newLoc.setX(newLoc.getX() - 1);
                caseLocation = newLoc;
            } else if (isPossibleLocation(newLoc, Direction.RIGHT)) {
                newLoc.setX(newLoc.getX() + 1);
                caseLocation = newLoc;
            }
        }

        return caseLocation;
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

        if (!isInside(location.getX(), location.getY())) {
            return false;
        }
        return maps[location.getY()][location.getX()] == null;
    }

    public boolean isInside(int x, int y) {
        return !(x >= mapWidth || x < 0 || y >= mapHeight || y < 0);
    }

    public Map[][] getFloor() {
        return maps;
    }

}
