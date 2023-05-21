import java.util.Random;

public abstract class Daisy {
    private int age;
    private double albedo;

    public Daisy(double albedo) {
        this.albedo = albedo;
    }

    public abstract Daisy createDaisy();

    public int checkSurvivability(double temperature) {
        Random random = new Random();
        if (age < Params.MAX_AGE) {
            double seed_threshold = 0.1457 * temperature - 0.0032 * Math.pow(temperature, 2) - 0.6443;
            if (random.nextDouble() < seed_threshold) {
                return 1;
            }else return 0;
        }
        return -1;
    }

    public double getAlbedo() {
        return albedo;
    }
}
