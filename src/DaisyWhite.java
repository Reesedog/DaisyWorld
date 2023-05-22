public class DaisyWhite extends Daisy {
    public DaisyWhite() {
        super(Params.WHITE_ALBEDO);
    }

    public Daisy createDaisy(){
        return new DaisyWhite();
    }
}
