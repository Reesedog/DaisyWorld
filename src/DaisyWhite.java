import java.util.Random;

public class DaisyWhite extends Daisy {

    private static Random random = new Random();
    public DaisyWhite() {
        super(Params.WHITE_ALBEDO + random.nextDouble() * 1.2 - 0.6);
    }

    public DaisyWhite(int age) {
        super(Params.WHITE_ALBEDO, age);
    }
    public Daisy createDaisy(){
        return new DaisyWhite();
    }
}
