package terd.utils;

import java.util.Random;

/**
 * Seed class.
 */
public class Seed {

    public static final Integer SEED_LENGTH = 16;
    private String seed;

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
     * Get Seed Char for a certain position.
     * If the position is greater than the seed length
     * the seed will loop
     *
     * @param atPos Position at which to get the char
     * @return the char at position atPos
     */
    public int getAnswer(int atPos) {
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
}
