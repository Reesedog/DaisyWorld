import java.util.Random;

public class DaisyBlack extends Daisy{
    private static Random random = new Random();
    public DaisyBlack() {
        super(Params.BLACK_ALBEDO + random.nextDouble() * 1.2 - 0.6);
    }

    public DaisyBlack(int age) {
        super(Params.BLACK_ALBEDO, age);
    }
    public Daisy createDaisy(){
        return new DaisyBlack();
    }
}
