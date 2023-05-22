import java.util.Random;

public abstract class Daisy {
    private int age;
    private double albedo;

    public Daisy(double albedo) {
        this.albedo = albedo;
        this.age = 0;
    }

    public abstract Daisy createDaisy();

    public int checkSurvivability(double temperature) {
        Random random = new Random();
        age+=1;
        if (age < Params.MAX_AGE) {
            double seed_threshold = 0.1457 * temperature - 0.0032 * Math.pow(temperature, 2) - 0.6443;
            if (random.nextDouble() < seed_threshold) {
                return 1;       // seed
            }else return 0;     // alive and do nothing
        }
        return -1;              // dead
    }

    public double getAlbedo() {
        return albedo;
    }
}
