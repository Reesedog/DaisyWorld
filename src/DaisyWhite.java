public class DaisyWhite extends Daisy {
    public DaisyWhite() {
        super(Params.WHITE_ALBEDO);
    }

    public DaisyWhite(int age) {
        super(Params.WHITE_ALBEDO, age);
    }

    public Daisy createDaisy(){
        return new DaisyWhite();
    }
}
