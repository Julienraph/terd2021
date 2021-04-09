package terd.utils;

import java.util.Random;

/**
 * Seed class.
 */
public class Seed {

    public static final Integer SEED_LENGTH = 32;
    private String seed;
    private int offset = 0;

    /**
     * Seed constructor
     */
    public Seed() {
        this.generateSeed();
    }

    /**
     * Generate a new seed.
     */
    private void generateSeed() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < SEED_LENGTH) {
            stringBuilder.append(Integer.toHexString(random.nextInt()));
        }

        seed = stringBuilder.toString();
    }

    /**
     * get Seed Offset
     *
     * @return offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * set Seed Offset
     *
     * @param offset offset
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Add 1 to the Seed offset
     */
    public void addOffset() {
        addOffset(1);
    }

    /**
     * Add the 'count' to seed offset
     *
     * @param count count
     */
    public void addOffset(int count) {
        offset = offset + count;
    }

    /**
     * Get Seed Char for a certain position.
     * If the position is greater than the seed length
     * the seed will loop
     *
     * @param atPos Position at which to get the char
     * @return the char at position atPos
     */
    public int getAnswer(int atPos) {
        atPos = atPos + offset;
        if (atPos > (SEED_LENGTH - 1)) {
            atPos = atPos % SEED_LENGTH;
        }
        return Integer.parseInt(String.valueOf(seed.charAt(atPos)), 16);
    }

    /**
     * Get the whole seed
     *
     * @return the seed
     */
    public String getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        return "Seed{" +
                "seed='" + seed + '\'' +
                '}';
    }
}
