package terd.utils;

import java.util.Random;

/**
 * Seed class.
 */
public class Seed {

    public static final Integer SEED_LENGTH = 500;
    private String seed;
    private int offset = 0;

    /**
     * Seed constructor
     */
    public Seed() {
        this.generateSeed();
    }
    public Seed(Seed seedb,int modif) { // genere une nouvelle seed a partir d'une existante
        this.generateSeed(seedb, modif);
    }
    public Seed(String fake){this.seed=fake;} // pour obtenir une seed prevue
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
    private void generateSeed(Seed seedb,int modif) { // genere une nouvelle seed a partir d'une existante
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int i=1;
        while (stringBuilder.length() < SEED_LENGTH) {
            stringBuilder.append(Integer.toHexString(seedb.getAnswer(i*modif)+i*modif));
            i++;
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
    public static void main(String[] args) {
        Seed seed=new Seed("111111111111111111111111111111");
        Seed seed2=new Seed(seed,1);
        System.out.print("Seed de base : ");
        System.out.print(seed.getSeed());
        System.out.print(" taille :");
        System.out.println(seed.getSeed().length());

        System.out.print("Seed n + 1    : ");
        System.out.print(seed2.getSeed());
        System.out.print(" taille :");
        System.out.println(seed2.getSeed().length());

        Seed seed3=new Seed(seed,2);
        System.out.print("Seed n + 2    : ");
        System.out.print(seed3.getSeed());
        System.out.print(" taille :");
        System.out.println(seed3.getSeed().length());

        Seed seed15=new Seed(seed,18);
        System.out.print("Seed n + 17   : ");
        System.out.print(seed15.getSeed());
        System.out.print(" taille :");
        System.out.println(seed15.getSeed().length());

    }
}


